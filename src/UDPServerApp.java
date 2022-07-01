import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class UDPServerApp {

    private static final int SERVER_PORT = 5001;
    private static int INTEGER_NUM_QUANTITY;
    private static int LONG_NUM_QUANTITY;
    private static final int DATA_BYTE_LENGTH = 61;
    private static final byte CHECK_SUM_BYTE = 100;

    public static void main(String[] args) throws IOException {
        InetAddress ip = InetAddress.getLocalHost();
        DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT, ip);
        byte [] data = new byte[DATA_BYTE_LENGTH];
        DatagramPacket receivedPacket = new DatagramPacket(data, DATA_BYTE_LENGTH);
        while (true) {
            serverSocket.receive(receivedPacket);
            System.out.println("Trying to receive the correct packet...");
            if ((data = receivedPacket.getData())[0] == CHECK_SUM_BYTE) {
                System.out.println("Correct packet was received.");
                System.out.println(Arrays.toString(data));
                ByteBuffer byteBuffer = ByteBuffer.allocate(DATA_BYTE_LENGTH);
                byteBuffer.put(data);
                byteBuffer.flip();
                System.out.println("The checksum byte is " + byteBuffer.get());
                INTEGER_NUM_QUANTITY = byteBuffer.getInt();
                System.out.println(String.format("The number of integer numbers which are going to be accepted is %d",
                        INTEGER_NUM_QUANTITY));
                LONG_NUM_QUANTITY = byteBuffer.getInt();
                System.out.println(String.format("The number of long numbers which are going to be accepted is %d",
                        LONG_NUM_QUANTITY));
                int i = 0;
                for (; i < INTEGER_NUM_QUANTITY; i++) {
                    System.out.println(String.format("%d accepted.", byteBuffer.getInt()));
                }
                for (; i < INTEGER_NUM_QUANTITY + LONG_NUM_QUANTITY; i++) {
                    System.out.println(String.format("%d accepted.", byteBuffer.getLong()));
                }
                System.out.println("All numbers were successfully accepted!");
                break;
            }
        }
    }

}
