package com.zachary.jsonselect.util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * html util
 *
 * @author zachary bai
 */
public class HtmlUtil {

    private static final Pattern PATTERN_NUM = Pattern.compile("\\\\x(\\d+)");
    private static final Pattern PATTERN_LINE_BREAK = Pattern.compile("\\n");
    private static final Pattern PATTERN_LINE_START = Pattern.compile("\\r");
    private static final Pattern PATTERN_BLANK_CHARS = Pattern.compile("\\s+");

    /**
     * 把&#34；&nbsp类似的unicode和html tag转义成标准字符
     *
     * @param str
     * @return
     */
    public static String decodeUnicode(String str) {
        return StringEscapeUtils.unescapeHtml4(str);
    }

    /**
     * 转义\x26类似的16进制的成ascii字符
     *
     * @param str
     * @return
     */
    public static String decodeHex(String str) {

        Matcher m = PATTERN_NUM.matcher(str);
        String result = str;
        while (m.find()) {
            String code = m.group(1);
            String htmlChar = m.group();
            char c = (char) Integer.parseInt(code, 16);
            result = result.replace(htmlChar, String.valueOf(c));
        }
        return result;
    }

    /**
     * 去掉文本的换行符
     *
     * @param source
     * @return
     */
    public final static String getHtmlWithoutNR(String source) {
        if (source == null) {
            return null;
        }
        String reStr = "";
        Matcher matcher = PATTERN_LINE_BREAK.matcher(source);
        if (matcher != null) {
            reStr = matcher.replaceAll("");
            Matcher matcher2 = PATTERN_LINE_START.matcher(reStr);
            if (matcher2 != null) {
                reStr = matcher2.replaceAll("");
            }
        }
        return reStr;
    }


    /**
     * 去掉文本的blank字符：空格、制表符、换行符、行起始符
     *
     * @param str
     * @return
     */
    public final static String replaceBlank(String str) {
        if (str == null) {
            return null;
        }
        String dest = "";
        if (str != null) {
            Matcher m = PATTERN_BLANK_CHARS.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    /**
     * 去除UTF-8 BOM行首的BOM(Byte Order Mark)
     *
     * @param fileStr
     * @return
     */
    public final static String rmBomOfFileStart(String fileStr) {
        if (fileStr.startsWith("\uFEFF")) {
            fileStr = fileStr.replace("\uFEFF", "");
        }
        return fileStr;
    }

}
