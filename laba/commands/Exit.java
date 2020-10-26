package laba.commands;

import laba.commands.general.*;
import laba.com.company.Ticket;

import java.util.List;

/**
 * Класс, осуществляющий завршение работы клиента
 */
public class Exit implements Command {
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    @Override
    public String getName() {
        return "EXIT";
    }

    @Override
    public void execute(List<Ticket> list) {
        System.out.println(ANSI_BLUE+"Клиент отключился." + ANSI_RESET);
    }

    @Override
    public void printAnswer() {
        System.out.println(ANSI_GREEN + "Завершение работы." + ANSI_RESET);
        System.exit(0);
    }
}
