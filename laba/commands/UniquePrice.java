package laba.commands;

import laba.com.company.DataBaseHandler;
import laba.commands.general.*;
import laba.com.company.Ticket;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UniquePrice implements CommandWithInput {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    private StringBuilder answer;
    private float price;
    private boolean success;

    @Override
    public String getName() {
        return "UNIQUEPRICE";
    }

    @Override
    public void recordData(String arg) {
        try {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextFloat()){
                price = scanner.nextFloat();
            }
            success = true;
        }
        catch (Exception e){
            success = false;
        }
    }

    @Override
    public void collectData(String arg) {
        recordData(arg);
        while(!success) {
            System.out.println("Неверно введены аргуметы команды.");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextFloat()){
                price = scanner.nextFloat();
            }
            collectData(new Scanner(System.in).nextLine());
        }
    }

    @Override
    public boolean isDataCorrect() {
        return success;
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
            for (int i = 0; i < list.size(); i++){
                if(list.get(i).getPrice() != price){
                    answer.append(list.get(i).getPrice()).append("\n");
                }
            }
        }
        else answer.append(ANSI_RED+"Коллекция не имеет элементов."+ANSI_RESET);
    }

    @Override
    public void printAnswer() {
        System.out.println(answer);
    }
}
