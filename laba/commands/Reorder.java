package laba.commands;

import java.sql.SQLException;
import java.util.List;

import laba.com.company.DataBaseHandler;
import laba.com.company.Ticket;
import java.util.ArrayList;

import static java.util.Collections.*;

/**
 Команда, отсортировывающая коллекцию в порядке, обратном данному.
 */

public class Reorder extends Show{
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    String answer;

    @Override
    public String getName() {
        return "REORDER";
    }

    @Override
    public void execute(List<Ticket> list) {
        try{
            list = DataBaseHandler.getTicketCollection();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        if (list.size() > 0 ){
            reverse(list);
            answer = ANSI_GREEN+"Коллекция отсортирована в обратном порядке."+ANSI_RESET;
        }
        else{
            answer = ANSI_RED+"Коллекция не имеет элементов."+ANSI_RESET;
        }
    }

    @Override
    public void printAnswer() {
        System.out.println(answer);
    }
}
