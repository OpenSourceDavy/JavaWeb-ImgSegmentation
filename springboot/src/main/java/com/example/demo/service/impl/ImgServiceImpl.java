package com.example.demo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Img;
import com.example.demo.mapper.ImgMapper;
import com.example.demo.service.ImgService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ImgServiceImpl implements ImgService {

    @Resource
    private ImgMapper imgMapper;


    @Override
    public Boolean save(Img img) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        img.setCreateTime(date);
        return imgMapper.insert(img) > 0;
    }

    @Override
    public int update(Img img) {
        return imgMapper.updateById(img);
    }

    @Override
    public int deleteById(Integer id) {
        return imgMapper.deleteById(id);
    }

    @Override
    public Page<Img> findPage(Integer pageNum, Integer pageSize, String search, String sid, String pid) {
        if(StrUtil.isNotEmpty(sid)){
            //专家
            return imgMapper.selectPage(new Page<>(pageNum,pageSize), Wrappers.<Img>lambdaQuery().like(Img::getState,search).eq(Img::getSid,sid));
        }else if (StrUtil.isNotEmpty(pid)){
            //病人
            return imgMapper.selectPage(new Page<>(pageNum,pageSize), Wrappers.<Img>lambdaQuery().like(Img::getState,search).eq(Img::getPid,pid));
        }else{
            //管理员
            return imgMapper.selectPage(new Page<>(pageNum,pageSize), Wrappers.<Img>lambdaQuery().like(Img::getState,search));
        }
    }


}
