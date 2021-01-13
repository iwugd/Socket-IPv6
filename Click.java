import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Enumeration;

public class Click {
    public static void main(String[] args) throws IOException {

        String servernameString = getLocalIPv6Address();
        System.out.println("Client Starts");
        System.out.println("if received \"OK\", client stop\n");
        while (true) {
            Socket socket = null;
            try {

                socket = new Socket(servernameString, 8080);

                DataInputStream input = new DataInputStream(socket.getInputStream());

                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                System.out.print("input: \t");
                String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
                out.writeUTF(str);
                String ret = input.readUTF();
                System.out.println("from server: " + ret);
                if ("OK".equals(ret)) {
                    System.out.println("client close");
                    Thread.sleep(500);
                    break;
                }
                out.close();
                input.close();
            } catch (Exception e) {
                System.out.println("client wrong:" + e.getMessage());
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        socket = null;
                        System.out.println("client finally wrong:" + e.getMessage());
                    }
                }
            }
        }
    }
    public static String getLocalIPv6Address() throws IOException {
        InetAddress inetAddress = null;
        Enumeration<NetworkInterface> networkInterfaces =
        NetworkInterface
        .getNetworkInterfaces();
        outer:
        while (networkInterfaces.hasMoreElements()) {
            Enumeration<InetAddress> inetAds =
            networkInterfaces.nextElement()
            .getInetAddresses();
            while (inetAds.hasMoreElements()) {
                inetAddress = inetAds.nextElement();
        //Check if it's ipv6 address and reserved address
                if (inetAddress instanceof Inet6Address
                    && !isReservedAddr(inetAddress)) {
                    break outer;
                }
            }
        }
        String ipAddr = inetAddress.getHostAddress();
        // Filter network card No
        int index = ipAddr.indexOf('%');
        if (index > 0) {
            ipAddr = ipAddr.substring(0, index);
        }
        return ipAddr;
    }

    private static boolean isReservedAddr(InetAddress inetAddr) {
        if (inetAddr.isAnyLocalAddress() || inetAddr.isLinkLocalAddress()|| inetAddr.isLoopbackAddress()) {
            return true;
            }        
        return false;
    }
}