package multi;

import java.io.IOException;
import java.net.Socket;

/**
 * @author wangchong
 * @date 2019/5/15 16:04
 * @email 876459397@qq.com
 * @CSDN https://blog.csdn.net/wfcn_zyq
 * @describe
 */
public class MultiThreadClient {
    public static void main(String[] args) throws IOException {
        Socket client = new Socket("127.0.0.1",6666);
        Thread readFromServer = new Thread(new ReadFromServerThread(client));
        Thread writeToServer = new Thread(new WriteToServerThread(client));
        readFromServer.start();
        writeToServer.start();
    }
}
