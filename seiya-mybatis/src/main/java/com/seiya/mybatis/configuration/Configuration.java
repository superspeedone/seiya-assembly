package com.seiya.mybatis.configuration;

import com.seiya.mybatis.binding.MapperProxy;
import com.seiya.mybatis.binding.MapperRegistry;
import com.seiya.mybatis.session.SqlSession;

import java.lang.reflect.Proxy;

public class Configuration {

    private MapperRegistry mapperRegistry = new MapperRegistry();

    public <T> T getMapper(SqlSession sqlSession, Class clazz) {
        return (T) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[] {clazz},
                new MapperProxy(sqlSession));
    }

    public MapperRegistry getMapperRegistry() {
        return mapperRegistry;
    }
}




