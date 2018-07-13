package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.security.UserAuthentication;
import org.h2.tools.Server;

public class Servidor {

    public static void startDb() throws SQLException {
        Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }

    /**
     * @throws SQLException
     */
    public static void stopDb() {
        try {
            Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
