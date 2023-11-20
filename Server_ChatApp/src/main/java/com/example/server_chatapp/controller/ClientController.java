package com.example.server_chatapp.controller;


import com.example.server_chatapp.DAO.ClientDAO;
import com.example.server_chatapp.model.Client;

public class ClientController {
//    public void showClients(){
//        ArrayList<Client> clients = new ArrayList<Client>();
//        clients = ClientDAO.getClients();
//        for (Client client:clients) {
//            System.out.printf("%-10s%-20s%-20s%-20s%-30s%-5s",client.getId(), client.getName(), client.getUsername(), client.getPassword(), client.getEmail(), client.isLogin());
//            System.out.println();
//        }
//    }
    public static String Login(String username, String password){
        String result = ClientDAO.Login(username, password);
        if(result.equals("Login-fail")){
            return "Login-fail";
        } else if (result.equals("Password_fail")) {
            return "Password_fail";
        } else{
            // Client client = ClientDAO.getClient(result);
            // System.out.println(result);
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
        System.out.println(client.getUsername() + " " + client.getEmail());

        int numUser = Integer.parseInt(id_last.substring(2));
        String ID_newUser = "US";
        numUser++;
        if(numUser < 10) ID_newUser += "0" + numUser;
        else ID_newUser += numUser;
        client.setId(ID_newUser);

        boolean result = ClientDAO.SignUp(client);
        return "Sign up success";
    }
    public static boolean checkClientIsLogin(String idUser){
        return ClientDAO.CheckLogin(idUser);
    }
    public static void Logout(String iduser){
        ClientDAO.Logout(iduser);
    }
    public void showUser(){
        String id_user = "US01";
        Client client = ClientDAO.getClient(id_user);
        System.out.printf("%-10s%-20s%-20s%-20s%-30s%-5s",client.getId(), client.getName(), client.getUsername(), client.getPassword(), client.getEmail(), client.isLogin());
    }
}
