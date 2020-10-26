package laba.commands;


import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import laba.com.company.Ticket;
import laba.com.company.DataBaseHandler;

/**
 * Класс, осуществляющий перезапись элемента по его id
 */
public class Update extends Add {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    private int id;
    private String answer;

    @Override
    public String getName() {
        return "UPDATE";
    }

    @Override
    public void execute(List<Ticket> list) {
        try{
            list = DataBaseHandler.getTicketCollection();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        Ticket ticket = null;
        for (Ticket t : list)
            if(t.getId() == id) {
                ticket = t;
                break;
            }

        if(ticket != null) {
            if(!ticket.getUser().getLogin().equals(getUser().getLogin())){
                answer = ANSI_RED + "Недостаточно прав для изменения этого элемента." + ANSI_RESET;
                return;
            }
            DataBaseHandler.replaceTicket(id, getToAdd());
            getToAdd().setId(id);
            list.add(list.indexOf(ticket), getToAdd());
            list.remove(ticket);

            answer = ANSI_GREEN + "Элемент обновлен." + ANSI_RESET;
        }
        else answer = ANSI_RED +"Такой элемент не найден." + ANSI_RESET;

    }

    @Override
    public void collectData(String arg) {
        try{
            id = Integer.parseInt(arg);
        } catch(NumberFormatException e){
            System.out.println("Введите id элемента:");
            collectData(new Scanner(System.in).nextLine());
            return;
        }
        super.collectData(arg);
    }

    @Override
    public void recordData(String arg) {
        try {
            id = Integer.parseInt(String.valueOf(arg.toCharArray(), 0, arg.indexOf(" ")));
            super.recordData(String.valueOf(arg.toCharArray(), arg.indexOf(" ") + 1, arg.length() - arg.indexOf(" ") - 1));
        }catch(Exception e){
            setIfCorrect(false);
        }
    }

    @Override
    public void printAnswer() {
        System.out.println(answer);
    }
}