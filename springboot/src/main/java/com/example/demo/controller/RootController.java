package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Root;
import com.example.demo.service.RootService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/root")
public class RootController {

    @Resource
    private RootService rootService;

    @PutMapping
    public Result<?> updateById(@RequestBody Root root){
        rootService.update(root);
        return  Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteById(@PathVariable Integer id){
        rootService.deleteById(id);
        return  Result.success();
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody Root root){
        Root res = rootService.findOneByNameAndPWD(root);
        if(res == null){
            return Result.error("-1","用户名或密码错误");
        }
        return  Result.success(res);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody Root root){
        Root res = rootService.findOneByName(root);
        if(res != null){
            return Result.error("-1","用户名已重复");
        }
        rootService.save(root);
        return  Result.success();
    }

    @PostMapping
    public Result<?> save(@RequestBody Root root){
        rootService.save(root);
        return  Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        Page<Root> rootPage = rootService.findPage(pageNum,pageSize, search);
        return  Result.success(rootPage);
    }
}
