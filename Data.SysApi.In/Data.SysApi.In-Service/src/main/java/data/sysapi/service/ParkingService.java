/*
package data.sysapi.service;

import com.alibaba.fastjson.JSONObject;
import data.sysapi.interfaces.IParkingService;
import data.sysapi.service.utils.activeMQ;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import witparking.core.utils.UuidUtil;

import javax.jms.*;
import java.text.SimpleDateFormat;
import java.util.Date;

*/
/**
 * Created by LBL on 2019/10/10.
 *//*

@Service
public class ParkingService implements IParkingService {
    private final Logger logger = LoggerFactory.getLogger(ParkingService.class);

    @Override
    public void parkinginfo(String jsonStr) {
        try {
            JSONObject object = JSONObject.parseObject(jsonStr);
            StringBuilder strBuilder = new StringBuilder();

            //车牌号
            String plateNumber = object.getString("plateNumber");
            //车位
            String spaceCode = object.getString("spaceCode");
            if (spaceCode.length() == 8) {
                spaceCode = spaceCode + "0000";
            }
            //车场地址
            String address = object.getString("address");
            //采集时间(yyyy-MM-dd HH:mm:ss)
            String createTime = object.getString("createTime");
            //方向编号
            String direction = object.getString("direction");
            //停车图片
            String picture1 = object.getString("picture1");
            String picture2 = object.getString("picture2");
            //号牌颜色
            String colour = object.getString("colour");
            //数据上传时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String upTime = df.format(new Date());

            String uuid = UuidUtil.newid();
            strBuilder.append("VMKS,2.4,0099,").append(uuid).append(",").append(plateNumber).append(",02,").append(address)
                    .append(",1,,,,").append(createTime).append(",,").append(direction).append(",").append(picture1).append(",")
                    .append(picture2).append(",,,,").append("01,,").append(colour).append(",").append(upTime);
            //车辆信息发送到即墨（海信）指定的topic
            activeMQ.sendMsg(strBuilder.toString(),"HIATMP.HISENSE.PASS.PASSINF","");
        } catch (Exception e) {
            logger.error("parkinginfoForJm异常：{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}
*/
