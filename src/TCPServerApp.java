import java.net.*;
import java.io.*;

public class TCPServerApp {

    private static int INTEGER_NUM_QUANTITY;
    private static int LONG_NUM_QUANTITY;

    public static void main(String[] args) throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(4999);
        Socket connectionSocket = serverSocket.accept();
        DataInputStream inputStream = new DataInputStream(connectionSocket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(connectionSocket.getOutputStream())) {
            System.out.println(String.format("Client %s connected.", connectionSocket.toString()));
            while ((INTEGER_NUM_QUANTITY = inputStream.readInt()) <= 0) ;
            System.out.println(String.format("The number of integer numbers which are going to be accepted is %d",
                    INTEGER_NUM_QUANTITY));
            outputStream.writeBoolean(true);
            while ((LONG_NUM_QUANTITY = inputStream.readInt()) <= 0) ;
            System.out.println(String.format("The number of long numbers which are going to be accepted is %d",
                    LONG_NUM_QUANTITY));
            outputStream.writeBoolean(true);
            int tempInt;
            long tempLong;
            int i = 0;
            for (; i < INTEGER_NUM_QUANTITY; i++) {
                while ((tempInt = inputStream.readInt()) < 0) ;
                System.out.println(String.format("%d accepted.", tempInt));
                outputStream.writeInt(i);
                outputStream.flush();
            }
            for (; i < INTEGER_NUM_QUANTITY + LONG_NUM_QUANTITY; i++) {
                while ((tempLong = inputStream.readLong()) < 0) ;
                System.out.println(String.format("%d accepted.", tempLong));
                outputStream.writeInt(i);
                outputStream.flush();
            }
            System.out.println("All numbers were successfully accepted!");
        }
    }
}
