package com.rietergroup.wxnews.dao;

import com.rietergroup.wxnews.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDao {

    String TABLE_NAME = "comment";
    String INSERT_FIELDS = " user_id, content, create_date, entity_id, entity_type, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /*
    *增加评论
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,") values (#{userId}, #{content}, #{createDate}, #{entityId}, #{entityType}, #{status}) "})
    int addComment(Comment comment);

    @Select({" select ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE entity_id=#{entityId} and entity_type=#{entityType} order by id desc "})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({" select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} " })
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({" update ", TABLE_NAME, " set status=#{status} where entity_id=#{entityId} and entity_type=#{entityType} "})
    void updateStatus(@Param("entityId") int entityId, @Param("entityType") int entityType);

}
