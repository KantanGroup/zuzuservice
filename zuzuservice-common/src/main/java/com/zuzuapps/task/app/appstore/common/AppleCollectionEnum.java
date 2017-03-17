package com.zuzuapps.task.app.appstore.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tuanta17
 */
public enum AppleCollectionEnum {
    TOP_MAC("topmacapps"),
    TOP_FREE_MAC("topfreemacapps"),
    TOP_GROSSING_MAC("topgrossingmacapps"),
    TOP_PAID_MAC("toppaidmacapps"),
    NEW_IOS("newapplications"),
    NEW_FREE_IOS("newfreeapplications"),
    NEW_PAID_IOS("newpaidapplications"),
    TOP_FREE_IOS("topfreeapplications"),
    TOP_FREE_IPAD("topfreeipadapplications"),
    TOP_GROSSING_IOS("topgrossingapplications"),
    TOP_GROSSING_IPAD("topgrossingipadapplications"),
    TOP_PAID_IOS("toppaidapplications"),
    TOP_PAID_IPAD("toppaidipadapplications");

    private String collection;

    AppleCollectionEnum(String c) {
        this.collection = c;
    }

    public String getCollection() {
        return collection;
    }

    private static final Map<String, AppleCollectionEnum> map;

    static {
        map = new HashMap<String, AppleCollectionEnum>();
        for (AppleCollectionEnum v : AppleCollectionEnum.values()) {
            map.put(v.getCollection(), v);
        }
    }

    public static AppleCollectionEnum findByKey(String id) {
        return map.get(id);
    }
}
