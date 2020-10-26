package Client;

import laba.commands.ExecuteScript;
import laba.commands.Exit;
import laba.commands.general.*;
import laba.sercurity.User;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Основной класс клиента
 */
public class Client {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    private InputStream input = null;
    private OutputStream output = null;
    private String address = null;
    private int port = -1;

    /**
     * Метод, отвечающий за общую работу клиента
     * @param address String-адрес сервера
     * @param port Номер порта сервера
     */
    public void loop(String address, int port){
        this.address = address;
        this.port = port;
        System.out.printf(ANSI_BLUE+"Подключение по адресу "+ANSI_RESET+ANSI_RED+"%s "+ANSI_RESET+ANSI_BLUE+"и порту "+ANSI_RESET+ANSI_RED+"%d "+ANSI_RESET+ANSI_BLUE+".......\n"+ANSI_RESET, address, port);

        try(Socket socket = new Socket(address, port);
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream()
        ){
            this.input = input;
            this.output = output;
            System.out.println(ANSI_GREEN+"Подключение успешно завершено."+ANSI_RESET);
            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                System.out.println("Введите название команды и её аргументы, если они требуются. Для просмотра доступных команд введите"+ANSI_GREEN+" Help. "+ANSI_RESET);
                System.out.println(ANSI_GREEN+"Для добавления нового пользователя введите Register. "+ANSI_RESET);
                String line = scanner.nextLine();
                String commandName, arg;
                if(line.contains(" ")){
                    commandName = String.valueOf(line.toCharArray(), 0, line.indexOf(" ")).trim();
                    arg = String.valueOf(line.toCharArray(), line.indexOf(" ") + 1,
                            line.length() - line.indexOf(" ") - 1);
                }
                else{
                    commandName = line;
                    arg = "";
                }
                User user = null;
                if("ADD".equals(commandName.toUpperCase())||"ADDIFMAX".equals(commandName.toUpperCase())||"UPDATE".equals(commandName.toUpperCase())||"CLEAR".equals(commandName.toUpperCase())){
                    String login, password;
                    System.out.println(ANSI_YELLOW+"Введите логин:"+ANSI_RESET);
                    login = scanner.nextLine();
                    System.out.println(ANSI_YELLOW+"Введите пароль:"+ANSI_RESET);
                    password = scanner.nextLine();
                    //password = new String(System.console().readPassword());
                    user = new User(login, User.getPasswordHash(password));
                    sendObject(user);
                    Object obj = receiveObject();
                    if(obj instanceof Boolean)
                        if((boolean)obj) System.out.println(ANSI_GREEN+"Пользователь авторизован."+ANSI_RESET);
                        else {
                            System.out.println(ANSI_RED+"Неверный логин или пароль."+ANSI_RESET);
                            continue;
                        }
                    else System.err.println(ANSI_RED+"Внутренняя ошибка программы."+ANSI_RESET);
                }
                else user = null;
                Command command = CommandKeeper.getPreparedCommand(commandName, arg, user);
                if(command == null){
                    System.out.println(ANSI_RED+"Вы ввели неверную команду. Повторите ввод."+ANSI_RESET);
                    continue;
                }
                performCommand(command);
            }
        }catch (Exception e) {
            errorHandler(e);
        }
        finally{
            try {
                sendObject(new Exit());
            } catch (IOException e) {}
        }
    }

    /**
     * Метод отправляет объект на сервер
     * @param obj Объект, который должен быть отправлен
     */
    private void sendObject(Object obj) throws IOException{
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objOutput = new ObjectOutputStream(byteStream);
        objOutput.writeObject(obj);
        output.write(byteStream.toByteArray());
    }

    /**
     * Метод получает объект
     * @return Объект, полученный от сервера
     */
    private Object receiveObject() throws IOException, ClassNotFoundException{
        ObjectInputStream objInput = new ObjectInputStream(input);
        return objInput.readObject();
    }

    /**
     * Метод отправляет команду, получает ответ и выводит ео в консоль
     * @param command команда для отправки
     */
    private void performCommand(Command command) throws IOException, ClassNotFoundException{
        if(command instanceof CommandMaker) {
            for (Command tiny : ((CommandMaker) command).getCommands()) performCommand(tiny);
            FileOpenedController.removeFromOpened(((ExecuteScript) command).getFileReading().getName());
        }
        else{
            sendObject(command);
            Command received = ((Command)receiveObject());
            received.printAnswer();
        }
    }


    /**
     * Обрабатывает ошибки
     */
    private void errorHandler(Exception e){
        //System.err.println(ANSI_RED+"Произвошла ошибка."+ANSI_RESET);
        if(e instanceof ClassNotFoundException) {
            System.err.println(ANSI_RED+"Произошла ошибка целостности пакета, нужная команда не найдена на стороне сервера."+ANSI_RESET);
        } else if(e instanceof IOException){
            System.out.println(ANSI_RED+"Произошла ошибка подключения!"+ANSI_RESET);
            System.out.println(ANSI_BLUE+"Выберите действие:\n"+ANSI_RESET+ ANSI_YELLOW+"1. Повторить соединение.\n2. Изменить адрес и порт.\n3. Завершить сессию."+ANSI_RESET);
            int answer;
            while(true) {
                try {
                    answer = Integer.parseInt(new Scanner(System.in).nextLine());
                    break;
                } catch (Exception ex){
                    System.out.println(ANSI_RED+"Введите 1, 2 или 3."+ANSI_RESET);
                }
            }
            if (answer == 1) loop(address, port);
            else if (answer == 2){
                setAndRestart();
            }
            else if (answer == 3){
                System.out.println(ANSI_BLUE+"Завершение сессии."+ANSI_RESET);
                System.exit(0);
            }
            else{
                System.out.println(ANSI_RED+"Такого варианта нет."+ANSI_RESET);
                errorHandler(e);
            }
        }
        else if (e instanceof ConnectException){
            System.out.println(ANSI_RED+"Сервер временно не доступен. Пробуем подключиться еще раз."+ANSI_RESET);
            setAndRestart();
        }
        else{
            System.err.println("Произошла неизвестная ошибка");
            e.printStackTrace();
        }
    }

    /**
     * Переустанавливает адрес и порт и перезапускает клиент
     */
    private void setAndRestart(){
        Scanner scanner = new Scanner(System.in);
        int port; String address;

        while (true) {
            try {
                System.out.println(ANSI_GREEN+"Введите адрес: "+ANSI_RESET);
                address = scanner.nextLine();
                break;
            } catch (Exception ignored) {}
        }
        while (true){
            try {
                System.out.println(ANSI_GREEN+"Введите номер порта: "+ANSI_RESET);
                port = Integer.parseInt(scanner.nextLine());
                break;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        loop(address, port);
    }
}