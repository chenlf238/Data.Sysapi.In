package test.service;

import com.alibaba.fastjson.JSONObject;
import data.sysapi.interfaces.IPicCloudStoreService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.Base;
import witparking.core.entitys.ReturnMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @className PicCloudStoreServiceTest
 * @Description TODO
 * @Author zt
 * @Date 2019/11/13 9:38
 * @Version 1.0
 **/
public class PicCloudStoreServiceTest extends Base {

    @Autowired
    IPicCloudStoreService iPicCloudStoreService;

    @Test
    public void doPictureStore() {
       /* {"GatewayIP":"172.25.117.98","GatewayPort":"9011","Token":"1574403404013500"}*/
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gatewayIP", "172.25.117.105");
        jsonObject.put("serialId", "192168991_1");
        jsonObject.put("gatewayPort", "9011");
        jsonObject.put("token", "1574321778006560");
        List<String> picUrlArr = new ArrayList<String>();
        picUrlArr.add("http://parkingphoto.oss-cn-qingdao.aliyuncs.com/3703050010010021/2019/11/22/DI37030500100100212019112213230900A.jpg?Expires=1574403945&OSSAccessKeyId=t8aDdX9z3nTiEvWK&Signature=LDAzhleFv4F6bWGO76uAs0ZxuaM%3D");
        jsonObject.put("picUrlArr", picUrlArr);
        String s = iPicCloudStoreService.doPictureStore(jsonObject.toJSONString());
        logger.info("##################{}###################", s);
    }

}
