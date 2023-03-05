package com.iiaim.service;

import com.github.pagehelper.Page;
import com.iiaim.pojo.Block;

import java.util.List;

/**
 * @author 陈杰炫
 */

public interface BlockServices {
    void addBlock(Block block);

    void updateBlock(Block block);

    void deleteBlocksByIds(int[] ids);

    List<Block> selectAllBlocks();

    Page<Block> selectBlocks(Block block, int currentPage, int pageSize);

    List<Block> selectByPage(int begin, int size);

    Long findTotal();

    Block getCoordinate(String substrate);

    Block verify(Block block);
}
