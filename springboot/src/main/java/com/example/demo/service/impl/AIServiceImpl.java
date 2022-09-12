package com.example.demo.service.impl;

import com.example.demo.service.AIService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import static com.sun.deploy.uitoolkit.impl.awt.AWTClientPrintHelper.print;

@Service
public class AIServiceImpl implements AIService {

    @Override
    public String pythonTrigger(String imgUrl) {
        try {
            Socket socket = new Socket("localhost",9786);
            while (true) {
                //获取输出流，向服务器端发送信息
                OutputStream os = socket.getOutputStream();//字节输出流
                PrintWriter pw = new PrintWriter(os);//将输出流包装为打印流
                System.out.print("> ");
                Scanner instr = new Scanner(imgUrl);

                pw.write(instr.nextLine());//前端要填的参数
                pw.flush();
                System.out.println("\n");
                //socket.shutdownOutput();//关闭输出流

                InputStream is = socket.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String url = in.readLine();
                System.out.print("java接收到"+url);


//                List<List<String>> arrlist2 = new ArrayList<>();
//                for (String str : s) {
//                    List<String> arrayList = new ArrayList<>();
//                    arrayList.add(str);
//                    arrlist2.add(arrayList);
//                }
//                System.out.println(arrlist2)
                is.close();
                in.close();
                socket.close();
                return url;
            }
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "调用Python服务器失败";
        } catch (IOException e) {
            e.printStackTrace();
            return "调用Python服务器失败";
        }
    }
}
