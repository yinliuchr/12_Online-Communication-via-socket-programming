package UDP; /**
 * Created by liuyin14 on 2016/10/17.
 */
import java.net.InetAddress;


public class PingMessage {

    private InetAddress addr;
    private int port = 0;
    private String message = null;

    public PingMessage(InetAddress addr, int port, String message){
        this.addr = addr;
        this.port = port;
        this.message = message;
    }

    public InetAddress getHost() {
        return addr;
    }

    public String getContents() {
        return message;
    }

    public int getPort() {
        return port;
    }


}
