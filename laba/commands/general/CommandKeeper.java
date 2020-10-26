package laba.commands.general;

import laba.sercurity.User;
import laba.commands.*;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Класс, через который происходит работа с командами
 */
public class CommandKeeper {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    private static final HashMap<String, Constructor> commands = new HashMap<>();

    private interface Constructor{
        Command create();
    }

    static{
        commands.put(new Info().getName(), Info::new);
        commands.put(new RemoveId().getName(), RemoveId::new);
        commands.put(new Add().getName(), Add::new);
        commands.put(new Show().getName(), Show::new);
        commands.put(new Update().getName(), Update::new);
        commands.put(new ExecuteScript().getName(), ExecuteScript::new);
        commands.put(new Help().getName(), Help::new);
        commands.put(new AddIfMax().getName(), AddIfMax::new);
        commands.put(new Clear().getName(), Clear::new);
        commands.put(new Exit().getName(), Exit::new);
        commands.put(new DescendingPrice().getName(), DescendingPrice::new);
        commands.put(new MaxCoordinates().getName(), MaxCoordinates::new);
        commands.put(new UniquePrice().getName(), UniquePrice::new);
        commands.put(new RemoveIndex().getName(), RemoveIndex::new);
        commands.put(new Reorder().getName(), Reorder::new);
        commands.put(new AddUser().getName(), AddUser::new);
    }


    /**
     * Возвращает команду, готовую к выполнению (к отправке на сервер)
     * @param name Stirng-название команды
     * @param arg Аргумент команды или пустая строка, если аргумента нет
     * @return Комада, готовая к выполнению
     */
    public static Command getPreparedCommand(String name, String arg, User user){

        Command result;
        name = name.toUpperCase();

        if(commands.containsKey(name))
            result = commands.get(name).create();
        else return null;

        if(result instanceof CommandWithRightsNeeded) {
            if (user == null) {
                Scanner scanner = new Scanner(System.in);
                String login, password;
                System.out.println(ANSI_YELLOW+"Введите логин:"+ANSI_RESET);
                login = scanner.nextLine();
                System.out.println(ANSI_YELLOW+"Введите пароль:"+ANSI_RESET);
                password = scanner.nextLine();
                user = new User(login, User.getPasswordHash(password));
            }
            ((CommandWithRightsNeeded) result).setUser(user);
        }

        if(result instanceof CommandWithInput)((CommandWithInput)result).collectData(arg);

        return result;
    }

    public static Command getCommandFromScript(String name, String arg, User user){
        Command result;
        name = name.toUpperCase();

        if(commands.containsKey(name))
            result = commands.get(name).create();
        else return null;

        if(result instanceof CommandWithRightsNeeded) ((CommandWithRightsNeeded) result).setUser(user);
        if(result instanceof CommandWithInput) ((CommandWithInput)result).recordData(arg);



        return result;
    }
    /**
     * Проверяет, существует ли команда с таким названием
     * @param commandName String-название команды
     */
    public static boolean exists(String commandName){
        boolean result;
        result = commands.containsKey(commandName.toUpperCase());
        return result;

    }
}


