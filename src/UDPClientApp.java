import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class UDPClientApp {

    private static final int CLIENT_PORT = 5000;
    private static final int SERVER_PORT = 5001;
    private static final int INTEGER_NUM_QUANTITY = 1;
    private static final int LONG_NUM_QUANTITY = 6;
    private static final byte CHECK_SUM_BYTE = 100;
    private static final int DATA_BYTE_LENGTH = 2 * Integer.BYTES + INTEGER_NUM_QUANTITY * Integer.BYTES +
            LONG_NUM_QUANTITY * Long.BYTES; // Length without checksum byte

    public static void main(String[] args) throws IOException {
        Scanner consoleScanner = new Scanner(System.in);
        InetAddress ip = InetAddress.getLocalHost();
        DatagramSocket clientSocket = new DatagramSocket(CLIENT_PORT, ip);
        byte data[] = null;
        ByteBuffer byteBuffer = ByteBuffer.allocate(DATA_BYTE_LENGTH);
        byteBuffer.putInt(INTEGER_NUM_QUANTITY);
        byteBuffer.putInt(LONG_NUM_QUANTITY);
        int tempInt;
        int i = 0;
        System.out.println(String.format("Entering %d integer numbers...", INTEGER_NUM_QUANTITY));
        for (; i < INTEGER_NUM_QUANTITY; i++) {
            System.out.println(String.format("Please enter #%d integer number.", i + 1));
            tempInt = consoleScanner.nextInt();
            byteBuffer.putInt(tempInt);
        }
        long tempLong;
        System.out.println(String.format("Entering %d long numbers...", LONG_NUM_QUANTITY));
        for (; i < LONG_NUM_QUANTITY + INTEGER_NUM_QUANTITY; i++) {
            System.out.println(String.format("Please enter #%d long number.", i + 1 - INTEGER_NUM_QUANTITY));
            tempLong = consoleScanner.nextLong();
            byteBuffer.putLong(tempLong);
        }
        data = byteBuffer.array();
        byte[] dataWithCheckSum = new byte[data.length + 1];
        for (i = 0; i < DATA_BYTE_LENGTH; i++) {
            dataWithCheckSum[i + 1] = data[i];
        }
        dataWithCheckSum[0] = CHECK_SUM_BYTE; // checksum byte is transferred when all another steps have been successfully done
        System.out.println("Sending packets...");
        DatagramPacket sentPacket = new DatagramPacket(dataWithCheckSum, dataWithCheckSum.length, ip, SERVER_PORT);
        int packetsSentCounter = 0;
        long startTime = 0;
        long packetSendingTime = 0;
        while (true) {
            startTime = System.currentTimeMillis();
            clientSocket.send(sentPacket);
            packetSendingTime = System.currentTimeMillis() - startTime;
            packetsSentCounter++;
            System.out.println(String.format("%d packets was sent.", packetsSentCounter));
            System.out.println(String.format("Time needed for sending packet: %d", packetSendingTime));
        }
    }

}
