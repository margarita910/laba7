package Server;

import laba.com.company.Ticket;
import laba.com.company.DataBaseHandler;

import java.sql.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

/**
 * Основной класс сервера
 */
public class Server {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";

    protected static ExecutorService inquiryReaderPool;
    protected static ForkJoinPool processingInquiryPool;

    protected static List<Ticket> collection;

    public void startServer(int port) throws SQLException{

        inquiryReaderPool = Executors.newFixedThreadPool(101);
        processingInquiryPool = ForkJoinPool.commonPool();

        collection = Collections.synchronizedList(DataBaseHandler.getTicketCollection());
        System.out.println(ANSI_GREEN + "Коллекция загружена из Базы Данных."+ANSI_RESET);
        System.out.printf(ANSI_BLUE + "Адрес: " + ANSI_RESET + ANSI_RED + "127.0.0.1 " + ANSI_RESET+ ANSI_BLUE +"Порт: "+ANSI_RESET+ANSI_RED+port+ANSI_RESET+"\n");

        try(ServerSocketChannel channel = ServerSocketChannel.open().bind(new InetSocketAddress(port))) {
            for(;;){
                SocketChannel channel1 = channel.accept();
                collection = Collections.synchronizedList(DataBaseHandler.getTicketCollection());
                InputHandler input = new InputHandler(channel1);
                inquiryReaderPool.execute(input);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }

}