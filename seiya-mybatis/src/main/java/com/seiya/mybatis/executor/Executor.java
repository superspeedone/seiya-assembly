package com.seiya.mybatis.executor;

public interface Executor {

    public <T> T query(String statement, String parameter, Class clazz);

}
