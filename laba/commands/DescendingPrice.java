package laba.commands;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import laba.com.company.DataBaseHandler;
import laba.com.company.Ticket;
import java.util.Comparator;
import java.util.ArrayList;

import static java.util.Collections.sort;

public class DescendingPrice extends Show {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    private StringBuilder answer;

    @Override
    public String getName() {
        return "DESCENDINGPRICE";
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
        if (list.size() > 0){
            class DescendingComportaor implements Comparator<Ticket> {
                @Override
                public int compare(Ticket ticket1, Ticket ticket2) {
                    int result = 0;
                    if (!(ticket1.getPrice() < 0 || ticket2.getPrice() < 0)){
                        result = ticket1.getPrice().compareTo(ticket2.getPrice());
                    }
                    return result;
                }
            }
            Comparator comparator = Collections.reverseOrder(new DescendingComportaor());
            sort(list, comparator);
            for (Ticket ticket : list){
                answer.append(ticket.getPrice()).append("\n");
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
