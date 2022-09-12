import socket
import win32api
import threading
from test import finalTest
from imgPreProcess import imgToTxt
import requests as req



class AIServer(object):

    def __init__(self, port):





        self.tcp_server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)



        self.tcp_server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)



        self.tcp_server_socket.bind(("localhost", port))



        self.tcp_server_socket.listen(100)

    def run_forever(self):


        while True:



            new_socket, client_addr = self.tcp_server_socket.accept()

            print("设备{0}已连接".format(client_addr))



            t1 = threading.Thread(target=self.service_machine, args=(new_socket, client_addr))

            t1.start()

    def service_machine(self, new_socket, client_addr):



        while True:



            receive_url = new_socket.recv(1024)



            if receive_url:




                print(receive_url.decode())




                imgToTxt(receive_url.decode())


                respond_url = finalTest()  # 传入url

                respond_url = respond_url+'\n'
                print(respond_url)
                new_socket.send(respond_url.encode())



            else:
                    print('设备{0}断开连接...'.format(client_addr))
                    break





        new_socket.close()


def main(port):


    Ai_server = AIServer(port)

    print("服务器已开启")

    Ai_server.run_forever()


if __name__ == '__main__':
    port = 9786  # 指定端口

    main(9786)

