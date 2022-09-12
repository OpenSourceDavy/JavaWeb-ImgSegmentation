package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Specialist;
import com.example.demo.service.PatientService;
import com.example.demo.service.SpecialistService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private PatientService patientService;

    @Resource
    private SpecialistService specialistService;

    @GetMapping("/get")
    public List<?> getAll(@RequestParam int role){
        if(role == 1){
            return patientService.getAll();
        }else {
            return specialistService.getAll();
        }
    }

    @PutMapping("/patient")
    public Result<?> updateById(@RequestBody Patient patient){
        patientService.update(patient);
        return  Result.success();
    }

    @DeleteMapping
    public Result<?> deleteById(@RequestParam Integer id,@RequestParam int role){
        if(role == 1){
            patientService.deleteById(id);
        }else {
            specialistService.deleteById(id);
        }
        return  Result.success();
    }

    @PostMapping("/patient/login")
    public Result<?> login(@RequestBody Patient patient){
        Patient res = patientService.findOneByNameAndPWD(patient);
        if(res == null){
            return Result.error("-1","用户名或密码错误");
        }
        return  Result.success(res);
    }

    @PostMapping("/patient/register")
    public Result<?> register(@RequestBody Patient patient){
        Patient res = patientService.findOneByName(patient);
        if(res != null){
            return Result.error("-1","用户名已重复");
        }
        patientService.save(patient);
        return  Result.success();
    }

    @PostMapping("/patient")
    public Result<?> save(@RequestBody Patient patient){
        patientService.save(patient);
        return  Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "") int role){
        if(role == 1){
            Page<Patient> patientPage = patientService.findPage(pageNum,pageSize, search);
            return  Result.success(patientPage);
        }else{
            Page<Specialist> patientPage = specialistService.findPage(pageNum,pageSize, search);
            return  Result.success(patientPage);
        }
    }

    @PutMapping("/specialist")
    public Result<?> updateById(@RequestBody Specialist specialist){
        specialistService.update(specialist);
        return  Result.success();
    }


    @PostMapping("/specialist/login")
    public Result<?> login(@RequestBody Specialist specialist){
        Specialist res = specialistService.findOneByNameAndPWD(specialist);
        if(res == null){
            return Result.error("-1","用户名或密码错误");
        }
        return  Result.success(res);
    }

    @PostMapping("/specialist/register")
    public Result<?> register(@RequestBody Specialist specialist){
        Specialist res = specialistService.findOneByName(specialist);
        if(res != null){
            return Result.error("-1","用户名已重复");
        }
        specialistService.save(specialist);
        return  Result.success();
    }

    @PostMapping("/specialist")
    public Result<?> save(@RequestBody Specialist specialist){
        specialistService.save(specialist);
        return  Result.success();
    }


}
