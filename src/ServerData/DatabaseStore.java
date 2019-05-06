package ServerData;

import com.mongodb.DBCollection;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.net.UnknownHostException;
import org.bson.Document;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DatabaseStore {
    
    static MongoClient mongoClient;
    
    public MongoCollection<Document> users, factions, properties;
	
    public DatabaseStore() throws UnknownHostException {
        mongoClient = MongoClients.create();
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Economy Plugin] Connected to Mongo");
        
        MongoDatabase database = mongoClient.getDatabase("minecraft");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Economy Plugin] Loaded mongo minecraft database");
        
        users = database.getCollection("users");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Economy Plugin] Loaded user colection");
        
        factions = database.getCollection("factions");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Economy Plugin] Loaded faction colection");
        
        properties = database.getCollection("properties");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Economy Plugin] Loaded property colection");
        
        
        
    }
    
    public static void disconnect() {
        mongoClient.close();
    }
}