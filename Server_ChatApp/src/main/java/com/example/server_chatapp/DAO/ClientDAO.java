package com.example.server_chatapp.DAO;


import com.example.server_chatapp.model.Client;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ClientDAO extends connectMySQL{
    public static ArrayList<Client> getClients() {
        ArrayList<Client> clients = new ArrayList<Client>();
        try {
            Connection conn = connectSQL();
            var sql = "select * FROM user";
            var statement = conn.prepareStatement(sql);
            var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("ID_user");
                String name = resultSet.getString("Name");
                String username = resultSet.getString("username");
                String password1 = resultSet.getString("password");
                String email = resultSet.getString("email");
                boolean isLogin = resultSet.getBoolean("isLogin");
                Client clientt = new Client(id, name, username, password1, email, isLogin);
                clients.add(clientt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clients;
    }

    public static Client getClient(String ID_user) {
        Client client;
        try {
            Connection conn = connectSQL();

            String sql = "SELECT * FROM user WHERE ID_user= '" + ID_user + "'";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            String id="", name="", username="", password = "", email="";
            boolean isLogin=false;
            while (resultSet.next()){
                id = resultSet.getString("ID_user");
                name = resultSet.getString("Name");
                username = resultSet.getString("username");
                password = resultSet.getString("password");
                email = resultSet.getString("email");
                isLogin = resultSet.getBoolean("isLogin");
            }
            client = new Client(id, name, username, password, email, isLogin);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return client;
    }
    public static String Login(String user, String pass) {
        try {
            Connection conn = connectSQL();
            String sql = "SELECT * FROM user WHERE username='" + user + "'";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String id = resultSet.getString("ID_user");
                String password = resultSet.getString("password");

                if(password.equals(pass)){
                    return id;
                }
                else return "Password_fail";
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "Login-fail";
    }
    public static boolean SignUp(Client client){
        try{
            Connection conn = connectSQL();
            String sql = "INSERT INTO user(ID_user, Name, username, password, email, isLogin) VALUES ('" + client.getId() + "'" +
                                                                                                        ",'" + client.getName() + "'" +
                                                                                                        ",'" + client.getUsername() + "'" +
                                                                                                        ",'" + client.getPassword() + "'" +
                                                                                                        ",'" + client.getEmail() + "'" +
                                                                                                        ",'" + 0 + "')";
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean CheckLogin(String idUser){
        try{
            Connection conn = connectSQL();
            String sql = "SELECT isLogin FROM user WHERE ID_user='" + idUser + "'";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                boolean isLogin = resultSet.getBoolean("isLogin");
                if(!isLogin){
                    sql = "UPDATE user " +
                            "SET isLogin = true " +
                            "WHERE ID_user = '" + idUser + "'";
                    statement = conn.createStatement();
                    statement.executeUpdate(sql);
                    return false;
                }
                else  return true;
            }
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return true;
    }
    public static void Logout(String idUser){
        try{
            Connection conn = connectSQL();
            String sql = "SELECT isLogin FROM user WHERE ID_user='" + idUser + "'";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                boolean isLogin = resultSet.getBoolean("isLogin");
                if(isLogin){
                    sql = "UPDATE user " +
                            "SET isLogin = false " +
                            "WHERE ID_user = '" + idUser + "'";
                    statement = conn.createStatement();
                    statement.executeUpdate(sql);
                }
            }
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    // đăng kí
}