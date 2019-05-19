package multi;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangchong
 * @date 2019/5/15 16:09
 * @email 876459397@qq.com
 * @CSDN https://blog.csdn.net/wfcn_zyq
 * @describe
 */
public class MultiThreadServer {
    private static Map<String,Socket> clientMap = new ConcurrentHashMap<>();
    private static class ExecuteClient implements Runnable {
        private Socket client;

        public ExecuteClient(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                Scanner in = new Scanner(client.getInputStream());
                String strFromClient;
                while (true) {
                    if (in.hasNextLine()) {
                        strFromClient = in.nextLine();
                        Pattern pattern = Pattern.compile("\r");
                        Matcher matcher = pattern.matcher(strFromClient);
                        strFromClient = matcher.replaceAll("");
                        if (strFromClient.startsWith("userName")) {
                            String userName = strFromClient.split("\\:")[1];
                            registerUser(userName,client);
                        }
                        if (strFromClient.startsWith("G")) {
                            String msg = strFromClient.split("\\:")[1];
                            groupChat(msg);
                            continue;
                        }
                        if (strFromClient.startsWith("P")) {
                            String userName = strFromClient.split("\\:")[1].split("\\:")[0];
                            String msg = strFromClient.split("\\:")[1].split("-")[1];
                            privateChat(userName,msg);
                        }
                        if (strFromClient.startsWith("byebye")) {
                            String userName = null;
                            for (String keyName : clientMap.keySet()
                                 ) {
                                if (clientMap.get(keyName).equals(client)) {
                                    userName = keyName;
                                }
                            }
                            System.out.println("用户" + userName + "下线了！");
                            clientMap.remove(userName);
                            continue;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        private static void registerUser(String userName, Socket client) throws IOException {
            System.out.println("用户姓名为:" + userName);
            System.out.println("用户" + userName + "上线了");
            System.out.println("当前群里人数为:" + (clientMap.size() + 1)  + "人");
            clientMap.put(userName,client);
            PrintStream out = new PrintStream(client.getOutputStream(),true,"UTF-8");
            out.println("用户注册成功！");
        }
        private void groupChat(String msg) throws IOException {
            Set<Map.Entry<String,Socket>> clientSet = clientMap.entrySet();
            for (Map.Entry<String,Socket> entry : clientSet) {
                Socket socket = entry.getValue();
                PrintStream out = new PrintStream(socket.getOutputStream(),true,"UTF-8");
                out.println("群聊信息为:" + msg);
            }
        }

        private void privateChat(String userName, String msg) throws IOException {
            Socket privateSocket = clientMap.get(userName);
            PrintStream out = new PrintStream(privateSocket.getOutputStream(),true,"UTF-8");
            out.println("私聊信息为:" + msg);
        }

        public static void main(String[] args) throws IOException {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            ServerSocket serverSocket = new ServerSocket(6666);
            for (int i = 0; i < 10; i++) {
                System.out.println("等待客户端连接...");
                Socket client = serverSocket.accept();
                System.out.println("有新的客户端连接,端口号为:" + client.getPort());
                executorService.submit(new ExecuteClient(client));
            }
            executorService.shutdown();
            serverSocket.close();
        }

    }
}
