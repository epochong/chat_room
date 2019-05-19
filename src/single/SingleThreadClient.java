package single;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author wangchong
 * @date 2019/5/10 19:22
 * @email 876459397@qq.com
 * @CSDN https://blog.csdn.net/wfcn_zyq
 * @describe 单线程版聊天室客户端
 */

public class SingleThreadClient {
    public static void main(String[] args) throws IOException {
        String serverName = "127.0.0.1";
        Integer port = 6666;
        try {
            //创建客户端Socket连接服务器
            Socket client = new Socket(serverName,port);
            System.out.println("连接服务器,服务器地址为:" + client.getInetAddress());
            PrintStream out = new PrintStream(client.getOutputStream(),true,"UTF-8");
            Scanner in = new Scanner(client.getInputStream());
            in.useDelimiter("\n");
            //向服务器端输出内容
            out.println("Hi I am Client");
            //客户端读取服务器输入
            if (in.hasNext()) {
                System.out.println("服务器发送消息为:" + in.next());
            }
            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            System.err.println("客户端通信异常,错误为" + e);
        }


    }
}
