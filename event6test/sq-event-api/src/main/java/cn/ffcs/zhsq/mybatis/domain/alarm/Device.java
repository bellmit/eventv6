package cn.ffcs.zhsq.mybatis.domain.alarm;

import java.io.Serializable;

/**
 * ffcs全球眼对象
 *
 * @Author sulch
 * @Date 2016-12-10 19:40
 */
public class Device implements Serializable{
    private Long deviceId;//设备编号
    private String deviceName;//设备名称
    private String deviceNum;//设备号
    private String deviceIp;
    private String devicePort;

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getDevicePort() {
        return devicePort;
    }

    public void setDevicePort(String devicePort) {
        this.devicePort = devicePort;
    }
}
