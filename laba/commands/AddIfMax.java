package laba.commands;

import laba.com.company.DataBaseHandler;
import laba.com.company.Ticket;
import laba.com.company.Coordinates;

import java.sql.SQLException;
import java.util.List;
import java.util.Comparator;

import static java.util.Collections.max;

/**
 Команда, которая добавляет элемент в коллекцию, если его значение больше максимального.
 */

public class AddIfMax extends Add{
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    @Override
    public String getName() {
        return "ADDIFMAX";
    }


    @Override
    public void execute(List<Ticket> list) {
        try{
            list = DataBaseHandler.getTicketCollection();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        float price = getToAdd().getPrice();
        if (list.stream().noneMatch(element -> {
            if(element.getPrice() != null) return element.getPrice().compareTo(price) > 0;
            else return false;
        }))
            super.execute(list);
        else
            setAnswer(ANSI_RED+"Элемент не является максимальным по цене. Ticket не добавлен"+ANSI_RESET);
    }
}
