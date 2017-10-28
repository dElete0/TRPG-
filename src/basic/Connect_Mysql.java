package basic;

import java.sql.*;

public class Connect_Mysql {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/db_test";
    static final String USER = "root";
    static final String PASS = "root";

    public static void main(String[] args){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            //注册驱动
            Class.forName(JDBC_DRIVER);

            //打开一个连接
            System.out.println("Connecting to DB...");
            connection = DriverManager.getConnection(DB_URL,USER,PASS);

            //创建一个查询
            System.out.println("Creating statement...");
            statement = connection.createStatement();
            String sql;
            sql = "SELECT id,name,ifLogin,password,isAdm FROM users";

            //提交一个sql语句、执行一个sql语句
            resultSet = statement.executeQuery(sql);

            //从结果集提取数据
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                boolean ifLogin = resultSet.getBoolean("ifLogin");
                String password = resultSet.getString("password");
                boolean isAdm = resultSet.getBoolean("isAdm");

                System.out.println("ID" + id);
                System.out.println("Name" + name);
                System.out.println("IfLogin" + ifLogin);
                System.out.println("Password" + password);
                System.out.println("isAdm" + isAdm);

            }

        } catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {

            //关闭各种各样的东西
            try {
                if(resultSet != null) {
                    resultSet.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }

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
            }catch (SQLException e){
                e.printStackTrace();
            }

            System.out.println("该关的都关了");
        }
    }
}
