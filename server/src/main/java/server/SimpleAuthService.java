package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleAuthService implements AuthService {

    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement psInsert;
    private static String nickname;


//    private class UserData {
//        String login;
//        String password;
//        String nickname;
//
//        public UserData(String login, String password, String nickname) {
//            this.login = login;
//            this.password = password;
//            this.nickname = nickname;
//        }
//    }

//    private List<UserData> users;

    //    public SimpleAuthService() {
//        users = new ArrayList<>();
//        users.add(new UserData("qwe", "qwe", "qwe"));
//        users.add(new UserData("asd", "asd", "asd"));
//        users.add(new UserData("zxc", "zxc", "zxc"));
//        for (int i = 1; i < 10; i++) {
//            users.add(new UserData("login" + i, "pass" + i, "nick" + i));
//        }
//    }

    public static boolean changeNickname(String nickname, String nickname2) throws SQLException {

        ResultSet resultSet = stmt.executeQuery( "SELECT login, password, nickname FROM chatDB" );
        if (nickname2.equals( resultSet.getString( 1 ) )) {
            return false;
        }
        resultSet.close();
        String x = String.format( "UPDATE chatDB SET nickname = '%s' WHERE nickname = '%s'", nickname2, nickname );
        stmt.executeUpdate( x );

        return true;
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) throws SQLException {

        ResultSet resultSet = stmt.executeQuery( "SELECT login, password, nickname FROM chatDB" );
        while (resultSet.next()) {
            if (login.equals( resultSet.getString( 1 ) ) &&
                    password.equals( resultSet.getString( 2 ) ))
                return nickname = resultSet.getString( 3 );
        }
        resultSet.close();
        return null;
    }

    @Override
    public boolean registration(String login, String password, String nickname) throws SQLException {
        String x = String.format( "SELECT '%s', '%s', '%s' FROM chatDB", login, password, nickname );
        ResultSet resultSet = stmt.executeQuery( x );
        while (resultSet.next()) {
            if (login.equals( resultSet.getString( 1 ) ) &&
                    nickname.equals( resultSet.getString( 3 ) ))
                return false;
        }
        resultSet.close();
        x = String.format( "INSERT INTO chatDB (login, password, nickname) " +
                "VALUES ('%s', '%s', '%s')", login, password, nickname );
        stmt.executeUpdate( x );
        return true;
    }

    public SimpleAuthService() {
        try {
            connect();
            System.out.println( "connect DB" );
        } catch (Exception e) {
            e.printStackTrace();
        }
//        finally {
//            disconnect();
//            System.out.println( "disconnect DB :(" );
//        }
    }

    private static void disconnect() {
        try {
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void connect() throws ClassNotFoundException, SQLException {
        Class.forName( "org.sqlite.JDBC" );
        connection = DriverManager.getConnection( "jdbc:sqlite:main.db" );
        stmt = connection.createStatement();
    }
}
