package com.zachary.jsonselect;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.sql.Timestamp;
import java.util.Date;

/**
 * parse json may use these class
 *
 * @author zachary
 */
public enum JsonType {

    Str('s', String.class),
    Int('i', Integer.class),
    Boolean('b', Boolean.class),
    Long('l', Long.class),
    Double('f', Double.class),
    JsonObj('o', JSONObject.class),
    JsonArr('a', JSONArray.class),
    TimeStamp('t', Timestamp.class),
    Date('d', Date.class);

    private char index;
    private Class<?> clazz;


    public static JsonType getTypeByIndex(char index) {
        JsonType[] jsonTypes = JsonType.values();
        for (JsonType jsonType : jsonTypes) {
            if (jsonType.getIndex() == index) {
                return jsonType;
            }
        }
        return null;
    }

    private JsonType(char index, Class<?> clazz) {
        this.index = index;
        this.clazz = clazz;
    }

    public int getIndex() {
        return index;
    }

    public <T> T getClazz() {
        return (T) clazz;
    }

}
