package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Img;
import com.example.demo.service.ImgService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/img")
public class ImgController {

    @Resource
    private ImgService imgService;

    @PutMapping
    public Result<?> updateById(@RequestBody Img img){
        imgService.update(img);
        return  Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteById(@PathVariable Integer id){
        imgService.deleteById(id);
        return  Result.success();
    }


    @PostMapping
    public Result<?> save(@RequestBody Img img){
        imgService.save(img);
        return  Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "") String sid,
                              @RequestParam(defaultValue = "") String pid){
        Page<Img> imgPage = imgService.findPage(pageNum,pageSize, search, sid, pid);
        return  Result.success(imgPage);
    }



}

