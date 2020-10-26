package Server;

import java.io.IOException;
import java.sql.*;

public class Main {
    final static int PORT = 7788;
    public static void main(String[] args)  throws SQLException{
        new Server().startServer(PORT);
    }
}

