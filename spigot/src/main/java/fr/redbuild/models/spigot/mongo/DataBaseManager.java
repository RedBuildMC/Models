package fr.redbuild.models.spigot.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import fr.redbuild.models.spigot.logger.CtMsg;

import java.util.HashMap;
import java.util.Map;



public class DataBaseManager {

    private final Map<String,MongoClient> clients = new HashMap<>();


    public void connect(String name,String adresse,String user,String pwd,String authMechanism){
        clients.put(name,MongoClients.create("mongodb://" + user + ":" + pwd + "@" + adresse + "/?authMechanism=" + authMechanism));
        // System.out.println("Database Connected !");
        CtMsg.log("Database Connected !");
    }

    public void sha256Connect(String name,String adresse,String user,String pwd){
        connect(name,adresse,user,pwd,"SCRAM-SHA-256");
    }

    public void disconnect(String name){
        clients.remove(name);
        // System.out.println("Database Disconnected !");
        CtMsg.log("Database Disconnected !");
    }

    public MongoClient getClient(String clientName){
        return clients.get(clientName);
    }
}
