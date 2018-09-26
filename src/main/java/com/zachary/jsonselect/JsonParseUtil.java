package com.zachary.jsonselect;

import com.zachary.jsonselect.util.HtmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * JSON parse
 *
 * @author zachary
 */
public class JsonParseUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonParseUtil.class);

    /**
     * parse jsonp to JSON
     *
     * @param jsonp
     * @return
     */
    public static JSON parseJsonp2Json(String jsonp) {
        jsonp = HtmlUtil.getHtmlWithoutNR(jsonp);
        if (jsonp.contains("({") && jsonp.endsWith("})")) {
            jsonp = jsonp.substring(jsonp.indexOf("({") + 1, jsonp.length() - 1);
        } else if (jsonp.contains("([") && jsonp.endsWith("])")) {
            jsonp = jsonp.substring(jsonp.indexOf("([") + 1, jsonp.length() - 1);
        }
        return parseJsonStr2Json(jsonp);
    }


    /**
     * parse jsonStr to JSON
     *
     * @param jsonStr
     * @return
     */
    public static JSON parseJsonStr2Json(String jsonStr) {
        if (jsonStr.startsWith("{") && jsonStr.endsWith("}")) {
            return parseJsonStr2JsonObject(jsonStr);
        } else if (jsonStr.startsWith("[") && jsonStr.endsWith("]")) {
            return parseJsonStr2JsonArray(jsonStr);
        } else {
            logger.error("jsonStr is not json!");
            return null;
        }
    }

    /**
     * parse jsonStr to JSONObject
     *
     * @param jsonStr
     * @return
     */
    public static JSONObject parseJsonStr2JsonObject(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            logger.warn("jsonStr is blank, cannot parse");
            return null;
        }
        jsonStr = HtmlUtil.getHtmlWithoutNR(jsonStr);

        JSONObject json = null;
        try {
            json = JSON.parseObject(jsonStr);
        } catch (Exception e) {
            logger.error("parse jsonStr 2 JsonObject exception!");
        }
        return json;
    }


    /**
     * parse jsonStr to JSONOArray
     *
     * @param jsonStr
     * @return
     */
    public static JSONArray parseJsonStr2JsonArray(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            logger.warn("jsonStr is blank, cannot parse");
            return null;
        }
        jsonStr = HtmlUtil.getHtmlWithoutNR(jsonStr);

        JSONArray json = null;
        try {
            json = JSON.parseArray(jsonStr);
        } catch (Exception e) {
            logger.error("parse jsonStr 2 JsonArray exception!");
        }
        return json;
    }

    /**
     * whether jsonStr is jsonp or not
     *
     * @param jsonStr
     * @return
     */
    public static boolean isJsonp(String jsonStr) {
        jsonStr = HtmlUtil.getHtmlWithoutNR(jsonStr);
        if (StringUtils.isBlank(jsonStr) || jsonStr.endsWith("()")) {
            logger.error("param [jsonStr] is blank or json content is blank");
            return false;
        }
        if (!StringUtils.isBlank(jsonStr) && jsonStr.length() >= 4 && (jsonStr.endsWith("})") || jsonStr.endsWith("])"))
                && (jsonStr.contains("({") || jsonStr.contains("(["))) {
            return true;
        }
        return false;
    }

    /**
     * get value
     *
     * @param json
     * @param key
     * @param clazz
     * @return
     */
    public static <T> T getValueOfKey(JSONObject json, String key, Class<T> clazz) {
        if (json == null) {
            logger.error("param [json] is null");
            return null;
        }
        Set<String> keySet = json.keySet();
        if (keySet == null || keySet.size() <= 0 || StringUtils.isBlank(key)) {
            logger.warn("param [json] keySet is blank or param [key] is blank, can not get value of key[%s]", key);
            return null;
        }
        for (String item : keySet) {
            if (item.equals(key)) {
                try {
                    return (T) json.getObject(key, clazz);
                } catch (Exception e) {
                    logger.error("get value of key[%s] use type[%s] failed", key, clazz.getName());
                }
            }
        }

        return null;
    }

    /**
     * parse jsonArray to List
     *
     * @param jsonArray
     * @param clazz
     * @return
     */
    public static <T> List<T> getValueOfKey(JSONArray jsonArray, Class<T> clazz) {
        if (jsonArray == null) {
            logger.error("param [jsonArray] is null");
            return null;
        }
        List<T> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add((T) jsonArray.getObject(i, clazz));
        }
        return list;
    }

}
