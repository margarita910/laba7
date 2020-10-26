package laba.commands;

import laba.commands.general.*;
import laba.com.company.DataBaseHandler;
import laba.sercurity.User;
import laba.com.company.Ticket;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;


/**
 * Класс, осуществлящий удаление элемента по его id
 */
public class RemoveId implements CommandWithInput, CommandWithRightsNeeded {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    private int id = -1;
    private String answer;
    private boolean success = true;
    private User user;

    @Override
    public String getName() {
        return "REMOVEID";
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void execute(List<Ticket> list) {
        if(!DataBaseHandler.checkLoginAccuracy(user)){
            answer = ANSI_RED + "Неверный логин или пароль." + ANSI_RESET;
            return;
        }
        try{
            list = DataBaseHandler.getTicketCollection();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        Object[] tempo = list.stream().filter(element -> element.getId() == id).toArray();
        if (tempo.length != 0) {
            if(!(((Ticket)tempo[0]).getUser().getLogin().equals(user.getLogin()))){
                answer = ANSI_RED + "Недостаточно прав для удаления этого элемента." + ANSI_RESET;
                return;
            }
            DataBaseHandler.removeTicket((Ticket) tempo[0]);
            list.remove(tempo[0]);
            answer = ANSI_GREEN + "Элемент с id "+ id + " удален." + ANSI_RESET;
        }
        else answer = ANSI_RED+"Элемент с таким Id не найден." + ANSI_RESET;
    }

    @Override
    public void collectData(String arg) {
        recordData(arg);
        while(!isDataCorrect()){
            System.out.println("Введите id элемента:");
            collectData(new Scanner(System.in).nextLine());
        }

    }

    @Override
    public void recordData(String arg) {
        try {
            id = Integer.parseInt(arg);
            success = true;
        }catch(NumberFormatException e){
            success = false;
        }
    }

    @Override
    public void printAnswer() {
        System.out.println(answer);
    }

    @Override
    public boolean isDataCorrect() {
        return success;
    }
}