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
    
    public MongoCollection<Document> users, factions, properties;
	
    public DatabaseStore() throws UnknownHostException {
        MongoClient mongoClient = MongoClients.create();
        
        MongoDatabase database = mongoClient.getDatabase("minecraft");
        
        users = database.getCollection("users");
        factions = database.getCollection("factions");
        properties = database.getCollection("properties");
        
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Economy Plugin] Loaded user database");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Economy Plugin] Loaded faction database");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Economy Plugin] Loaded property database");
    }
}