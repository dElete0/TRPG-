package basic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class JDBC {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/db_test";
    static final String USER = "root";
    static final String PASS = "root";

    boolean ifConnect = false;
    ServerSocket ss = null;

    DataInputStream dis;
    DataOutputStream dos;

    Connection connection = null;
    Statement statement = null;

    public static void main(String[] args){
        new JDBC().start();
    }

    //start
    public void start(){
        try {
            //连接数据库
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to DB...");
            connection = DriverManager.getConnection(DB_URL,USER,PASS);
            statement = connection.createStatement();

            //连接客户端
            try {
                ss = new ServerSocket(8888);
                ifConnect = true;
            }catch (IOException e) {
                e.printStackTrace();
            }

            //创建线程
            while (ifConnect){
                Socket s = ss.accept();
                SelectSQL se = new SelectSQL(s);
                System.out.println("新线程连接上了");
                new Thread(se).start();
            }

        } catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //关闭各种各样的东西
            try {
                if(statement != null) {
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            try {
                if(connection != null) {
                    connection.close();
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("该关的都关了");
        }
    }

    //查询线程
    class SelectSQL implements Runnable{

        Socket s;
        private boolean isThisNeed = false;

        //构造函数
        public SelectSQL(Socket s){
            this.s = s;
            isThisNeed = true;
        }

        //重写run方法
        public void run() {
            try {
                while(isThisNeed) {
                    String str = dis.readUTF();
                    SelectFun(new Aport(str).fun,new Aport(str).sql);
                }
            } catch (EOFException e) {
                System.out.println("该线程结束");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //解码器
    class Aport{
        public String sql;
        public String fun;

        public Aport(String str){
            StringBuffer sbsql = null;
            StringBuffer sbfun = null;

            for(int i = 0;i < str.length();i++){
                if(str.charAt(i) != '#') {
                    sbfun.append(str.charAt(i));
                } else {
                    i++;
                    while (i<str.length()){
                        sbsql.append(str.charAt(i));
                    }
                    break;
                }
            }
            sql = sbsql.toString();
            fun = sbfun.toString();
        }
    }

    //查询功能
    public void SelectFun(String fun,String sql){
        if(fun == "Login"){
            try {
                ResultSet rs = statement.executeQuery(sql);
                String name = rs.getString("name");
                dos.writeUTF(name);
                dos.flush();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
