package com.seiya.mybatis.test;

import com.seiya.mybatis.configuration.Configuration;
import com.seiya.mybatis.executor.SimpleExecutor;
import com.seiya.mybatis.session.DefaultSqlSession;
import com.seiya.mybatis.session.SqlSession;

public class Test {

    public static void main(String[] args) {
         SqlSession sqlSession = new DefaultSqlSession(new Configuration(), new SimpleExecutor());
         UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
         User user = userMapper.selectByPrimaryKey(Long.valueOf("1"));
         System.out.println(user.toString());
    }

}
