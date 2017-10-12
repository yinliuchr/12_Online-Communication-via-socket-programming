package Simple; /**
 * Created by liuyin14 on 2016/10/17.
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class ToMyself {
    public static void main(String[] args) throws Exception {
        Date dDate = new Date();
        DateFormat dFormat = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL,
                Locale.US);
        String command = null;

        /** 与邮件服务器建立TCP连接. */
        // TODO: 1.在""中填入我们的smtp服务器和正确端口，助教老师的服务器地址166.111.74.90，端口是25
        // e.g. Socket socket = new Socket("mails.163.com",25);
        Socket socket = new Socket("mails.tsinghua.edu.cn",25);

        /** 创建BufferedReader每次读入一行信息. */
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        /** 读入系统的欢迎信息. */
        String response = br.readLine();
        System.out.println(response);
        // TODO: 2.把code改为合适的代码
        int code = 220;	//把-1改为合适的代码
        if (!response.startsWith(Integer.toString(code))) {
            throw new Exception(code + " reply not received from server.");
        }

        /** 取得socket输出流的引用. */
        OutputStream os = socket.getOutputStream();

        /** 发送 HELO 命令并取得服务器响应.
         *	填入所需的命命, 在以下的"\r\n"前写入所需的命令
         *	e.g.	command = "Helo x\r\n";
         *	其中\r\n为回车符,每个命今都必需以它们结尾. */
        // TODO: 3.填入命令
        command = "HELO smtp\r\n";
        System.out.print(command);
        os.write(command.getBytes("US-ASCII"));
        response = br.readLine();
        System.out.println(response);
        // TODO: 4.把code改为合适的代码
        code = 250; //把-1改为合适的代码
        if (!response.startsWith(Integer.toString(code))) {
            throw new Exception(code + " reply not received from server.");
        }

        /** 发送 MAIL FROM 命令. */
        // TODO: 5.填入命令
        command = "MAIL FROM: <yinliuchr@gmail.com>\r\n";
        System.out.print(command);
        os.write(command.getBytes("US-ASCII"));
        response = br.readLine();
        System.out.println(response);
        // TODO: 6.把code改为合适的代码
        code = 250; //把-1改为合适的代码
        if (!response.startsWith(Integer.toString(code))) {
            throw new Exception(code + " reply not received from server.");
        }

        /** 发送 RCPT TO 命令. */
        // TODO: 7.填入命令
        command = "RCPT TO: <liuyin14@mails.tsinghua.edu.cn>\r\n";
        System.out.print(command);
        os.write(command.getBytes("US-ASCII"));
        response = br.readLine();
        System.out.println(response);
        // TODO: 8.把code改为合适的代码
        code = 250; //把-1改为合适的代码
        if (!response.startsWith(Integer.toString(code))) {
            throw new Exception(code + " reply not received from server.");
        }

        /** 发送 DATA 命令. */
        // TODO: 9.填入命令
        command = "DATA\r\n";
        System.out.print(command);
        os.write(command.getBytes("US-ASCII"));
        response = br.readLine();
        System.out.println(response);
        // TODO: 10.把code改为合适的代码
        code = 354; //把-1改为合适的代码
        if (!response.startsWith(Integer.toString(code))) {
            throw new Exception(code + " reply not received from server.");
        }

        /** 自动写入当前的日期 */
        String date = "DATE " + dFormat.format(dDate) + "\r\n";
        System.out.print(date);
        os.write(date.getBytes("US-ASCII"));
        String str = "";
        // TODO: 11.把"x@x.x"改为邮件中显示的发件人地址
        str = "From:" + "academic@DA.tsinghua.edu.cn" + "\r\n";
        System.out.print(str);
        os.write(str.getBytes("US-ASCII"));
        // TODO: 12.把"x@x.x"改为邮件中显示的收件人地址
        str = "To:" + "liuyin14@mails.tsinghua.edu.cn" + "\r\n";
        System.out.print(str);
        os.write(str.getBytes("US-ASCII"));


        /** 发送邮件內容. */
        // TODO: 13.在"x"中填入Subject内容.
        str = "SUBJECT:" + "Scholarship" + "\r\n\r\n";
        System.out.print(str);
        os.write(str.getBytes("UTF-8"));

        // TODO: 14.在"x"中填入邮件正文内容.
        str = "柳荫同学：" + "\r\n" +"    恭喜你获得了清华大学自动化系国家奖学金！" + "\r\n" + "    请本周四下午2.00去主楼409签字！"
                + "\r\n" + "\r\n" + "自动化系教务处" + "\r\n";
        System.out.print(str);
        os.write(str.getBytes("UTF-8"));

        /** 以.作为邮件内容的结束符 */
        str = ".\r\n";
        System.out.print(str);
        os.write(str.getBytes("US-ASCII"));
        response = br.readLine();
        System.out.println(response);
        // TODO: 15.把code改为合适的代码
        code = 250; //把-1改为合适的代码
        if (!response.startsWith(Integer.toString(code))) {
            throw new Exception(code + " reply not received from server.");
        }

        /** 发送 QUIT 命令. */
        //TODO:	16.填入命令
        command = "QUIT\r\n";
        System.out.print(command);
        os.write(command.getBytes("US-ASCII"));
        response = br.readLine();
        System.out.println(response);
    }
}
