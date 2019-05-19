package multi;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author wangchong
 * @date 2019/5/15 15:33
 * @email 876459397@qq.com
 * @CSDN https://blog.csdn.net/wfcn_zyq
 * @describe
 */
public class ReadFromServerThread implements Runnable{
    private Socket client;

    public ReadFromServerThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            Scanner in = new Scanner(client.getInputStream());
            in.useDelimiter("\n");
            while (true) {
                if (in.hasNext()) {
                    System.out.println("从服务器发来的消息为:" + in.next());
                }
                if (client.isClosed()) {
                    System.out.println("客户端已经关闭");
                }
                break;
            }
            in.close();
        } catch (IOException e) {
            System.err.println("客户端度线程异常,错误为 " + e);
        }
    }



}
