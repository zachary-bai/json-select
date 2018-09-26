package com.zachary.jsonselect;

/**
 * SelectorIllegalException
 * selector must be like o[XXX],a[XXX],s[XXX],o[*],b[1] or join them with (:),
 * example: o[vl]:a[vi]:o[*]:s[url] or o[data] or l[width]
 *
 * @author zachary
 */
public class SelectorIllegalException extends Exception {

    private static final long serialVersionUID = 7026994323861254985L;

    public SelectorIllegalException(String msg) {
        super(msg);
    }

}
