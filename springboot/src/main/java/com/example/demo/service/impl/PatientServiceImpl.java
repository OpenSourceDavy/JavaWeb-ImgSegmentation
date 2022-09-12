package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Patient;
import com.example.demo.mapper.PatientMapper;
import com.example.demo.service.PatientService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    @Resource
    private PatientMapper patientMapper;


    @Override
    public Boolean save(Patient patient) {
        return patientMapper.insert(patient) > 0;
    }

    @Override
    public int update(Patient patient) {
        return patientMapper.updateById(patient);
    }

    @Override
    public int deleteById(Integer id) {
        return patientMapper.deleteById(id);
    }

    @Override
    public Page<Patient> findPage(Integer pageNum, Integer pageSize, String search) {
        return patientMapper.selectPage(new Page<>(pageNum,pageSize), Wrappers.<Patient>lambdaQuery().like(Patient::getNickName,search));
    }

    @Override
    public Patient findOneByNameAndPWD(Patient patient) {
        Patient res = patientMapper.selectOne(Wrappers.<Patient>lambdaQuery().eq(Patient::getUsername,patient.getUsername()).eq(Patient::getPassword,patient.getPassword()));
        return res;
    }

    @Override
    public Patient findOneByName(Patient patient) {
        Patient res = patientMapper.selectOne(Wrappers.<Patient>lambdaQuery().eq(Patient::getUsername,patient.getUsername()));
        return res;
    }

    @Override
    public List<Patient> getAll() {
        return patientMapper.selectList(null);
    }


}
