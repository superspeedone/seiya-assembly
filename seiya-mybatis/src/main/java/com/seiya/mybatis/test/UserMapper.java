package com.seiya.mybatis.test;

import com.seiya.mybatis.annotations.Select;

public interface UserMapper {

    @Select("select * from cms_user where user_id = %d")
    User selectByPrimaryKey(Long userId);


}
