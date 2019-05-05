package Entry;

import Commands.FactionCommands;
import Commands.SpecialCommands;
import org.bukkit.plugin.java.JavaPlugin;

import Events.*;

public class Entry extends JavaPlugin {
	@Override
    public void onEnable() {
        registerEvents();
        registerCommands();

        ServerData.init.Init();
    }
   
    @Override
    public void onDisable() {
       
    }
    
    public void registerEvents() {
    	getServer().getPluginManager().registerEvents(new BlockEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerServerEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerMovementEvents(), this);
    }
    
    public void registerCommands() {
        this.getCommand("bomb").setExecutor(new SpecialCommands());
        this.getCommand("f").setExecutor(new FactionCommands());
    }
}