package com.example.server_chatapp.controller;


import com.example.server_chatapp.DAO.ClientDAO;
import com.example.server_chatapp.model.Client;

import java.util.ArrayList;

public class ClientController {
    public static String Login(String username, String password){
        String result = ClientDAO.Login(username, password);
        if(result.equals("Login-fail")){
            return "Login-fail";
        } else if (result.equals("Password_fail")) {
            return "Password_fail";
        } else{
            return result;
        }
    }
    public static String SignUp(Client client){
        String id_last = "";
        for (Client clientL : ClientDAO.getClients()){
            if(client.getUsername().equals(clientL.getUsername())){
                return "User name existed";
            }
            id_last = clientL.getId();
        }
        int numUser = Integer.parseInt(id_last.substring(2));
        String ID_newUser = "US";
        numUser++;
        if(numUser < 10) ID_newUser += "0" + numUser;
        else ID_newUser += numUser;
        client.setId(ID_newUser);

        boolean result = ClientDAO.SignUp(client);
        return "Sign up success";
    }
    public static ArrayList<Client> getClients(){
        return ClientDAO.getClients();
    }
    public static Client getClient(ArrayList<Client> clients, String id_user){
        for(Client client : clients) {
            if(client.getId().equals(id_user)) return client;
        }
        return null;
    }
    public static boolean checkClientIsLogin(String idUser){
        return ClientDAO.CheckLogin(idUser);
    }
    public static void Logout(String iduser){
        ClientDAO.Logout(iduser);
    }
}
