package com.zachary.jsonselect.util;

import org.junit.Test;

import java.io.IOException;

public class HtmlUtilTest {

    @Test
    public void testWashHtml() throws IOException {
        System.out.println(HtmlUtil.decodeUnicode("百度在&#34;大会&#34;&nbsp;"));
        System.out.println(HtmlUtil.decodeHex("百度在\\x26大会&#34;&nbsp;"));
//        String fileStr = FileUtil.getContent("/home/zachary/Desktop/rss.txt");
//        System.out.println(fileStr.length() + " " + HtmlUtil.rmBomOfFileStart(fileStr).length());
    }
}
