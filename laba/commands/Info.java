package laba.commands;

import laba.commands.general.*;
import laba.com.company.Ticket;
import laba.com.company.DataBaseHandler;

import java.util.List;
import java.util.Vector;
import java.sql.*;

/**
 * Класс, осущеествляющий вывод информацию о коллекции
 */
public class Info implements Command {
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    private String answer;

    @Override
    public String getName() {
        return "INFO";
    }

    @Override
    public void execute(List<Ticket> list) {
        list = new Vector();
        try{
            list = DataBaseHandler.getTicketCollection();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        if (list == null || list.size() == 0){
            answer = ANSI_YELLOW+"Коллекция: "+ANSI_RESET+ list.getClass() + ANSI_YELLOW+"\nКоличество элементов: "+ANSI_RESET+ list.size() + "\nВы всегда можете добавить новые элементы в коллекцию =)";
        }
        else {
            answer = ANSI_YELLOW+"Коллекция: "+ANSI_RESET+ list.getClass() + ANSI_YELLOW+"\nКоличество элементов: "+ANSI_RESET+ list.size() + ANSI_YELLOW+"\nТип элементов: "+ANSI_RESET + list.get(0).getClass() +ANSI_YELLOW+"\nКоллекция создана: "+ANSI_RESET+list.get(0).getCreationDate();
        }
    }

    @Override
    public void printAnswer() {
        System.out.println(answer);
    }
}
