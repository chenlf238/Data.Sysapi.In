package data.sysapi.model;

import java.io.Serializable;

/**
 * @className BestNodeModel
 * @Description 最优节点Model
 * @Author zt
 * @Date 2019/11/26 13:44
 * @Version 1.0
 **/
public class BestNodeModel implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * 最优节点ip
     */
    private String gatewayIP;
    /**
     * 最优节点端口
     */
    private String gatewayPort;
    /**
     * 最优节点token
     */
    private String token;

    public String getGatewayIP() {
        return gatewayIP;
    }

    public void setGatewayIP(String gatewayIP) {
        this.gatewayIP = gatewayIP;
    }

    public String getGatewayPort() {
        return gatewayPort;
    }

    public void setGatewayPort(String gatewayPort) {
        this.gatewayPort = gatewayPort;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
