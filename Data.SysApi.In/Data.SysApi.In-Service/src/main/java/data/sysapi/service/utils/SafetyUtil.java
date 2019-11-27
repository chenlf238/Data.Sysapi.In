package data.sysapi.service.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import witparking.core.utils.security.Base64Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * @className SafetyUtil
 * @Description 安全认证
 * @Author zt
 * @Date 2019/11/5 10:33
 * @Version 1.0
 **/
public class SafetyUtil {

    private static final Logger logger = LoggerFactory.getLogger(SafetyUtil.class);

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private static final String HIKCSTOR = "hikcstor ";

    /**
     * 获取认证信息
     * @param accessKey
     * @param secretKey
     * @param data
     * @return 安全认证结果
     */
    public static String getAuthorization (String accessKey , String secretKey , String data) {
        if (StringUtils.isBlank(accessKey) || StringUtils.isBlank(secretKey)) {
            return null;
        }
        String result = HIKCSTOR + accessKey + ":" + getHmacSHA1(secretKey, data);
        logger.info("【Authorization】########{}#############", result);
        return result;
    }

    /**
     * 生成需要加密的数据
     * @param httpVerb 请求方式
     * @param contentMD5
     * @param contentType 数据类型
     * @param date GMT格式时间
     * @param url 请求资源
     * @return stringToSign
     */
    public static String getSignString(String httpVerb, String contentMD5,String contentType, String date, String url) {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isBlank(httpVerb)) {
            httpVerb = "";
        }
        stringBuilder.append(httpVerb).append("\n");
        if (StringUtils.isBlank(contentMD5)) {
            contentMD5 = "";
        }
        stringBuilder.append(contentMD5).append("\n");
        if (StringUtils.isBlank(contentType)) {
            contentType = "";
        }
        stringBuilder.append(contentType).append("\n");
        if (StringUtils.isBlank(date)) {
            date = "";
        }
        stringBuilder.append(date).append("\n");
        if (StringUtils.isBlank(url)) {
            url = "";
        }
        stringBuilder.append(url);
        logger.info("【生成加密数据】###############StringToSign：{}##################", stringBuilder.toString());
        return stringBuilder.toString();

    }

    /**
     * HMAC-SHA1加密数据
     * @param secretKey 加密key
     * @param data 加密数据
     * @return 加密后base64字符串
     */
    public static String getHmacSHA1(String secretKey, String data) {
        String result = null;
        try {
            SecretKeySpec sigInitKey = new SecretKeySpec(secretKey.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(sigInitKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = Base64Utils.encodeToString(rawHmac);

        } catch (Exception e) {
            logger.error("【HMAC-SHA1加密】######算法加密失败：{}##########", e.getMessage());
            e.printStackTrace();
        }
        logger.info("【HAMC-SHA1加密】###加密结果：{}###########", result);
        return result;
    }

    /**
     * 时间格式转换（GMT）
     * @param date
     * @return
     */
    public static String getGmtDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为GMT
        return sdf.format(date);
    }

    /**
     * 获取请求序列号
     * 图片来源的设备 ID，唯一标记图片的来源（例如：采用 IP+通道；最大长度为 64个字节，仅支持大小写字母、数字、下划线和中横线），长度超过为异常
     * 图片持续存储过程中，请确保 SerialID保持不变
     */
    public static String getSerialId() {
        String serialId = null;
        try {
            InetAddress localHost = InetAddress.getLocalHost();//当前主机ip
            long threadId = Thread.currentThread().getId();//当前线程id
            String localIp = localHost.getHostAddress().replace(".", "");
            serialId = localIp + "_" + threadId;
            if (serialId.length() > 64) {
                serialId = serialId.substring(0, 64);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return serialId;
    }

}
