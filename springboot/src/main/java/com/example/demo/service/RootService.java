package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Root;


public interface RootService {
    Boolean save(Root root);

    int update(Root root);

    int deleteById(Integer id);

    Page<Root> findPage(Integer pageNum, Integer pageSize, String search);

    Root findOneByNameAndPWD(Root root);

    Root findOneByName(Root root);


}
