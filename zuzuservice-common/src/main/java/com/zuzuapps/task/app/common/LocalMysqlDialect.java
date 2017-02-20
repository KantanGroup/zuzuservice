package com.zuzuapps.task.app.common;

import org.hibernate.dialect.MySQL5Dialect;

/**
 * @author tuanta17
 */
public class LocalMysqlDialect extends MySQL5Dialect {
    @Override
    public String getTableTypeString() {
        return " DEFAULT CHARSET=utf8";
    }
}
