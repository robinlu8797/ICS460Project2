
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Random;

public class Receiver {
    static String RECV = "RECV";
    static String DUPL = "DUPL";
    static String CRPT = "CRPT";
    static String SeqEr = "!Seq";
    static String ACK = "ACK";
    static String SENT = "SENT";
    static String ERR = "ERR";
    static String DROP = "DROP";
    static String timeout = "TimeOut";
    static String sendFirstMessage = "SENDing";

    public static void main(String[] args) throws IOException {
		//This is Socet of the server
        DatagramSocket socketOfReceiver = new DatagramSocket(4000);
		
        int sizeOfPacket = 10000; // This can be any size > sender packet size now
		//Total Bytes Received
        double totalBytes = 0;
		//A file output stream to output file
        FileOutputStream fileOutputStream = new FileOutputStream("src/a190630092823318/output.jpg");
		//Buffered output stream to get the output
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

		//Received Data 
        byte[] dataRecieved = new byte[sizeOfPacket];
		//Send Data
        byte[] datasend  = new byte[sizeOfPacket];
		//To recieve packet
        byte[] mybytearray = new byte[sizeOfPacket];
		
        DatagramPacket packetReceived = new DatagramPacket(mybytearray, mybytearray.length);

        System.out.println("Starting receiver and waiting for first packet...");
        boolean running = true;
        int i = 0;

        Random random = new Random( );
		int chance = random.nextInt( 100 );
		
        while (running) {
            try {
            	if(chance%2==0) {
					//Receive Packet in socket
	                socketOfReceiver.receive(packetReceived);
					//SetTimeout
	                socketOfReceiver.setSoTimeout(3000);
					//Binray Data
	                byte binData[] = packetReceived.getData();
	
	                binData = Arrays.copyOfRange(binData, 0, packetReceived.getLength());
	
	                totalBytes += binData.length;
	                System.out.println(RECV + " " + String.format("%d",System.currentTimeMillis()) +
	                        " " + (i + 1) + " " + RECV);
	                i += 1;
	                bufferedOutputStream.write(binData, 0, binData.length);
            	}
            	else if(chance%3==0) {
            		System.out.println("Message "+DUPL);
            	}
            	else if(chance%5==0) {
            		System.out.println("Message "+CRPT);
            	}
            	else if(chance%7==0) {
            		System.out.println("Message "+SeqEr);
            	}
            	else if(chance%10==0) {
            		System.out.println("Message "+ERR);
            	}
            	else {
            		System.out.println("Message "+DROP);
            	}
            	} catch (SocketTimeoutException s) {
                System.out.println("Transfer Finished");
                break;
            }

            System.out.println(sendFirstMessage + " " + ACK + " " + (i) + " " +
                    String.format("%d",System.currentTimeMillis()) + " " + SENT
            );
        }
    }

    private static byte[] trimBytes(byte[] bytes){
        int i = bytes.length -1;
        while(i >= 0 && bytes[i] == 0){
            --i;
        }
        return Arrays.copyOf(bytes, i + 1);
    }
}
