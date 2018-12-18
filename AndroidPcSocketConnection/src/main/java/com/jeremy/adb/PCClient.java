package com.jeremy.adb;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

//右键点击该类，可以选择运行
//step 1  adb forward tcp:8000 tcp:9000
//step 2  run app
//step 3  run PCClient

public class PCClient {

    public static void main(String[] args) throws IOException {
        System.out.println("任意字符, 回车键发送Toast");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String msg = scanner.next();
            sendToast(msg);
        }
    }

    public static void sendToast(String msg) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8000);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(msg);
        socket.close();
    }
}
