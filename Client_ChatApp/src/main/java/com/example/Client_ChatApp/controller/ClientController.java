package com.example.Client_ChatApp.controller;

import com.example.Client_ChatApp.model.Client;

import java.util.ArrayList;

public class ClientController {
    public static Client getClientById(ArrayList<Client> clients, String id){
        for(Client client : clients){
            if(client.getId().equals(id)){
                System.out.println(client.getId());
                return client;
            }
        }
        return null;
    }

    public static String getLastName(String name){
        String Lastname = "";
        for(String e : name.split(" ")) Lastname = e;
        return Lastname;
    }
}
