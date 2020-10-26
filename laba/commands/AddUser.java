package laba.commands;

import laba.com.company.DataBaseHandler;
import laba.sercurity.User;
import laba.com.company.Ticket;
import laba.commands.general.*;

import java.util.List;
import java.util.Scanner;

public class AddUser implements CommandWithInput {
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private User user;
    private String answer;

    @Override
    public String getName() {
        return "REGISTER";
    }

    @Override
    public boolean isDataCorrect() {
        return true;
    }

    @Override
    public void collectData(String arg) {
        String login, password;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Регистрация нового пользователя.");
        System.out.println(ANSI_YELLOW+"Введите логин:"+ANSI_RESET);
        login = scanner.nextLine();
        System.out.println(ANSI_YELLOW+"Введите пароль:"+ANSI_RESET);
        password = scanner.nextLine();
        //password = new String(System.console().readPassword());
        user = new User(login, User.getPasswordHash(password));
    }

    @Override
    public void recordData(String arg) {
        throw new RuntimeException("Вызов этого метода не предполагается");
    }

    @Override
    public void execute(List<Ticket> list) {
        if (DataBaseHandler.addUser(user)) answer = ANSI_GREEN+"Пользователь зарегистриворан."+ANSI_RESET;
        else answer = ANSI_RED+"Такое имя уже занято."+ANSI_RESET;
    }

    @Override
    public void printAnswer() {
        System.out.println(answer);
    }
}

