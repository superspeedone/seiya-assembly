package com.seiya.mybatis.session;

import com.seiya.mybatis.configuration.Configuration;

public interface SqlSession {

    public <T> T getMapper(Class clazz);

    public <T> T selectOne(String statement, String parameter, Class returnType);

    public Configuration getConfiguration();

}
