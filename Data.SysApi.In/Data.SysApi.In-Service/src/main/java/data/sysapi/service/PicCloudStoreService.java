package data.sysapi.service;

import com.alibaba.fastjson.JSONObject;
import data.sysapi.interfaces.IPicCloudStoreService;
import data.sysapi.service.utils.PicHttpUtil;
import data.sysapi.service.utils.SafetyUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import witparking.core.entitys.ReturnMessage;
import witparking.core.exceptions.BusinessException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

/**
 * @className PicCloudStoreService
 * @Description 图片云存储service
 * @Author zt
 * @Date 2019/11/5 10:00
 * @Version 1.0
 **/
@Service
public class PicCloudStoreService implements IPicCloudStoreService {

    private static final Logger logger = LoggerFactory.getLogger(PicCloudStoreService.class);

    // 图片存储url
    private static final String URL_PIC_STORE = "/HikCStor/Picture/Write";

    public String doPictureStore(String jsonStr) throws BusinessException {
        logger.info("【图片云存储】#############入参打印：{}##########################", jsonStr);
        if (StringUtils.isBlank(jsonStr)) {
            throw new BusinessException("入参不能为空");
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        String serialId = jsonObject.getString("serialId");
        if (StringUtils.isBlank(serialId)) {
            throw new BusinessException("serialId不能为空");
        }
        String gatewayIp = jsonObject.getString("gatewayIP");
        if (StringUtils.isBlank(gatewayIp)) {
            throw new BusinessException("gatewayIp不能为空");
        }
        String gatewayPort = jsonObject.getString("gatewayPort");
        if (StringUtils.isBlank(gatewayPort)) {
            throw new BusinessException("gatewayPort不能为空");
        }
        String token = jsonObject.getString("token");
        if (StringUtils.isBlank(token)) {
            throw new BusinessException("token不能为空");
        }
        List<String> picUrlList = JSONObject.parseArray(jsonObject.getString("picUrlArr"), String.class);
        if (CollectionUtils.isEmpty(picUrlList)) {
            throw new BusinessException("图片数组不能为空");
        }
        StringBuilder urlSb = new StringBuilder();
        urlSb.append(PicHttpUtil.CLOUD_STORE_IP).append(":").append(PicHttpUtil.CLOUD_STORE_PORT).append(URL_PIC_STORE);
        String url = urlSb.toString();
        logger.info("【图片云存储】##########url：{}###################", url);
        HttpURLConnection conn = null;
        OutputStream out = null;
        BufferedReader reader = null;
        try {
            URL obj = new URL(url);
            conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");
            // POST请求必须设置
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
        } catch (Exception e) {
            logger.error("【图片云存储】################系统异常：{}############", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("系统异常，请稍后再试");
        }

        Calendar cal = Calendar.getInstance();
        // 分隔符
        String frontier = "---------------------------" + System.currentTimeMillis();
        String newLine = "\r\n";
        String contentType = "multipart/form-data;";
        // 设置请求头
        conn.setRequestProperty("Accept", "text/html, application/xhtml+xml, */*");
        String signString = SafetyUtil.getSignString("POST", "", contentType,
                SafetyUtil.getGmtDate(cal.getTime()), URL_PIC_STORE);
        conn.setRequestProperty("Authorization", SafetyUtil.getAuthorization(PicHttpUtil.ACCESS_KEY, PicHttpUtil.SECRET_KEY, signString));
        conn.setRequestProperty("Date", SafetyUtil.getGmtDate(cal.getTime()));
        conn.setRequestProperty("Accept-Language", "zh-CN");
        conn.setRequestProperty("Content-Type", contentType + "boundary=" + frontier);
        conn.setRequestProperty("Host", gatewayIp + ":" + gatewayPort);
        conn.setRequestProperty("Connection", "Keep-Alive");
        try {
            out = new DataOutputStream(conn.getOutputStream());
            // 设置form表单内容
            StringBuilder sb = new StringBuilder();
            sb.append(frontier).append(newLine);
            sb.append("Content-Disposition: form-data; name=\"SerialID\"").append(newLine);
            sb.append(serialId).append(newLine);
            sb.append(frontier).append(newLine);
            sb.append("Content-Disposition: form-data; name=\"PoolID\"").append(newLine);
            sb.append(PicHttpUtil.POOL_ID).append(newLine);
            sb.append(frontier).append(newLine);
            sb.append("Content-Disposition: form-data; name=\"TimeStamp\"").append(newLine);
            sb.append(cal.getTimeInMillis()).append(newLine);
            sb.append(frontier).append(newLine);
            sb.append("Content-Disposition: form-data; name=\"PictureType\"").append(newLine);
            sb.append("1").append(newLine);//当前图片的格式[1：JPG、2：BMP、3：PNG]
            sb.append(frontier).append(newLine);
            sb.append("Content-Disposition: form-data; name=\"Token\"").append(newLine);
            sb.append(token).append(newLine);
            sb.append(frontier).append(newLine);
            out.write(sb.toString().getBytes());
            for (String picUrl : picUrlList) {
                PicHttpUtil.downImage(picUrl, out, frontier);
                int responseCode = conn.getResponseCode();
                logger.info("####responseCode:{}#########", responseCode);
                reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String line = null;
                StringBuilder resultSb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    resultSb.append(line);
                }
                logger.info("【图片云存储】###########resp：{}#############", resultSb.toString());
                // TODO 返回信息解析

            }
        } catch (IOException e) {
            logger.error("【图片云存储】##系统异常：{}##", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ReturnMessage(true, "操作成功", 30, "").toJsonString();
    }



}
