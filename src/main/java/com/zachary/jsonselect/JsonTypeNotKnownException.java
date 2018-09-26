package com.zachary.jsonselect;

/**
 * JsonTypeNotKnownException
 *
 * @author zachary
 */
public class JsonTypeNotKnownException extends Exception {

    private static final long serialVersionUID = -7506753703808851045L;

    public JsonTypeNotKnownException(String msg) {
        super(msg);
    }
}
