package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Specialist;
import com.example.demo.mapper.SpecialistMapper;
import com.example.demo.service.SpecialistService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SpecialistServiceImpl implements SpecialistService {

    @Resource
    private SpecialistMapper specialistMapper;


    @Override
    public Boolean save(Specialist specialist) {
        return specialistMapper.insert(specialist) > 0;
    }

    @Override
    public int update(Specialist specialist) {
        return specialistMapper.updateById(specialist);
    }

    @Override
    public int deleteById(Integer id) {
        return specialistMapper.deleteById(id);
    }

    @Override
    public Page<Specialist> findPage(Integer pageNum, Integer pageSize, String search) {
        return specialistMapper.selectPage(new Page<>(pageNum,pageSize), Wrappers.<Specialist>lambdaQuery().like(Specialist::getNickName,search));
    }

    @Override
    public Specialist findOneByNameAndPWD(Specialist specialist) {
        Specialist res = specialistMapper.selectOne(Wrappers.<Specialist>lambdaQuery().eq(Specialist::getUsername,specialist.getUsername()).eq(Specialist::getPassword,specialist.getPassword()));
        return res;
    }

    @Override
    public Specialist findOneByName(Specialist specialist) {
        Specialist res = specialistMapper.selectOne(Wrappers.<Specialist>lambdaQuery().eq(Specialist::getUsername,specialist.getUsername()));
        return res;
    }

    @Override
    public List<Specialist> getAll() {
        return specialistMapper.selectList(null);
    }


}
