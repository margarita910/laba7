package laba.commands.general;

import laba.com.company.Ticket;

import java.io.Serializable;
import java.util.List;

/**
 * Интерфейс для реализации команд
 */
public interface Command extends Serializable {

    /**
     * Возвращает название команды
     */
    String getName();

    /**
     * Выполняет команду (предполагается, что на сервере)
     * @param collection Коллекция Ticket
     */
    void execute(List<Ticket> collection);

    /**
     * Пишет в консоль ответ, записанный во время выполнения команды execute на сервере
     */
    void printAnswer();

}
