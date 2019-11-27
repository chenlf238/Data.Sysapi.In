package data.sysapi.interfaces;

import witparking.core.exceptions.BusinessException;

/**
 * @className IPicCloudStoreService
 * @Description 图片云存储对外暴露接口
 * @Author zt
 * @Date 2019/11/5 9:57
 * @Version 1.0
 **/
public interface IPicCloudStoreService {


    /**
     * 图片存储
     * @param jsonStr
     * @return
     * @throws BusinessException
     */
    String doPictureStore(String jsonStr) throws BusinessException;


}
