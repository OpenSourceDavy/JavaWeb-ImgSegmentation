package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Specialist;

import java.util.List;


public interface SpecialistService {
    Boolean save(Specialist specialist);

    int update(Specialist specialist);

    int deleteById(Integer id);

    Page<Specialist> findPage(Integer pageNum, Integer pageSize, String search);

    Specialist findOneByNameAndPWD(Specialist specialist);

    Specialist findOneByName(Specialist specialist);

    List<Specialist> getAll();

}
