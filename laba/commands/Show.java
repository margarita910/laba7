package laba.commands;

import laba.com.company.DataBaseHandler;
import laba.commands.general.*;
import laba.com.company.Ticket;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс, осуществлящий вывод всех элементов коллекции
 */
public class Show implements Command {
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    private StringBuilder answer;

    @Override
    public String getName() {
        return "SHOW";
    }

    @Override
    public void execute(List<Ticket> list) {
        answer = new StringBuilder();
        try{
            list = DataBaseHandler.getTicketCollection();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        if (list.size() > 0 ){
            for (Ticket ticket : list){
                answer.append(ticket.toString()).append("\n");
            }
        }
        else {
            answer.append(ANSI_RED+"Коллекция не имеет элементов."+ANSI_RESET);
        }
    }

    @Override
    public void printAnswer() {
        System.out.println(answer);
    }
}