package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("sys_img")
@Data
public class Img {
    @TableId(type = IdType.AUTO,value = "id")
    private Integer id;
    private String pid;
    private String sid;
    private String img;
    private String result;
    private String state;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private String createTime;
}
