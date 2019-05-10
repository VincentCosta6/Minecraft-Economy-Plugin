package Entry;

import Commands.AdminCommands;
import Commands.FactionCommands;
import Commands.ShopCommands;
import Commands.SpecialCommands;
import static Commands.SpecialCommands.addChunkToMap;
import org.bukkit.plugin.java.JavaPlugin;

import Events.*;
import ServerData.DatabaseStore;
import ServerData.WaveData;
import ServerData.WinterArrived;
import ServerData.init;
import Winter.Logic;
import com.mongodb.client.FindIterable;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class Entry extends JavaPlugin {
    
    public static Logic winter;
    public List<List> waves = new ArrayList();
    
	@Override
    public void onEnable() {
        registerEvents();
        registerCommands();

        ServerData.init.Init();
        
        winter = new Logic(this);
        
        winter.runTaskAsynchronously(this);
        
        setAreasOnMap();
        
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Economy Plugin] Added areas to dynmap");
        
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Economy Plugin] Plugin successfully started");
    }
   
    @Override
    public void onDisable() {
       DatabaseStore.disconnect();
    }
    
    public void registerEvents() {
    	getServer().getPluginManager().registerEvents(new BlockEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerServerEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerMovementEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractionEvents(), this);
        getServer().getPluginManager().registerEvents(new MobSpawnEvents(), this);
        getServer().getPluginManager().registerEvents(new WeatherEvents(), this);
    }
    
    public void spawnEntites(List<WaveData> w) {
        SpawnAsyncEntity e = new SpawnAsyncEntity(w);
        
        e.runTask(this);
    }
    
    public void winterArrived() {
        WinterArrived arrival = new WinterArrived();
        
        arrival.runTask(this);
    }
    
    public void registerCommands() {
        this.getCommand("bomb").setExecutor(new SpecialCommands());
        this.getCommand("f").setExecutor(new FactionCommands());
        this.getCommand("sell").setExecutor(new ShopCommands());
        this.getCommand("add").setExecutor(new AdminCommands());
    }
    
    public void setAreasOnMap() {
        FindIterable<Document> iterable = init.dbStore.properties.find();
        int id = 0;
        for(Document d: iterable) {
            String chunkData = d.get("chunk").toString();

            String[] parts = chunkData.split(",");

            int chunkX = Integer.parseInt(parts[0]);
            int chunkZ = Integer.parseInt(parts[1]);

            Chunk chunk = Bukkit.getWorlds().get(0).getChunkAt(chunkX, chunkZ);
            
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker deletearea id:" + chunkData);

            addChunkToMap(chunk, d.get("faction").toString());

            id++;
        }
    }
}