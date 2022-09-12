package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Root;
import com.example.demo.mapper.RootMapper;
import com.example.demo.service.RootService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RootServiceImpl implements RootService {

    @Resource
    private RootMapper rootMapper;


    @Override
    public Boolean save(Root root) {
        return rootMapper.insert(root) > 0;
    }

    @Override
    public int update(Root root) {
        return rootMapper.updateById(root);
    }

    @Override
    public int deleteById(Integer id) {
        return rootMapper.deleteById(id);
    }

    @Override
    public Page<Root> findPage(Integer pageNum, Integer pageSize, String search) {
        return rootMapper.selectPage(new Page<>(pageNum,pageSize), Wrappers.<Root>lambdaQuery().like(Root::getNickName,search));
    }

    @Override
    public Root findOneByNameAndPWD(Root root) {
        Root res = rootMapper.selectOne(Wrappers.<Root>lambdaQuery().eq(Root::getUsername,root.getUsername()).eq(Root::getPassword,root.getPassword()));
        return res;
    }

    @Override
    public Root findOneByName(Root root) {
        Root res = rootMapper.selectOne(Wrappers.<Root>lambdaQuery().eq(Root::getUsername,root.getUsername()));
        return res;
    }


}
