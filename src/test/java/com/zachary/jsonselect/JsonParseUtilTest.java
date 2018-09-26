package com.zachary.jsonselect;

import com.zachary.jsonselect.util.FileUtil;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class JsonParseUtilTest {

    @Test
    public void testSelectJson() throws IOException {
        URL url = ClassLoader.getSystemClassLoader().getResource("./getInfo.json");
        String content = FileUtil.getContent(url.getPath());
//		content = CommonUtil.replaceBlank(content);
        content = "{dltype: 1,exem: 0,fl: {},hs: 0,ip: \"220.181.90.242\",ls: 0,preview: 87,s: \"o\",sfl: {},tm: 1487931280,publish: \"2017-03-06 10:33:02\"}";
//		content = content.substring(content.indexOf("({") + 1, content.length() - 1);
//		System.out.println(content);
//		JSONObject json = JsonParseUtil.parseJsonp2Json(content);
        JSONObject json = JsonParseUtil.parseJsonStr2JsonObject(content);

        String fn = json.getObject("ip", String.class);
        Integer preview = json.getObject("preview", Integer.class);
//		JSONObject fl = json.getObject("fl", JSONObject.class);
//		JSONObject vl = json.getObject("vl", JSONObject.class);
//		JSONArray vi = vl.getObject("vi", JSONArray.class);
        System.out.println("fn: " + fn);
        System.out.println("preview: " + preview);
//		System.out.println("fl: " + fl);
//		System.out.println("vl: " + vl);
//		System.out.println("vi: " + vi);
        System.out.println("publish: " + json.getString("publish"));
        System.out.println(String.class.getName());

    }

}
