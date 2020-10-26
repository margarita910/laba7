package laba.commands;

import laba.commands.general.*;
import laba.com.company.DataBaseHandler;
import laba.sercurity.User;
import laba.com.company.Ticket;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс, осуществляющий очищение коллекции
 */
public class Clear implements Command, CommandWithRightsNeeded {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private User user;
    private String answer;

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return "CLEAR";
    }

    @Override
    public void execute(List<Ticket> list) {
        try{
            list = DataBaseHandler.getTicketCollection();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        if(!DataBaseHandler.checkLoginAccuracy(user)){
            answer = ANSI_RED + "Неверный логин или пароль." + ANSI_RESET;
            return;
        }
        if (list.size() > 0){
            for (int i = list.size()-1; i>=0; i--){
                if(list.get(i).getUser().getLogin().equals(user.getLogin())){
                    DataBaseHandler.removeTicket(list.get(i));
                    list.remove(i);
                }
            }
            answer = ANSI_GREEN + "Ваши элементы удалены из данной коллекции." + ANSI_RESET;
        }
        else answer = ANSI_RED + "Коллекция не имеет элементов."+ ANSI_RESET;
    }

    @Override
    public void printAnswer() {
        System.out.println(answer);
    }
}
