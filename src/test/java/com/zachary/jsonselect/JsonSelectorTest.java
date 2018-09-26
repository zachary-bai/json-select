package com.zachary.jsonselect;

import com.zachary.jsonselect.util.FileUtil;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class JsonSelectorTest {

    /**
     * example
     *
     * @throws JsonTypeNotKnownException
     * @throws SelectorIllegalException
     * @throws IOException
     * @throws JsonIllegalException
     */
    @Test
    public void testJsonSelector() throws JsonTypeNotKnownException, SelectorIllegalException, JsonIllegalException, IOException {
        String jsonStr = "{dltype: 1,exem: 0,fl: {},hs: 0,ip: \"220.181.90.242\",ls: 0,preview: 87,s: \"o\",sfl: {},tm: 1487931280}";
        URL url = ClassLoader.getSystemClassLoader().getResource("./getInfo.json");
        jsonStr = FileUtil.getContent(url.getPath());
        JsonSelector jsonSelector = new JsonSelector(jsonStr);
//		jsonSelector.checkSingleton("s[ip]");
        String ip = jsonSelector.getJsonNode("s[ip]");
        Long tm = jsonSelector.getJsonNode("l[tm]");
        String fvkey = jsonSelector.getJsonNode("o[vl]:a[vi]:o[0]:s[fvkey]");
        String ui2Url = jsonSelector.getJsonNode("o[vl]:a[vi]:o[0]:o[ul]:a[ui]:o[2]:s[url]");
        int vt1 = jsonSelector.getJsonNode("o[vl]:a[vi]:o[0]:o[ul]:a[ui]:o[1]:i[vt]");
        List<Object> dt_list = jsonSelector.getJsonNode("o[vl]:a[vi]:o[0]:o[ul]:a[ui]:o[*]");

        System.out.println(ip + "\n" + tm + "\n" + fvkey);
        System.out.println(ui2Url);
        System.out.println(vt1 + "");

        System.out.println(dt_list.size() + ": " + dt_list);
//		System.out.println(jsonSelector.getJsonNode("o[vl]:a[vi]:o[0]:o[ul]:a[ui]:o[1]:i[vt]:s[c]"));
    }
}
