import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        System.out.println("Server Start…\n");
        Server server = new Server();
        server.init();
    }

    public void init() {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {

                Socket client = serverSocket.accept();

                new HandlerThread(client);
            }
        } catch (Exception e) {
            System.out.println("Server Wrong: " + e.getMessage());
        }
    }

    private class HandlerThread implements Runnable {
        private Socket socket;

        public HandlerThread(Socket client) {
            socket = client;
            new Thread(this).start();
        }

        public void run() {
            try {

                DataInputStream input = new DataInputStream(socket.getInputStream());
                String clientInputStr = input.readUTF();

                System.out.println("From Client:" + clientInputStr);

                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                System.out.print("Input:\t");

                String s = new BufferedReader(new InputStreamReader(System.in)).readLine();
                out.writeUTF(s);
                out.close();
                input.close();
            } catch (Exception e) {
                    System.out.println("Server run wrong: " + e.getMessage());
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Exception e) {
                        socket = null;
                        System.out.println("Server finally wrong:" + e.getMessage());
                    }
                }
            }
        }
    }
}