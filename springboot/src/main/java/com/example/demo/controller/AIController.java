package com.example.demo.controller;

import cn.hutool.core.io.FileUtil;
import com.example.demo.common.Result;
import com.example.demo.entity.Img;
import com.example.demo.service.AIService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/AI")
public class AIController {

    @Resource
    private AIService aiService;

    @PutMapping
    public Result<?> pythonTriggerByUrl(@RequestBody String flag){
        String basePath = System.getProperty("user.dir") + "/springboot/src/main/resources/files/";  // 定于文件上传的根路径
        List<String> fileNames = FileUtil.listFileNames(basePath);  // 获取所有的文件名称
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");  // 找到跟参数一致的文件
        basePath = basePath.replaceAll("\\\\", "/");
        String imgUrl = basePath+fileName;
        String url =  aiService.pythonTrigger(imgUrl);
        url = url.replaceAll("\\\\", "/");
        return  Result.success(url);
    }

}
