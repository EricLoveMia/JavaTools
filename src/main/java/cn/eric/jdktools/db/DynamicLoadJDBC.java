package cn.eric.jdktools.db;

import org.slf4j.Logger;

import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description: 动态加载JDBC驱动
 * @author: 钱旭
 * @date: 2022-03-02 17:36
 **/
public class DynamicLoadJDBC {

    private static final String JDBC_URL = "jdbc:mysql://211.149.243.54:3306/tiger?serverTimezone=UTC&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static final Map<String, String> jdbcVersionMap = new HashMap<String, String>();
    private static final Map<String, Driver> tmpDriverMap = new HashMap<String, Driver>();

    // 动态加载jdbc驱动
    private static void dynamicLoadJdbc(String mysqlJdbcFile) throws Exception {
        URL u = new URL("jar:file:lib/" + mysqlJdbcFile + "!/");
        String classname = jdbcVersionMap.get(mysqlJdbcFile);
        URLClassLoader ucl = new URLClassLoader(new URL[] { u });
        Driver d = (Driver)Class.forName(classname, true, ucl).newInstance();
        DriverShim driver = new DriverShim(d);
        DriverManager.registerDriver(driver);
        tmpDriverMap.put(mysqlJdbcFile, driver);
    }

    // 每一次测试完卸载对应版本的jdbc驱动
    private static void dynamicUnLoadJdbc(String mysqlJdbcFile) throws SQLException {
        DriverManager.deregisterDriver(tmpDriverMap.get(mysqlJdbcFile));
    }

    // 进行一次测试
    private static void testOneVersion(String mysqlJdbcFile) {

        System.out.println("start test mysql jdbc version : " + mysqlJdbcFile);

        try {
            dynamicLoadJdbc(mysqlJdbcFile);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select user()");
            System.out.println("select user() output : ");
            while(rs.next()) {
                System.out.println(rs.getObject(1));
            }
            rs = stmt.executeQuery("show tables");
            System.out.println("show tables output : ");
            while(rs.next()) {
                System.out.println(rs.getObject(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            dynamicUnLoadJdbc(mysqlJdbcFile);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("end !!!");
        System.out.println();
    }

    public static void main(String[] args) {

        //jdbcVersionMap.put("mysql-connector-java-6.0.3.jar", "com.mysql.cj.jdbc.Driver");
        //jdbcVersionMap.put("mysql-connector-java-5.1.6.jar", "com.mysql.jdbc.Driver");
        //jdbcVersionMap.put("mysql-connector-java-5.1.31.jar", "com.mysql.jdbc.Driver");
        //jdbcVersionMap.put("mysql-connector-java-5.1.35.jar", "com.mysql.jdbc.Driver");
        //jdbcVersionMap.put("mysql-connector-java-5.1.39.jar", "com.mysql.jdbc.Driver");
        jdbcVersionMap.put("mysql-connector-java-5.1.49.jar", "com.mysql.jdbc.Driver");

        for(String mysqlJdbcFile : jdbcVersionMap.keySet()) {
            testOneVersion(mysqlJdbcFile);
        }

    }

}
