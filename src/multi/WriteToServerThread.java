package multi;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author wangchong
 * @date 2019/5/15 16:07
 * @email 876459397@qq.com
 * @CSDN https://blog.csdn.net/wfcn_zyq
 * @describe
 */
public class WriteToServerThread implements Runnable {
    private Socket client;

    public WriteToServerThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        try {
            PrintStream out = new PrintStream(client.getOutputStream());
            while (true) {
                System.out.println("请输出要发送的信息...");
                String strToServer;
                if (scanner.hasNextLine()) {
                    strToServer = scanner.nextLine().trim();
                    out.println(strToServer);
                    if (strToServer.equals("byebye")) {
                        System.out.println("关闭客户端");
                        scanner.close();
                        out.close();
                        client.close();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("客户端写线程异常,错误为" + e);
        }
    }
}
