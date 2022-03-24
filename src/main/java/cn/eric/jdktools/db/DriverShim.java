package cn.eric.jdktools.db;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description: jdbc drivier
 * @author: 钱旭
 * @date: 2022-03-02 17:38
 **/
public class DriverShim implements Driver{

    private Driver driver;
    DriverShim(Driver d) { this.driver = d; }
    public boolean acceptsURL(String u) throws SQLException {
        return this.driver.acceptsURL(u);
    }
    public Connection connect(String u, Properties p) throws SQLException {
        return this.driver.connect(u, p);
    }
    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return this.driver.getPropertyInfo(url, info);
    }
    @Override
    public int getMajorVersion() {
        return this.driver.getMajorVersion();
    }
    @Override
    public int getMinorVersion() {
        return this.driver.getMinorVersion();
    }
    @Override
    public boolean jdbcCompliant() {
        return this.driver.jdbcCompliant();
    }
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.driver.getParentLogger();
    }
}
