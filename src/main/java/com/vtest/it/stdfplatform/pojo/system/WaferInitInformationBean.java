package com.vtest.it.stdfplatform.pojo.system;

import java.io.Serializable;

public class WaferInitInformationBean implements Serializable {
    public static final long serialVersionUID = 1l;
    private String customCode;
    private String device;
    private String lot;
    private String cp;
    private String waferId;

    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getWaferId() {
        return waferId;
    }

    public void setWaferId(String waferId) {
        this.waferId = waferId;
    }
}
