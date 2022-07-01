import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class TCPClientApp {

    private static int INTEGER_NUM_QUANTITY = 1;
    private static int LONG_NUM_QUANTITY = 6;

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        long executionTime = 0;
        long connectionTime;
        long packetSendingTime;
        try(Socket socket = new Socket("127.0.0.1", 4999);
        Scanner consoleScanner = new Scanner(System.in);
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {
            connectionTime = System.currentTimeMillis() - startTime;
            executionTime += connectionTime;
            System.out.println("Please enter the quantity of integer numbers.");
            INTEGER_NUM_QUANTITY = consoleScanner.nextInt();
            startTime = System.currentTimeMillis();
            outputStream.writeInt(INTEGER_NUM_QUANTITY);
            outputStream.flush();
            while (!inputStream.readBoolean());
            executionTime += System.currentTimeMillis() - startTime;
            System.out.println("Please enter the quantity of long numbers.");
            LONG_NUM_QUANTITY = consoleScanner.nextInt();
            startTime = System.currentTimeMillis();
            outputStream.writeInt(LONG_NUM_QUANTITY);
            outputStream.flush();
            while (!inputStream.readBoolean());
            packetSendingTime = System.currentTimeMillis() - startTime;
            executionTime += packetSendingTime;
            int tempInt;
            int i = 0;
            int numbersSentCounter = -1;
            System.out.println("Entering integer numbers...");
            for (; i < INTEGER_NUM_QUANTITY; i++) {
                System.out.println(String.format("Please enter #%d integer number.", i + 1));
                tempInt = consoleScanner.nextInt();
                startTime = System.currentTimeMillis();
                outputStream.writeInt(tempInt);
                outputStream.flush();
                while ((numbersSentCounter = inputStream.readInt()) != i) ;
                executionTime += System.currentTimeMillis() - startTime;
            }
            long tempLong;
            System.out.println("Entering long numbers...");
            for (; i < INTEGER_NUM_QUANTITY + LONG_NUM_QUANTITY; i++) {
                System.out.println(String.format("Please enter #%d long number.", i + 1 - INTEGER_NUM_QUANTITY));
                tempLong = consoleScanner.nextLong();
                startTime = System.currentTimeMillis();
                outputStream.writeLong(tempLong);
                outputStream.flush();
                while ((numbersSentCounter = inputStream.readInt()) != i);
                executionTime += System.currentTimeMillis() - startTime;
            }
            System.out.println("All numbers were successfully sent!");
            System.out.println(String.format("Execution time: %d ms", executionTime));
            System.out.println(String.format("Connection time: %d ms", connectionTime));
            System.out.println(String.format("Packet sending time: %d ms", packetSendingTime));
        }
    }

}
