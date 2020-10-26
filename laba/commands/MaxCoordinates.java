package laba.commands;

import laba.com.company.Ticket;
import laba.com.company.DataBaseHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;

import static java.util.Collections.max;

/**
 * Команда, которая выводит элемент коллекции, значение поля coordinates которого является максимальным.
 */

public class MaxCoordinates extends Show {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    private StringBuilder answer;

    @Override
    public String getName() {
        return "MAXCOORDINATES";
    }

    @Override
    public void execute(List<Ticket> list) {
        answer = new StringBuilder();
        Ticket ticket;
        try{
            list = DataBaseHandler.getTicketCollection();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        if (list.size() > 0 ){
            class FoundMaxCoordinates implements Comparator<Ticket> {
                @Override
                public int compare (Ticket ticket1, Ticket ticket2){
                    int result = 0;
                    if (!(ticket1.getCoordinates().getX() == null || ticket2.getCoordinates().getX() == null )){
                        result = ticket1.getCoordinates().compareTo(ticket2.getCoordinates());
                    }
                    return result;
                }
            }
            ticket = max(list, new FoundMaxCoordinates());
            answer.append(ticket.toString());
        }
        else answer.append(ANSI_RED+"Коллекция не имеет элементов."+ANSI_RESET);
    }

    @Override
    public void printAnswer() {
        System.out.println(answer);
    }
}
