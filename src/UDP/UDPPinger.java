package UDP; /**
 * Created by liuyin14 on 2016/10/17.
 */
/**
 * UDPPinger.java -- Basic routines for UDP pinger
 *
 * $Id: UDPPinger.java,v 1.1.1.1 2003/09/30 14:36:11 kangasha Exp $
 *
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class UDPPinger {
    /** Socket which we use. */
    DatagramSocket socket;

    /** Maximum length of a PING message. */
    static final int MAX_PING_LEN = 1024;

    /** Create a socket for sending UDP messages */
    public void createSocket() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Error creating socket: " + e);
        }
    }

    /** Create a socket for receiving UDP messages. This socket must be
     * bound to the given port. */
    public void createSocket(int port) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            System.out.println("Error creating socket: " + e);
        }
    }

    /** Send a UDP ping message which is given as the argument. */
    public void sendPing(PingMessage ping) {
        InetAddress host = ping.getHost();
        int port = ping.getPort();
        String message = ping.getContents();

        try {
	    /* Create a datagram packet addressed to the recipient */
            DatagramPacket packet =
                    new DatagramPacket(message.getBytes(), message.length(),
                            host, port);

	    /* Send the packet */
            socket.send(packet);
            System.out.println("Sent message to " + host + ":" + port);
        } catch (IOException e) {
            System.out.println("Error sending packet: " + e);
        }
    }

    /** Receive a UDP ping message and return the received message. We
     throw an exception to indicate that the socket timed out. This
     can happen when a message was lost in the network. */
    public PingMessage receivePing() throws SocketTimeoutException {
	/* Create packet for receiving the reply */
        byte recvBuf[] = new byte[MAX_PING_LEN];
        DatagramPacket recvPacket =
                new DatagramPacket(recvBuf, MAX_PING_LEN);
        PingMessage reply = null;

	/* Read message from socket. */
        try {
            socket.receive(recvPacket);

            System.out.println("Received message from " +
                    recvPacket.getAddress() +
                    ":" + recvPacket.getPort());
            String recvMsg = new String(recvPacket.getData());
            reply = new PingMessage(recvPacket.getAddress(),
                    recvPacket.getPort(),
                    recvMsg);

        } catch (SocketTimeoutException e) {
	    /* Note: Because the socket has a timeout, we may get a
	     * SocketTimeoutException. The calling function needs to
	     * know of this timeout to know when to quit. However,
	     * SocketTimeoutException is a subclass of IOException
	     * which signals read errors on the socket, so we need to
	     * catch SocketTimeoutException here and throw it
	     * forward. */
            throw e;
        } catch (IOException e) {
            System.out.println("Error reading from socket: " + e);
        }
        return reply;
    }
}


