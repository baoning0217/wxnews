package com.rietergroup.wxnews.dao;

import com.rietergroup.wxnews.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {
    String TABLE_NAME = "user";
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, name, password, salt, head_url ";

    /**
     * 增加用户
     * @param user
     * @return
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUSer(User user);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Select({"select ", SELECT_FIELDS, " from", TABLE_NAME, " where id=#{id}"})
    User selectById(int id);

    /**
     * 更新用户
     * @param user
     */
    @Update({"update", TABLE_NAME, " set password = #{password} where id=#{id}"})
    void updatePassword(User user);

    /**
     * 删除用户
     * @param id
     */
    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);

    /**
     * 根据name查询
     * @param name
     * @return
     */
    @Select({"select ", SELECT_FIELDS, " from", TABLE_NAME, " where name=#{name}"})
    User selectByName(String name);



}
