package laba.commands.general;

/**
 * Интерфейс для работы с командой execute_script
 */
public interface CommandMaker extends Command {
    /**
     * Возвращает массив команд, готовых к отправке на сервер
     */
    Command[] getCommands();
}
