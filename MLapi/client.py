#!/usr/bin/python
# -*- coding: UTF-8 -*-
# 文件名：tcpclient.py
from PIL import Image
import socket
import time
import os

MaxBytes = 1024 * 1024
host = 'localhost'
port = 9786
client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

client.connect((host, port))
while True:
    inputData = input();  # 等待输入数据
    if (inputData == "quit"):
        print("我要退出了，再见")
        break
    sendBytes = client.send(inputData.encode())
    if sendBytes <= 0:
        break;
    recvData = client.recv(MaxBytes)
    if not recvData:
        print('接收数据为空，我要退出了')
        break
    localTime = time.asctime(time.localtime(time.time()))
    print(localTime, ' 接收到数据字节数:', len(recvData))
    print(recvData.decode())
    img = Image.open(recvData.decode())
    img.show()
client.close()
print("我已经退出了，后会无期")
