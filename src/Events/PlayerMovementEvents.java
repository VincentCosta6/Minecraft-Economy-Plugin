package Events;

import ServerData.LocationInfo;
import ServerData.init;
import com.connorlinfoot.titleapi.TitleAPI;
import static com.mongodb.client.model.Filters.eq;
import java.util.HashMap;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovementEvents implements Listener {
    public static HashMap<Player, LocationInfo> locations = new HashMap();
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Chunk locChunk = event.getTo().getChunk();
        
        if(!locations.containsKey(event.getPlayer())) {
            String parsedValue = locChunk.getX() + "," + locChunk.getZ();
            
            Document property = init.dbStore.properties.find(eq("chunk", parsedValue)).first();
            
            String faction = property == null ? "none" : property.get("faction").toString();
            
            locations.put(event.getPlayer(), new LocationInfo(locChunk, faction));
            
            TitleAPI.sendTitle(event.getPlayer(), 5, 20, 20, ChatColor.GOLD + faction);
            return;
        }
        
        LocationInfo hashedInfo = (LocationInfo) locations.get(event.getPlayer());
        
        if(!locChunk.equals(hashedInfo.chunk)) {
            // Chunk was changed
            
            String parsedValue = locChunk.getX() + "," + locChunk.getZ();
            String hashed = hashedInfo.chunk.getX() + "," + hashedInfo.chunk.getZ();
            
            Document property = init.dbStore.properties.find(eq("chunk", parsedValue)).first();
            
            if(property == null) {
                // If claim is set to auto claim the land
            }
            
            String print = property == null ? "none" : property.get("faction").toString();
            
            if(!print.contentEquals(hashedInfo.factionName)) {
                hashedInfo.factionName = print;
                
                TitleAPI.sendTitle(event.getPlayer(), 5, 20, 20, ChatColor.GOLD + print);
            }
            
            hashedInfo.chunk = locChunk;
        }
        
        
    }
}

