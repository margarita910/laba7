package laba.commands;

import laba.commands.general.*;
import laba.com.company.DataBaseHandler;
import laba.sercurity.User;
import laba.com.company.Ticket;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Класс, осуществлящий удаление элемента по его индексу
 */

public class RemoveIndex implements CommandWithInput, CommandWithRightsNeeded {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    private int index = -1;
    private String answer;
    private User user;


    @Override
    public String getName() {
        return "REMOVEINDEX";
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
        try {
            if(!list.get(index-1).getUser().getLogin().equals(user.getLogin())){
                answer = ANSI_RED + "Недостаточно прав для удаления этого элемента. " + ANSI_RESET;
                return;
            }
            DataBaseHandler.removeTicket(list.get(index-1));
            list.remove(index-1);
            answer = ANSI_GREEN + "Элемент под индексом "+ index+ " удален."+ ANSI_RESET;
        }
        catch (Exception e){
            answer = ANSI_RED + "Элемент с таким индексом не найден." + ANSI_RESET ;
            e.printStackTrace();
        }
    }

    @Override
    public void printAnswer() {
        System.out.println(answer);
    }

    @Override
    public void collectData(String arg) {
        recordData(arg);
        while(!isDataCorrect()){
            System.out.println("Введите индекс (начиная с 1):");
            collectData(new Scanner(System.in).nextLine());
        }
    }

    @Override
    public void recordData(String arg) {
        try{
            index = Integer.parseInt(arg);
        }catch(Exception ignored){

        }
    }

    @Override
    public boolean isDataCorrect() {
        return index > 0;
    }
}