package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Patient;

import java.util.List;


public interface PatientService {
    Boolean save(Patient patient);

    int update(Patient patient);

    int deleteById(Integer id);

    Page<Patient> findPage(Integer pageNum, Integer pageSize, String search);

    Patient findOneByNameAndPWD(Patient patient);

    Patient findOneByName(Patient patient);

    List<Patient> getAll();

}
