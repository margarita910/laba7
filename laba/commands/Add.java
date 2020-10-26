package laba.commands;

import laba.com.company.*;
import laba.sercurity.User;

import java.util.List;
import java.util.Scanner;
import laba.commands.general.*;

import laba.com.company.DataBaseHandler;

/**
 Команда, добавляющая элемент в коллекцию.
 */

public class Add implements CommandWithInput, CommandWithRightsNeeded {
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private List<Ticket> list;
    private Ticket toAdd;
    private String answer;
    private boolean success = true;
    private User user = null;
    String login;
    String password;

    @Override
    public String getName() {
        return "ADD";
    }

    @Override
    public void execute(List<Ticket> list) {
        if(!DataBaseHandler.checkLoginAccuracy(user)){
            answer = ANSI_RED+"Неверный логин и пароль."+ANSI_RESET;
            return;
        }
        list.add(toAdd);
        DataBaseHandler.addTicket(toAdd);
        answer = ANSI_GREEN+"Ticket добавлен в коллекцию."+ANSI_RESET;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    protected User getUser(){
        return user;
    }

    @Override
    public void collectData(String arg) {
        if (user == null) throw new IllegalArgumentException();
        System.out.println("Для выполнения команды необходимо создать Ticket.");
        String input;
        Scanner scanner = new Scanner(System.in);
        String name;
        System.out.println("Введите поле name: ");
        System.out.print("\tname: ");
        name = scanner.nextLine().trim();
        while (name.isEmpty()){
            System.out.println("Неверный ввод. Повторите ввод поля name.");
            System.out.print("\tname: ");
            name = scanner.nextLine().trim();
        }
        Coordinates coordinates = createCoordinates();
        float price;
        System.out.println("Введите значение поля price: ");
        while (true){
            System.out.print("\tprice: ");
            input = scanner.nextLine();
            if (readFloat(input)){
                price = Float.parseFloat(input);
                if ((price <= 0) || input.equals(" ")){
                }
                else break;
            }
            System.out.println("Вы ввели не правильное значение поля price. Значение данного поля должно быть числом типа float, больше 0. Повторите ввод поля price.");
        }
        String comment;
        System.out.println("Введите комментарий: ");
        System.out.print("\tcomment: ");
        comment = scanner.nextLine().trim();
        while (comment.isEmpty()){
            System.out.println("Неверный ввод. Значение данного поля не может быть null. Повторите ввод поля comment.");
            System.out.print("\tcomment: ");
            comment = scanner.nextLine().trim();
        }
        TicketType type;
        System.out.println("Задайте тип: ");
        type = createType();
        Person person = null;
        System.out.println("Хотите задать поле person? (Введите 'yes' или 'no'.)");
        String answer = scanner.nextLine().trim().toUpperCase();
        if (answer.compareTo("YES") == 0){
            person = createPerson();
        }
        else if (answer.compareTo("NO") == 0) person = null;
        else {
            while (answer.compareTo("YES") != 0 && answer.compareTo("NO") != 0){
                System.out.println("Неверный ввод. Ваш ответ не распознан. Повторите ввод");
                answer = scanner.nextLine().trim().toUpperCase();
            }
        }
        toAdd = new Ticket(name, coordinates, price, comment, type, person, user);
    }

    private static Coordinates createCoordinates(){
        Scanner scanner = new Scanner(System.in);
        Float x = null;
        double y;
        String input;
        System.out.println("Введите координаты: ");
        while (true){
            System.out.print("\tx: ");
            input = scanner.nextLine();
            if (readFloat(input)) x = Float.parseFloat(input);
            else {
                System.out.println("Вы ввели неверное значение x. Данное поле имеет тип float.");
            }
            if (x == null) System.out.println("Повторите ввод.");
            else break;
        }
        while (true){
            System.out.print("\ty: ");
            input = scanner.nextLine();
            if (readDouble(input)) y = Double.parseDouble(input);
            else {
                System.out.println("Вы ввели неверное значение y. Данное поле имеет тип double. Повторите ввод");
                continue;
            }
            break;
        }
        return new Coordinates(x, y);
    }

    private static TicketType createType() {
        System.out.println("Доступные варианты: ");
        for (TicketType type : TicketType.values()) {
            System.out.println("\t" + type);
        }
        String type = null;
        Scanner scanner = new Scanner(System.in);
        boolean test = false;
        while (!test) {
            System.out.print("\ttype: ");
            type = scanner.nextLine();
            for (TicketType type1 : TicketType.values()) {
                if (type1.name().equals(type.toUpperCase())) {
                    test = true;
                    break;
                }
            }
            if (!test){
                System.out.println("Вы ввели неверный тип. Повторите ввод.");
            }
        }
        return TicketType.valueOf(type.toUpperCase());
    }

    private static boolean readFloat(String string){
        try{
            Float.parseFloat(string);
        }
        catch (IllegalArgumentException e){
            return false;
        }
        return true;
    }

    private static Person createPerson(){
        Scanner scanner = new Scanner(System.in);
        String input;
        int height;
        while (true){
            System.out.println("Введите рост: ");
            System.out.print("\theight: ");
            input = scanner.nextLine();
            if (readInt(input)) height = Integer.parseInt(input);
            else {
                System.out.println("Вы ввели неправильный рост. Повторите ввод.");
                continue;
            }
            if (height < 46 || height > 240){
                System.out.println("Вы ввели неправильный рост. Рост человека не может быть меньше 46 и больше 240. Повторите ввод.");
            }
            else break;
        }
        Color eyeColor;
        System.out.println("Введите цвет глаз: ");
        eyeColor = createColor();
        Color hairColor;
        System.out.println("Введите цвет волос: ");
        hairColor = createColor();
        Country nationality;
        System.out.println("Введите национальность: ");
        nationality = createCountry();
        return new Person(height, eyeColor, hairColor, nationality);
    }

    private static boolean readDouble(String string){
        try{
            Double.parseDouble(string);
        }
        catch (IllegalArgumentException e){
            return false;
        }
        return true;
    }

    private static boolean readInt(String string){
        try{
            Integer.parseInt(string);
        }
        catch (IllegalArgumentException e){
            return false;
        }
        return true;
    }

    private static Color createColor(){
        System.out.println("Доступные варианты: ");
        for (Color color : Color.values()) {
            System.out.println("\t" + color);
        }
        String color = null;
        Scanner scanner = new Scanner(System.in);
        boolean test = false;
        while (!test) {
            System.out.print("\tcolor: ");
            color = scanner.nextLine();
            for (Color color1 : Color.values()) {
                if (color1.name().equals(color.toUpperCase())) {
                    test = true;
                    break;
                }
            }
            if (!test){
                System.out.println("Вы ввели неверный цвет. Повторите ввод.");
            }
        }
        return Color.valueOf(color.toUpperCase());
    }

    private static Country createCountry(){
        System.out.println("Доступные варианты: ");
        for (Country country : Country.values()) {
            System.out.println("\t" + country);
        }
        String country = null;
        Scanner scanner = new Scanner(System.in);
        boolean test = false;
        while (!test) {
            System.out.print("\tcountry: ");
            country = scanner.nextLine();
            for (Country country1 : Country.values()) {
                if (country1.name().equals(country.toUpperCase())) {
                    test = true;
                    break;
                }
            }
            if (!test){
                System.out.println("Вы ввели неверную страну. Повторите ввод.");
            }
        }
        return Country.valueOf(country.toUpperCase());
    }

    @Override
    public void recordData(String arg) {
        try {
            if (arg == null) {
                return;
            }
        } catch (Exception e){
            success = false;
        }
    }

    @Override
    public void printAnswer(){
        System.out.println(answer);
    }

    @Override
    public boolean isDataCorrect() {
        return success;
    }

    protected void setIfCorrect(boolean b){
        success = b;
    }

    protected Ticket getToAdd() {
        return toAdd;
    }

    protected void setAnswer(String answer) {
        this.answer = answer;
    }
}

