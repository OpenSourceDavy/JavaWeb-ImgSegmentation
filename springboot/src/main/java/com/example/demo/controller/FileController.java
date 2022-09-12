package com.example.demo.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.example.demo.common.Result;
import com.example.demo.entity.Img;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${server.port}")
    private String port;

    //@Value("175.24.204.121") //云服务器配置IP
    @Value("localhost")
    private String ip;

    //上传
    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file) throws IOException {
        //获取文件名
        String originalFilename = file.getOriginalFilename();
        //定义文件唯一标识
        String flag = IdUtil.fastSimpleUUID();
        //获取上传的路径
        String rootFilePath = System.getProperty("user.dir") + "/springboot/src/main/resources/files/" + flag + "_" + originalFilename;
        //把文件写入上传的路径
        FileUtil.writeBytes(file.getBytes(),rootFilePath);
        return Result.success("http://"+ip+":"+port+"/files/"+flag);
    }

    //显示
    @GetMapping("{flag}")
    public void getFiles(@PathVariable String flag, HttpServletResponse response) {
        OutputStream os;  // 新建一个输出流对象
        String basePath = System.getProperty("user.dir") + "/springboot/src/main/resources/files/";  // 定于文件上传的根路径
        List<String> fileNames = FileUtil.listFileNames(basePath);  // 获取所有的文件名称
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");  // 找到跟参数一致的文件
        try {
            if (StrUtil.isNotEmpty(fileName)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(basePath + fileName);  // 通过文件的路径读取文件字节流
                os = response.getOutputStream();   // 通过输出流返回文件
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }
    }

    //显示
    @GetMapping("/AI/{flag}")
    public void getAIFiles(@PathVariable String flag, HttpServletResponse response) {
        OutputStream os;  // 新建一个输出流对象
        String basePath = "C:/Users/davy3/crm_vue1/MLapi/experiments/UNet_vessel_seg/result_img/";  // 定于文件上传的根路径
        List<String> fileNames = FileUtil.listFileNames(basePath);  // 获取所有的文件名称
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");  // 找到跟参数一致的文件
        try {
            if (StrUtil.isNotEmpty(fileName)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(basePath + fileName);  // 通过文件的路径读取文件字节流
                os = response.getOutputStream();   // 通过输出流返回文件
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }
    }

    //整合 integration
    @PostMapping("/integration")
    public Result<?> integration(@RequestBody Img img, HttpServletResponse response) {
        OutputStream os;  // 新建一个输出流对象
        String basePath = System.getProperty("user.dir") + "/springboot/src/main/resources/files/";  // 定于文件上传的根路径
        List<String> fileNames = FileUtil.listFileNames(basePath);  // 获取所有的文件名称
        String[] imgs = img.getImg().split("/");
        String flag = imgs[imgs.length-1];
        String result = img.getResult();
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");  // 找到跟参数一致的文件
        //获得一个新的标识
        String newFlag = IdUtil.fastSimpleUUID();
        try {
            if (StrUtil.isNotEmpty(fileName) && StrUtil.isNotEmpty(result)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                BufferedImage background = resizeImage(1500,2111, ImageIO.read(new File(basePath + fileName)));
                String[] split = StrUtil.cut(result, 25);
                Graphics2D g = background.createGraphics();
                g.setColor(Color.white);
                g.setFont(new Font("微软雅黑",Font.BOLD,50));
                //这里将信息和信息对应的位置写入
                for(int i = 0 ; i<split.length ; i++){
                    g.drawString(split[i],20,1000+(i*50));
                }
                g.dispose();
                ImageIO.write(background, "jpg", new File(basePath + newFlag + "_" + fileName));
            }
        } catch (Exception e) {
            System.out.println("文件整合失败"+e.getMessage());
        }
        return Result.success(newFlag);
    }

    public static BufferedImage resizeImage(int x,int y,BufferedImage bfi){
        BufferedImage bufferedImage = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(
                bfi.getScaledInstance(x, y, Image.SCALE_SMOOTH), 0, 0, null);
        return bufferedImage;
    }

}
