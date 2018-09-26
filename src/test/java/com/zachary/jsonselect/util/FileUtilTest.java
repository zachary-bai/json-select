package com.zachary.jsonselect.util;

import org.junit.Test;

import java.io.File;

public class FileUtilTest {

    @Test
    public void testWriteFile() {
        File file = new File("/data/test/text-file.txt");
        FileUtil.writeFile(file, "test content", false);
    }
}
