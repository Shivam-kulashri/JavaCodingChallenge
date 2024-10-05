package util;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DBPropertyUtil {
    public static String getDBConnectionString(String propertyFileName) throws IOException {
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(propertyFileName);
        properties.load(fis);
        return properties.getProperty("db.connection.string");
    }
}
