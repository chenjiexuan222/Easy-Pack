package com.iiaim.mapper;

import com.iiaim.pojo.Block;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 陈杰炫
 */
public interface BlockMapper {
    //add
    void addBlock(Block block);

    //update
    void updateBlock(Block block);

    //delete in batch
    void deleteBlocksByIds(@Param("ids") int[] ids);

    //select all data from block
    @Select("select * from block")
    List<Block> selectAllBlocks();

    //query data from block under some limitations
    List<Block> selectBlocks(Block block);

    List<Block> selectByPage(@Param("begin") int begin, @Param("size") int size);

    @Select("select count(*) from block")
    Long findTotal();

    //get coordinate from block with blockName
    Block getCoordinate(@Param("blockName") String substrate);

    Block verify(Block block);
}
