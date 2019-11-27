package data.sysapi.interfaces;

/**
 * Created by LBL on 2019/10/10.
 */
public interface IParkingService {

    /**
     * 驶入驶出信息.
     *
     * @param jsonStr 驶入驶出信息Json串
     */
    void parkinginfo(String jsonStr);

}
