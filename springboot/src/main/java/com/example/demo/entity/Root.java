package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("sys_root")
@Data
public class Root {
    @TableId(type = IdType.AUTO,value = "id")
    private Integer id;
    private String username;
    private String password;
    private String nickName;
    private String role;
}
