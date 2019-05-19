package single;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author wangchong
 * @date 2019/5/15 14:58
 * @email 876459397@qq.com
 * @CSDN https://blog.csdn.net/wfcn_zyq
 * @describe
 */
public class SingleThreadServer {
    public static void main(String[] args) throws IOException {
        //创建服务端Socket,端口号为6666
        ServerSocket serverSocket = new ServerSocket(6666);
        try {
            System.out.println("等待客户端连接...");
            //等待客户端连接,有客户端连接后返回客户端的Socket对象,否则线程将一直阻塞在此
            Socket client = serverSocket.accept();
            System.out.println("有新的客户端连接,端口号为:" + client.getPort());
            /*
             * 获取客户端的输入输出流
             *
             * 这是服务端,所以客户端的输入输出流看作是
             * input:系统向客户端输入
             * out:服务端向客户端输出
             */
            Scanner clientInput = new Scanner(client.getInputStream());
            clientInput.useDelimiter("\n");
            PrintStream clientOut = new PrintStream(client.getOutputStream(),true,"UTF-8");
            //读取客户端输入
            if (clientInput.hasNext()) {
                System.out.println(client.getInetAddress() + "说: " + clientInput.next());
            }
            //向客户端输出
            clientOut.println("hello I am Server!");
            //关闭输入输出流
            clientInput.close();
            clientOut.close();
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("服务端通信出现异常,错误为: " + e);
        }
    }
}
