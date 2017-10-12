package UDP; /**
 * Created by liuyin14 on 2016/10/17.
 */
/**
 * UDPClient.java -- Simple UDP client
 *
 * $Id: UDPClient.java,v 1.2 2003/10/14 14:25:30 kangasha Exp $
 *
 */

import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;

public class UDPClient extends UDPPinger implements Runnable {
    /** Host to ping */
    String remoteHost;

    /** Port number of remote host */
    int remotePort;

    /** How many pings to send */
    static final int NUM_PINGS = 10;

    /** How many reply pings have we received */
    int numReplies = 0;

    /** Array for holding replies and RTTs */
    static boolean[] replies = new boolean[NUM_PINGS];

    static long[] rtt = new long[NUM_PINGS];

	/*
	 * Send our own pings at least once per second. If no replies received
	 * within 5 seconds, assume ping was lost.
	 */
    /** 1 second timeout for waiting replies */
    static final int TIMEOUT = 1000;

    /** 5 second timeout for collecting pings at the end */
    static final int REPLY_TIMEOUT = 5000;

    /** Create UDPClient object. */
    public UDPClient(String host, int port) {
        remoteHost = host;
        remotePort = port;
    }

    /**
     * Main Function.
     */
    public static void main(String args[]) {
        //TODO: 修改为期望Ping的机器正确的IP地址和端口,现在是本机设置.改端口时同时要改UDPServer的设置.
        String host = "183.172.144.173";
        int port = 9876;

        System.out.println("Contacting host " + host + " at port " + port);

        UDPClient client = new UDPClient(host, port);
        client.run();	//运行程序
    }

    /** Main code for pinger client thread. */
    public void run() {
		/* Create socket. We do not care which local port we use. */
        createSocket();
        try {
            socket.setSoTimeout(TIMEOUT);
        } catch (SocketException e) {
            System.out.println("Error setting timeout TIMEOUT: " + e);
        }

        for (int i = 0; i < NUM_PINGS; i++) {
			/*
			 * Message we want to send. Add space at the end for easy parsing of
			 * replies.
			 */
            Date now = new Date();
            String message = "PING " + i + " " + now.getTime() + " ";
            replies[i] = false;
            rtt[i] = 1000000;
            PingMessage ping = null;

			/* Send ping to recipient */
            try {
                //TODO: 1.PingMessage对象包含了什么信息?
                //TODO: 1.PingMessage对象包含了server的地址、端口号及'message'信息（上面81行有显示）
                ping = new PingMessage(InetAddress.getByName(remoteHost),
                        remotePort, message);
            } catch (UnknownHostException e) {
                System.out.println("Cannot find host: " + e);
            }
            sendPing(ping);

			/* Read reply */
            try {
                //TODO: 2.这里取得的PingMessage reply对象与上面发送的ping对象内容是否一样?
                //TODO: 2.这里取得的PingMessage reply对象与上面发送的ping对象内容一样，因为从UDPServer类中可以看到它又把receivepacket发了回来
                PingMessage reply = receivePing();
                //TODO:	3.handleReply的作用?是以致它所改变的变量值是什么?
                //TODO:	3.handleReply的作用是更新UDPClient的变量内容 是以致它所改变的变量值是是2个全局变量replies和rtt（从本程序结尾的该函数可看出）
                handleReply(reply.getContents());
            } catch (SocketTimeoutException e) {
				/*
				 * Reply did not arrive. Do nothing for now. Figure out lost
				 * pings later.
				 */
            }
        }
		/*
		 * We sent all our pings. Now check if there are still missing replies.
		 * Wait for a reply, if nothing comes, then assume it was lost. If a
		 * reply arrives, keep looking until nothing comes within a reasonable
		 * timeout.
		 */
        try {
            socket.setSoTimeout(REPLY_TIMEOUT);
        } catch (SocketException e) {
            System.out.println("Error setting timeout REPLY_TIMEOUT: " + e);
        }
        //TODO:	4.这个while循环的作用是什么,与第96行的receivePing调用有什么关系,既然上面都已调用了receivePing()，为何这里要重新调用??
        //TODO:	4.这个while循环的作用是处理超时的 reply,第96行的receivePing调用是处理在1000ms内到达的,
        //          而这里的是为了处理在最后5s内到达的，从上面的118行的try语句可以看出
        while (numReplies < NUM_PINGS) {
            try {
                PingMessage reply = receivePing();
                handleReply(reply.getContents());
            } catch (SocketTimeoutException e) {
				/* Nothing coming our way apparently. Exit loop. */
                numReplies = NUM_PINGS;
            }
        }
		/* Print statistics */
        for (int i = 0; i < NUM_PINGS; i++) {
            System.out.println("PING " + i + ": " + replies[i] + " RTT: "
                    + ((rtt[i] > 0) ? Long.toString(rtt[i]):"< 1")+ " ms");
        }

    }

    /**
     * Handle the incoming ping message. For now, just count it as having been
     * correctly received.
     */
    private void handleReply(String reply) {
        String[] tmp = reply.split(" ");
        int pingNumber = Integer.parseInt(tmp[1]);
        long then = Long.parseLong(tmp[2]);
        replies[pingNumber] = true;
		/* Calculate RTT and store it in the rtt-array. */
        Date now = new Date();
        //TODO:	5. 请简要说明这里的rtt的计算过程.
        //TODO:	5. 请简要说明这里的rtt的计算过程.then表示的是reply字符串中的时间，即发送时间，now读出当前时间，即接受时间，两者相减即为RTT
        rtt[pingNumber] = now.getTime() - then;
        numReplies++;
    }
}
