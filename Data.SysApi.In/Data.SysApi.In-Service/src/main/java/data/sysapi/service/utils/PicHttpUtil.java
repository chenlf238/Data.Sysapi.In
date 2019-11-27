package data.sysapi.service.utils;

import com.alibaba.fastjson.JSONObject;
import data.sysapi.model.BestNodeModel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import witparking.core.entitys.ReturnMessage;
import witparking.core.exceptions.BusinessException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @className PicHttpUtil
 * @Description 图片云存储-http工具类
 * @Author zt
 * @Date 2019/11/5 14:21
 * @Version 1.0
 **/
public class PicHttpUtil {

    // 27.223.66.156 外网
    // 119.167.113.94

    // 172.25.100.167

    // private static final String CLOUD_STORE_IP = "http://172.25.100.167";

    // public static final String CLOUD_STORE_IP = "http://119.167.113.94";

    public static final String CLOUD_STORE_IP = "http://192.168.110.250";
    // private static final String CLOUD_STORE_IP = "http://27.223.66.156";
    public static final String CLOUD_STORE_PORT = "6011";
    public static final String ACCESS_KEY = "83SPY3goj6qw7CTFQsx3l9o0n2pF0v62T7K8doqQcKnga9Gxa87H5xfu1A5a423";
    public static final String SECRET_KEY = "U0z6838af8n97elwb7Xc81X187o0dT5wH3W9PAH4i2S0QmK0zz9E879sd9I6885";
    public static final String POOL_ID = "249992726";
    // 获取最优节点的url
    private static final String URL_BEST_NODE = "/HikCStor/BestNode?SerialID=%s&PoolID=%s&Replication=%s";

    private static final Logger logger = LoggerFactory.getLogger(PicHttpUtil.class);

    private volatile static BestNodeModel bestNodeModel;

    /**
     * 获取最优节点
     * 多个线程获取时，防止并发存在
     */
    public static BestNodeModel getBestNodeModel() throws BusinessException {
        if (bestNodeModel == null) {
            synchronized (BestNodeModel.class){
                if (bestNodeModel == null) {
                    bestNodeModel = getBestNode();
                }
            }
        }
        return bestNodeModel;
    }

    private static BestNodeModel getBestNode() throws BusinessException {
        //serialId
        String SerialID = SafetyUtil.getSerialId();
        // 是否开启对客户端的图片信息执行冗余，0：表示不执行冗余，1：表示执行冗余，仅冗余一份，其他为非法参数
        String Replication = "0";
        // 拼接url
        String urlSuffix = String.format(URL_BEST_NODE, SerialID, POOL_ID, Replication);
        StringBuilder urlSb = new StringBuilder();
        urlSb.append(CLOUD_STORE_IP).append(":").append(CLOUD_STORE_PORT).append(urlSuffix);
        String url = urlSb.toString();
        logger.info("【获取最优节点】########url：{}################", url);
        // 创建http连接
        Calendar cal = Calendar.getInstance();
        HttpURLConnection conn = null;
        try {
            URL obj = new URL(url);
            conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("GET");
        } catch (MalformedURLException e) {
            logger.error("【获取最优节点】#####创建Http请求异常：{}######", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("创建HTTP请求异常");
        } catch (IOException e) {
            logger.error("【获取最优节点】#####打开Http连接异常：{}######", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("创建HTTP请求异常");
        }
        conn.setRequestProperty("Host", CLOUD_STORE_IP + ":" + CLOUD_STORE_PORT);
        conn.setRequestProperty("Accept-language", "zh-cn");
        conn.setRequestProperty("Date", SafetyUtil.getGmtDate(cal.getTime()));
        conn.setRequestProperty("Content-Type", "text/json");
        String signString = SafetyUtil.getSignString("GET", "", "text/json",
                SafetyUtil.getGmtDate(cal.getTime()), urlSuffix);
        conn.setRequestProperty("Authorization", SafetyUtil.getAuthorization(ACCESS_KEY, SECRET_KEY, signString));
        conn.setRequestProperty("Connection", "close");

        int responseCode = 0;
        BufferedReader in = null;
        Map<String, String> hash = new HashMap<String, String>();
        try {
            responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new BusinessException("获取最优节点失败！HTTP_CODE:" + responseCode);
            }
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine = null;
            StringBuilder respSb = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                respSb.append(inputLine);
            }
            in.close();
            String respStr = respSb.toString();
            logger.info("【获取最优节点】##############resp返回信息打印：{}#######################", respStr);
            //{"GatewayIP":"172.25.117.188","GatewayPort":"9011","Token":"1574316496556720"}
            JSONObject jsonObject = JSONObject.parseObject(respStr);
            BestNodeModel bestNodeModel = new BestNodeModel();
            bestNodeModel.setGatewayIP(jsonObject.getString("GatewayIP"));
            bestNodeModel.setGatewayPort(jsonObject.getString("GatewayPort"));
            bestNodeModel.setToken(jsonObject.getString("Token"));
            // hash.put("serialId", SerialID);
        } catch (IOException e) {
            logger.error("【获取最优节点】#####IO连接异常：{}######", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bestNodeModel;
    }

    /**
     * 图片写出，直接在此方法写出到指定url
     */
    public static void downImage(String url, OutputStream out , String frontier) {
        if (StringUtils.isBlank(url)) {
            return;
        }
        InputStream in = null;
        try {
            String newLine = "\r\n";
            StringBuilder sb = new StringBuilder();
            sb.append("Content-Disposition: form-data; name=\"Picture\";").append(newLine);
            sb.append("Content-Type:image/jpeg").append(newLine);
            out.write(sb.toString().getBytes());
            URL obj = new URL(url);
            in = obj.openStream();
            byte[] buffer = new byte[1024];
            int length;
            int picLength = 0;
            while ((length = in.read(buffer)) > 0) {
                logger.info("【downImage】####buffer：{}######", buffer);
                out.write(buffer, 0, length);
                picLength += length;
            }
            logger.info("【downImage】####picLength：{}######", picLength);
            in.close();
            sb = new StringBuilder();
            sb.append(frontier).append(newLine);
            sb.append("Content-Disposition: form-data; name=\"PictureLength\"").append(newLine);
            sb.append(picLength).append(newLine);
            sb.append(frontier).append(newLine).append("--");
            out.write(sb.toString().getBytes());

        } catch (Exception e) {
            logger.error("【downImage】#########系统异常：{}#######", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Runnable(){

            @Override
            public void run() {
                BestNodeModel bestNode = getBestNodeModel();
                System.out.println(bestNode);
            }
        });
        Thread thread2 = new Thread(new Runnable(){

            @Override
            public void run() {
                BestNodeModel bestNode = getBestNodeModel();
                System.out.println(bestNode);
            }
        });
        Thread thread3 = new Thread(new Runnable(){

            @Override
            public void run() {
                BestNodeModel bestNode = getBestNodeModel();
                System.out.println(bestNode);
            }
        });
        thread1.start();
        thread2.start();
        thread3.start();
    }


}
