package com.iiaim.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.iiaim.mapper.BlockMapper;
import com.iiaim.pojo.Block;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 陈杰炫
 */
@Service("blockServiceImpl")
public class BlockServiceImpl implements BlockServices{

    @Autowired
    private BlockMapper mapper;

    @Override
    public void addBlock(Block block) {
        mapper.addBlock(block);
    }

    @Override
    public void updateBlock(Block block) {
        mapper.updateBlock(block);
    }

    @Override
    public void deleteBlocksByIds(int[] ids) {
        mapper.deleteBlocksByIds(ids);
    }

    @Override
    public List<Block> selectAllBlocks() {
        return mapper.selectAllBlocks();
    }

    @Override
    public Page<Block> selectBlocks(Block block, int currentPage, int pageSize) {
        Page<Block> page = PageHelper.startPage(currentPage, pageSize,true);
        mapper.selectBlocks(block);
        return page;
    }

    @Override
    public List<Block> selectByPage(int begin, int size) {
        return mapper.selectByPage(begin, size);
    }

    @Override
    public Long findTotal() {
        return mapper.findTotal();
    }

    @Override
    public Block getCoordinate(String substrate) {
        return mapper.getCoordinate(substrate);
    }

    @Override
    public Block verify(Block block) {
        return mapper.verify(block);
    }
}
