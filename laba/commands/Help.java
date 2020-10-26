package laba.commands;

import laba.commands.general.*;
import laba.com.company.Ticket;

import java.util.List;

/**
 * Класс, выводящий список команд
 */
public class Help implements Command {
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    @Override
    public String getName() {
        return "HELP";
    }

    @Override
    public void execute(List<Ticket> list) {
    }

    @Override
    public void printAnswer() {
        System.out.println("Доступны следующие команды:");
        System.out.println(ANSI_YELLOW + "Add:" + ANSI_RESET + " добавляет элемент класса Ticket в коллекцию.");
        System.out.println(ANSI_YELLOW + "AddIfMax:" + ANSI_RESET + " добавляет элемент в коллекцию, если значение его поля price больше максимального.");
        System.out.println(ANSI_YELLOW + "Clear:" + ANSI_RESET + " очищает коллекцию.");
        System.out.println(ANSI_YELLOW + "ExecuteScript <file_name>:" + ANSI_RESET + " исполняет скрипт.");
        System.out.println(ANSI_YELLOW + "Exit:" + ANSI_RESET + " осуществляет выход из программы без сохранения изменений.");
        System.out.println(ANSI_YELLOW + "Help:" + ANSI_RESET + " показывает все доступные комманды. ");
        System.out.println(ANSI_YELLOW + "Info:" + ANSI_RESET + " выводит информацию о коллекции.");
        System.out.println(ANSI_YELLOW + "MaxCoordinates:" + ANSI_RESET + " выводит элемент коллекции, значение поля coordinates которого является максимальным.");
        System.out.println(ANSI_YELLOW + "DescendingPrice:" + ANSI_RESET + " выводит знаения поля price всех элементов в порядке убывания.");
        System.out.println(ANSI_YELLOW + "UniquePrice <float price>:" + ANSI_RESET + " выводит уникальные значения поля price всех элементов коллекции.");
        System.out.println(ANSI_YELLOW + "RemoveIndex <int index>:" + ANSI_RESET + " удаляет элемент, находящийся в заданной позиции.");
        System.out.println(ANSI_YELLOW + "RemoveId <int Id>:" + ANSI_RESET + " удаляет элемент из коллекции по его Id.");
        System.out.println(ANSI_YELLOW + "Reorder:" + ANSI_RESET + " отсортировывает коллекцию в порядке, обратном нынешнему.");
        System.out.println(ANSI_YELLOW + "Show:" + ANSI_RESET + " выводит все элементы коллекции.");
        System.out.println(ANSI_YELLOW + "Update <int Id>:" + ANSI_RESET + " обновляет значение элемента коллекции, ID которого равно заданному.");
        System.out.println(ANSI_RED+"Внимание! Команды, изменяющие коллекцию, требуют аутентификации." + ANSI_RESET);
    }
}
