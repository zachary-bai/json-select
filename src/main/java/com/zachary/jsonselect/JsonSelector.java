package com.zachary.jsonselect;

import com.zachary.jsonselect.util.HtmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * json selector, pls use public getJsonNode()
 *
 * @author zachary
 */
public class JsonSelector {

    private static final Logger logger = LoggerFactory.getLogger(JsonSelector.class);

    private static final String SINGLETON_SEPARATOR = ":";
    private static final String JSON_ARRAY_ALL_INDEX = "*";

    private JSON jsonRoot;

    public JsonSelector(String jsonStr) {
        jsonStr = HtmlUtil.getHtmlWithoutNR(jsonStr);
        if (StringUtils.isBlank(jsonStr) || jsonStr.endsWith("()")) {
            logger.error("param [jsonStr] is blank or json content is blank");
            this.jsonRoot = null;
        }
        if (JsonParseUtil.isJsonp(jsonStr)) {
            this.jsonRoot = JsonParseUtil.parseJsonp2Json(jsonStr);
        } else {
            this.jsonRoot = JsonParseUtil.parseJsonStr2Json(jsonStr);
        }
    }

    public JsonSelector(JSON json) {
        this.jsonRoot = json;
    }

    public JSON getRootJSON() {
        return this.jsonRoot;
    }

    /**
     * @param selector
     * @return
     * @throws JsonTypeNotKnownException
     * @throws SelectorIllegalException
     * @throws JsonIllegalException
     */
    public <T> T getJsonNode(String selector) throws JsonTypeNotKnownException, SelectorIllegalException, JsonIllegalException {
        if (StringUtils.isBlank(selector)) {
            logger.error("param [selector] is blank, illegale");
            return null;
        }
        checkSelector(selector);
        Object value = this.jsonRoot;
        String[] selectors = selector.split(SINGLETON_SEPARATOR);
        String lastSingleton = null;
        for (String singleton : selectors) {
            if (value == null) {
                throw new JsonIllegalException("singleton: " + lastSingleton + ", corresponding json is null, cannot getChildNode");
            } else if (value instanceof JSONObject) {
                value = getChildNode((JSONObject) value, singleton);
            } else if (value instanceof JSONArray) {
                value = getChildNode((JSONArray) value, singleton);
            } else if (value instanceof List<?>) {
                return (T) value;
            } else {
                throw new SelectorIllegalException("singleton: " + lastSingleton + ", corresponding json is base type, base type (neither JsonObj nor JsonArr) cannot getChildNode");
            }
            lastSingleton = singleton;
        }

        return (T) value;
    }

    /**
     * return (T) Object in JSONType
     *
     * @param json
     * @param singleton
     * @return
     * @throws JsonTypeNotKnownException
     * @throws SelectorIllegalException
     */
    private <T> T getChildNode(JSONObject json, String singleton) throws JsonTypeNotKnownException, SelectorIllegalException {
        checkSingleton(singleton);
        T value = (T) JsonParseUtil.getValueOfKey(json, getKeyOfSingleton(singleton), getClazzOfSingleton(singleton));

        return value;

    }

    /**
     * return List<T> XXX if singleton's parent and itself like a[XXX]:X[*], or return (T) Object if singleton's parent and itself like
     * a[XXX]:X[0]
     *
     * @param json
     * @param singleton
     * @return
     * @throws JsonTypeNotKnownException
     * @throws SelectorIllegalException
     * @throws IndexOutOfBoundsException
     */
    private <T> T getChildNode(JSONArray json, String singleton) throws JsonTypeNotKnownException, SelectorIllegalException, IndexOutOfBoundsException {
        checkSingleton(singleton);
        List<T> list = (List<T>) JsonParseUtil.getValueOfKey(json, getClazzOfSingleton(singleton));
        String key = getKeyOfSingleton(singleton);
        if (key.equals(JSON_ARRAY_ALL_INDEX)) {  // return the whole list
            return (T) list;
        } else if (StringUtils.isNotBlank(key) && StringUtils.isNumeric(key)) {  // return value of index=key
            try {
                return (T) list.get(Integer.valueOf(key));
            } catch (IndexOutOfBoundsException e) {
                String s = "list size is " + list.size() + ", index is " + key + ", IndexOutOfBoundsException";
                logger.error(s);
                throw new IndexOutOfBoundsException(s);
            }
        } else {
            throw new SelectorIllegalException("JsonArray, singleton: " + singleton + ", is illegale");
        }
    }


    /**
     * get key of singleton
     *
     * @param singleton
     * @return
     * @throws JsonTypeNotKnownException
     * @throws SelectorIllegalException
     */
    private String getKeyOfSingleton(String singleton) throws JsonTypeNotKnownException, SelectorIllegalException {
        checkSingleton(singleton);
        return singleton.substring(2, singleton.length() - 1);
    }

    /**
     * get clazz of singleton
     *
     * @param <T>
     * @param singleton
     * @return
     * @throws JsonTypeNotKnownException
     * @throws SelectorIllegalException
     */
    private <T> Class<T> getClazzOfSingleton(String singleton) throws JsonTypeNotKnownException, SelectorIllegalException {
        JsonType jsonType = getJsonTypeOfSingleton(singleton);

        return jsonType.getClazz();
    }

    /**
     * get JsonType of singleton
     *
     * @param singleton
     * @return
     * @throws JsonTypeNotKnownException
     * @throws SelectorIllegalException
     */
    private JsonType getJsonTypeOfSingleton(String singleton) throws JsonTypeNotKnownException, SelectorIllegalException {
        checkSingleton(singleton);
        char typeIndex = singleton.charAt(0);
//		checkJsonType(typeIndex);
        return JsonType.getTypeByIndex(typeIndex);
    }

    /**
     * check all selector
     *
     * @param selector
     * @throws JsonTypeNotKnownException
     * @throws SelectorIllegalException
     */
    private void checkSelector(String selector) throws JsonTypeNotKnownException, SelectorIllegalException {
        if (StringUtils.isBlank(selector)) {
            throw new SelectorIllegalException("selector: " + selector + ", is illegale");
        }
        String[] selectors = selector.split(SINGLETON_SEPARATOR);
        for (String string : selectors) {
            checkSingleton(string);
        }
    }

    /**
     * check singleton
     *
     * @param singleton
     * @throws JsonTypeNotKnownException
     * @throws SelectorIllegalException
     */
    private void checkSingleton(String singleton) throws JsonTypeNotKnownException, SelectorIllegalException {
        if (StringUtils.isBlank(singleton) || singleton.length() < 4) {
            throw new SelectorIllegalException("singleton: " + singleton + ", is illegale");
        }
        Pattern pattern = Pattern.compile("^[a-z]{1}\\[.+\\]$");
        Matcher matcher = pattern.matcher(singleton);
        if (!matcher.find()) {
            throw new SelectorIllegalException("singleton: " + singleton + ", is illegale");
        }
        char typeIndex = singleton.charAt(0);
        checkJsonType(typeIndex);
    }

    /**
     * check Json type
     *
     * @param typeIndex
     * @throws JsonTypeNotKnownException
     * @see JsonType
     */
    private void checkJsonType(char typeIndex) throws JsonTypeNotKnownException {
        JsonType jsonType = JsonType.getTypeByIndex(typeIndex);
        if (jsonType == null) {
            throw new JsonTypeNotKnownException("this type index[" + typeIndex + "] not known, pls check JsonType");
        }
    }

}

