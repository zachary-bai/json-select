package com.zachary.jsonselect;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class JsonTypeTest {

    @Test
    public void testGetJsonType() {
        Assert.assertEquals(JsonType.getTypeByIndex('d').getClazz(), Date.class);
    }

}
