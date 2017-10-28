package basic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;

@WebServlet(name = "LoginSever")
public class LoginSever extends HttpServlet {

    ResultSet re = null;
    String name;
    String password;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    boolean ifConnect = false;
    Socket s;

    //获取数据库值
    public String getname(String sql) {
        String str = null;

        try {
            dos.writeUTF(sql);
            dos.flush();
            dis = new DataInputStream(s.getInputStream());
            str = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    //连接服务器
    public void connect() {
        try {
            s = new Socket("127.0.0.1", 8888);
            dos = new DataOutputStream(s.getOutputStream());
            System.out.println("已经连上了");
            ifConnect = true;
        }catch (UnknownHostException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取HTTP传来的参数
        name = request.getParameter("users");
        password = request.getParameter("password");
        StringBuffer sb = new StringBuffer();
        sb.append("Login#SELECT name FROM users WHERE name='")
                .append(name)
                .append("' AND password='")
                .append(password)
                .append("'");
        String sql = sb.toString();

        //连接到JDBC
        connect();

        //判断是否为user
        if(name == getname(sql)){
            response.sendRedirect("/webapps/welcome.html");
        }else{
            response.sendRedirect("/webapps/login.html");
        }

    }
}
