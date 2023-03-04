package com.iiaim.mapper;

import com.iiaim.pojo.ClickReaction;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 陈杰炫
 */
public interface ClickReactionMapper {
    //add single
    void addReaction(ClickReaction clickReaction);

    //query reaction refer to pageSize
    List<ClickReaction> allReaction(@Param("begin") int begin, @Param("size") int size);

    @Select("select count(*) from reaction")
    Long findTotal();

    //query data from reaction under some limitations
    List<ClickReaction> selectReaction(ClickReaction reaction);
}
