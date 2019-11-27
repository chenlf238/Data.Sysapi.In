package data.controller;

import com.alibaba.fastjson.JSON;
import data.sysapi.interfaces.IPicCloudStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import witparking.core.utils.security.Base64Utils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @className PicCloudStoreController
 * @Description 图片云存储-内网部署
 * @Author zt
 * @Date 2019/11/6 10:16
 * @Version 1.0
 **/
@Controller
@RequestMapping(value = "picCloudStoreIn")
public class PicCloudStoreController {

    private static final Logger logger = LoggerFactory.getLogger(PicCloudStoreController.class);

    @Autowired
    IPicCloudStoreService picCloudStoreService;

    @ResponseBody
    @RequestMapping(value = "doPictureStore", method = RequestMethod.POST)
    public String doPictureStore(Map<String,String> map , HttpServletRequest request) {
        FileOutputStream out = null;
        ServletInputStream in = null;
        try {
            in = request.getInputStream();
            byte[] bytes = new byte[1024];
            File file = new File("d://ff.jpg");
            out = new FileOutputStream(file);
            // List<String> base64StrArr = new ArrayList<>();
            int lens = -1;
            StringBuilder content = new StringBuilder();
            while ((lens = in.read(bytes)) > 0) {
                logger.info("##bytes.size()={}##", bytes.length);
                // logger.info("##{}##", bytes);
                /*base64StrArr.add(Base64Utils.encodeToString(bytes));*/
                out.write(bytes);
                content.append(new String(bytes, 0, lens,"GBK"));
            }
            out.flush();
            logger.info("##end##");
            logger.info("##{}##", content.toString());

           /* for (String s : base64StrArr) {
                File file = new File("d://a.jpg");
                out = new FileOutputStream(file);
                out.write(Base64Utils.decodeFromString(s));
            }
            out.flush();*/
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
            }
        }
        return "success";
    }

    /**
     * 取得图片数据
     *
     * @param requestData
     * @param contentType
     * @return
     * @throws IOException
     */
    private byte[] getImgData(byte[] requestData, String contentType)
            throws IOException {
        String txtBody = new String(requestData, "GBK");
        if (!txtBody.contains("image/jpg") && !txtBody.contains("image/jpeg")&& !txtBody.contains("jpg")) {
            return null;
        }
        String boundarytext = contentType.substring(
                contentType.lastIndexOf("=") + 1, contentType.length());
        // 取得实际上传文件的起始与结束位置
        int pos = txtBody.indexOf("filename=\"");
        pos = txtBody.indexOf("\n", pos) + 1;
        pos = txtBody.indexOf("\n", pos) + 1;
        pos = txtBody.indexOf("\n", pos) + 1;
        // 文件描述信息后就文件内容，直到为文件边界为止，从pos开始找边界
        int boundaryLoc = txtBody.indexOf(boundarytext, pos) - 4;
        ByteArrayOutputStream realdatas = null;
        try {
            int begin = ((txtBody.substring(0, pos)).getBytes("GBK")).length;
            int end = ((txtBody.substring(begin, boundaryLoc)).getBytes("GBK")).length;
            realdatas = new ByteArrayOutputStream();
            realdatas.write(requestData, begin, end);
            return realdatas.toByteArray();
        } finally {
            if (null != realdatas) {
                try {
                    realdatas.close();
                } catch (IOException e) {
                    logger.error("处理上传图片数据错误：", e);
                }
            }
        }
    }

}
