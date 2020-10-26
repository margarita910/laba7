package Server;

import laba.com.company.DataBaseHandler;
import laba.com.company.Ticket;
import laba.commands.general.Command;
import java.nio.channels.SocketChannel;


import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class ProcessingHandler extends RecursiveAction{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    private final Command command;
    private final SocketChannel channel;

    protected static List<Ticket> collection;

    public ProcessingHandler(Command command, SocketChannel channel){
        this.command = command;
        this.channel = channel;
    }
    @Override
    protected void compute() {
        /*try{
            collection = Collections.synchronizedList(DataBaseHandler.getTicketCollection());
        }
        catch(SQLException e){
        }*/
        System.out.println(ANSI_YELLOW + "Получена команда: " + ANSI_RESET + command.getName());
        System.out.println(ANSI_YELLOW + "Отправляем результат..... " + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "Выполнена команда: " + ANSI_RESET + command.getName());
        command.execute(Server.collection);
        new AnswerSendHandler(command, channel).start();
    }
}
