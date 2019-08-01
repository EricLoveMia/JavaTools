package cn.eric.jdktools.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: JDBCUtils
 * @Description: TODO
 * @company lsj
 * @date 2019/8/1 16:25
 **/
public class JDBCUtils {
    private static final String driverClass;
    private static final String url;
    private static final String userName;
    private static final String passWord;

    static{
        //获取配置文件内容
        Properties properties = null;
        try {
            InputStream is = new FileInputStream("src/jdbc.properties");
            properties = new Properties();
            properties.load(is);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        driverClass = properties.getProperty("driverClass");
        url = properties.getProperty("url");
        userName = properties.getProperty("userName");
        passWord = properties.getProperty("passWord");
    }


    //注册驱动
    public static void loadDriver() {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //获得连接
    public static Connection getConnection() {
        Connection conn = null;
        try {
            loadDriver();
            conn = DriverManager.getConnection(url, userName, passWord);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeResource(ResultSet rs,PreparedStatement psmt,Connection conn) {
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //垃圾回收尽快回收对象
            rs = null;
        }
        if(psmt != null) {
            try {
                psmt.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            psmt = null;
        }
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            conn = null;
        }
    }
}
