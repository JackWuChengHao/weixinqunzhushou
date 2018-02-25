package com.rs.wxmgr.wechat.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class Pair implements Serializable {

    private static final long serialVersionUID = -8596267872040063811L;
    @JSONField(name="Key") private String key;
    @JSONField(name="Val")private String val;
    
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getVal() {
        return val;
    }
    public void setVal(String val) {
        this.val = val;
    }
    
}
