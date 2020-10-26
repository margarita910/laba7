package laba.com.company;

import laba.sercurity.User;
import laba.com.company.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DataBaseHandler {
    private static Connection connection;
    static{
        try {
            Class.forName("org.postgresql.Driver");
            //"jdbc:postgresql://localhost:5432/postgres", "postgres", "\password"
            connection = DriverManager.getConnection("jdbc:postgresql://pg/studs", "s285704", "pjs291");
            //connection = DriverManager.getConnection("jdbc:postgresql://localhost:4006/studs", "s285704", "pjs291");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void addTicket(Ticket ticket){
        PreparedStatement stToAdd;
        PreparedStatement stForId;
        try {
            stToAdd = connection.prepareStatement("insert into tickets (name, coordinate_x, coordinate_y, creation_date," +
                    " price, comment, type, person_height, person_eyecolor, person_haircolor," +
                    " person_nationality, owner) " +
                    "values (?, ?, ?, ?::timestamp, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (PreparedStatement st : new PreparedStatement[]{stToAdd,}) {
                st.setString(1, ticket.getName());
                st.setFloat(2, ticket.getCoordinates().getX());
                st.setDouble(3, ticket.getCoordinates().getY());
                st.setString(4, ticket.getCreationDate().toString());
                st.setFloat(5, ticket.getPrice());
                st.setString(6, ticket.getComment());
                st.setString(7, ticket.getType().toString());
                if(ticket.getPerson() != null) {
                    st.setString(8, String.valueOf(ticket.getPerson().getHeight()));
                    st.setString(9, ticket.getPerson().getEyeColor().toString());
                    st.setString(10, ticket.getPerson().getHairColor().toString());
                    st.setString(11, ticket.getPerson().getNationality().toString());
                }
                else {
                    st.setString(8, null);
                    st.setString(9, null);
                    st.setString(10, null);
                    st.setString(11, null);
                }
                st.setString(12, ticket.getUser().getLogin());
            }
            stToAdd.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeTicket(Ticket element){
        try {
            PreparedStatement st = connection.prepareStatement("delete from tickets where id = ?");
            st.setInt(1, element.getId());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void replaceTicket(int id, Ticket ticket){
        try {
            PreparedStatement st = connection.prepareStatement("update tickets set name=?, coordinate_x=?, coordinate_y=?, creation_date=?::timestamp," +
                    " price = ?, comment = ?, type = ?, person_height = ?, person_eyeColor = ?, person_hairColor=?," +
                    " person_nationality = ?, owner = ? where id=?");
            st.setString(1, ticket.getName());
            st.setFloat(2, ticket.getCoordinates().getX());
            st.setDouble(3, ticket.getCoordinates().getY());
            st.setString(4, ticket.getCreationDate().toString());
            st.setFloat(5, ticket.getPrice());
            st.setString(6, ticket.getComment());
            st.setString(7, ticket.getType().toString());
            if(ticket.getPerson() != null) {
                st.setString(8, String.valueOf(ticket.getPerson().getHeight()));
                st.setString(9, ticket.getPerson().getEyeColor().toString());
                st.setString(10, ticket.getPerson().getHairColor().toString());
                st.setString(11, ticket.getPerson().getNationality().toString());
            }
            else {
                st.setString(8, null);
                st.setString(9, null);
                st.setString(10, null);
                st.setString(11, null);
            }
            st.setString(12, ticket.getUser().getLogin());
            st.setInt(13, id);
            st.executeUpdate();
        }
        catch (SQLException e) {
        }
    }

    public static boolean addUser(User user){
        try {
            PreparedStatement st = connection.prepareStatement("insert into users (login, password) values (?, ?)");
            st.setString(1, user.getLogin());
            st.setString(2, user.getHash());
            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    public static List<Ticket> getTicketCollection() throws SQLException{
        List<Ticket> list = new Vector<>();
        try {
            ResultSet r = connection.createStatement().executeQuery("select * from tickets");
            while(r.next()){
                PreparedStatement ps = connection.prepareStatement("select * from users where login=?");
                ps.setString(1, r.getString("owner"));
                ResultSet user = ps.executeQuery();
                user.next();
                String time = r.getString("creation_date");
                String person_height = r.getString("person_height");
                Person person;
                if (person_height != null){
                    list.add(new Ticket(r.getInt("id"), r.getString("name"), new Coordinates(r.getFloat("coordinate_x"), r.getDouble("coordinate_y")),
                            r.getTimestamp("creation_date"), r.getFloat("price"),
                            r.getString("comment"), TicketType.valueOf(r.getString("type")),
                            new Person(r.getInt("person_height"), Color.valueOf(r.getString("person_eyecolor")), Color.valueOf(r.getString("person_haircolor")), Country.valueOf(r.getString("person_nationality"))),
                            new User(user.getString("login"), user.getString("password"))
                    ));
                }
                else {
                    person = null;
                    list.add(new Ticket(r.getInt("id"), r.getString("name"), new Coordinates(r.getFloat("coordinate_x"), r.getDouble("coordinate_y")),
                            r.getTimestamp("creation_date"), r.getFloat("price"),
                            r.getString("comment"), TicketType.valueOf(r.getString("type")),
                            person,
                            new User(user.getString("login"), user.getString("password"))
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при чтении из базы данных.");
        }
        catch (NullPointerException e){
            System.out.println("Ошибка при чтении из базы данных!!!");
            e.printStackTrace();
        }
        return list;
    }

    public static boolean checkLoginAccuracy(User user){
        PreparedStatement st;
        try {
            st = connection.prepareStatement("select * from users where login=? and password=?");
            st.setString(1, user.getLogin());
            st.setString(2, user.getHash());
            ResultSet res = st.executeQuery();
            int count = 0;
            while (res.next()){
                ++count;
            }
            if(count == 1) return true;
        }
        catch (SQLException e) {
        }
        return false;
    }
}

