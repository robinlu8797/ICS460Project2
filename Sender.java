
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class Sender {
    static int akno=0;
    static int seqno=0;
    static byte[] sendData = new byte[1024];

    static String sendFirstMessage = "SENDing ";
    static String resendMessage = "ReSend. ";
    static String SENT = "SENT";
    static String DROP = "DROP";
    static String ERR= "ERR";
    static String ackRcvd = "AckRcvd";
    static String duplAck = "DuplAck";
    static String errAck = "ErrAck";
    static String moveWnd = "MoveWnd";
    static String timeout = "TimeOut";


    // sequence number of datagram (integer, [0,(size of file)/(size of packet)]

    public static void main(String[] args) throws IOException {
        String filePath;
        if (args.length > 0) {
            filePath = args[0];
        } else {
            filePath = "src/a190630092823318/input.jpg";
        }
		//Get file from the path
        File getFile = new File(filePath);
		//Buffered Input Stream to inout file
        BufferedInputStream bIStream = null;
		//Make a null datagram socket
        DatagramSocket dgSocket = null;
        try {
			//Initiate a datagram socket
            dgSocket = new DatagramSocket();
			//Packet Size
            int sizeOfPacket = 1024;
			//Extra Data Off Set
            int extraDataOffset = sizeOfPacket / 16;
			//Total Bytes
            long totalBytes = 0;
            
            Random random = new Random( );
			int chance = random.nextInt( 100 );
			
			//Number Of Packets
            int totalPackets = (int)Math.ceil( getFile.length() / (sizeOfPacket - extraDataOffset)); // - packet overhead
			//Initialize buffered input stream with file stream
            bIStream = new BufferedInputStream(new FileInputStream(getFile));
            for (int i = 0; i < totalPackets+1; i++) {
				//Making a byte array
                byte[] byteArray = new byte[sizeOfPacket];
				//read in buffered inout stream
                bIStream.read(byteArray, 0, byteArray.length);
				//Total butes
                totalBytes += byteArray.length;
				//Send first message
                if(chance%2==0) {
                System.out.println( sendFirstMessage + (i + 1) + " " +
                        String.format("%d",(totalBytes - sizeOfPacket)) +
                        ":" + String.format("%d",totalBytes) + " " +
                        String.format("%d",System.currentTimeMillis()) + " " + SENT );
				//add data to datagram packet		
                DatagramPacket datagramPacket = new DatagramPacket(byteArray, byteArray.length, InetAddress.getByName("127.0.0.1"), 4000);
                dgSocket.send(datagramPacket);
                
                }else if(chance%3==0) {
                	System.out.println("Message "+resendMessage);
                }
                else if(chance%5==0) {
                	System.out.println("Message "+ERR);
                }else if(chance%7==0) {
                	System.out.println("Message "+duplAck);
                }
                else if(chance%9==0) {
                	System.out.println("Message "+errAck);
                }
                else {
                	//data corrupt
                	System.out.println("Message "+DROP);
                	System.out.print("Message "+timeout);
                }
                try {
                    Thread.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(ackRcvd +  " " + (i + 1) + " " + moveWnd);
            }
        } finally {
            if (bIStream != null)
                bIStream.close();
            if (dgSocket != null)
                dgSocket.close();
        }

    }
}
