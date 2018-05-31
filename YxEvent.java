package com.yxkj.platform.event;

/**
 * 事件对象,发出时转化为
 * Created by bruce on 2017/9/6.
 */
public class YxEvent {
    //实体ID
    private String entityId;
    private String extraJson;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getExtraJson() {
        return extraJson;
    }

    public void setExtraJson(String extraJson) {
        this.extraJson = extraJson;
    }
}
