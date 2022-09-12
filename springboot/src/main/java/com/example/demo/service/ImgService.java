package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Img;


public interface ImgService {
    Boolean save(Img img);

    int update(Img img);

    int deleteById(Integer id);



    Page<Img> findPage(Integer pageNum, Integer pageSize, String search, String sid, String pid);



}
