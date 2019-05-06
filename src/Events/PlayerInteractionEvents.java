/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import ServerData.BlockValues;
import ServerData.init;
import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author costa PlayerInteractEvent
 */
public class PlayerInteractionEvents implements Listener {
    
    @EventHandler
    public void onInteraction(PlayerInteractEvent event) {
        
        if(!event.hasBlock()) return;
        
        String blockType = event.getClickedBlock().getType().name();
        
        Bukkit.broadcastMessage(blockType);
        
        if(BlockValues.blacklistedInteractions.containsKey(blockType)) {
            checkChunkFactionOwner(event);
        }
    }
    
    public void checkChunkFactionOwner(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        
        Chunk chunk = event.getClickedBlock().getLocation().getChunk();
        String chunkCoor = chunk.getX() + "," + chunk.getZ();
                
        String userFaction = init.dbStore.users.find(eq("uuid", p.getUniqueId().toString())).first().get("faction").toString();
        String chunkFaction;
        
        Document chunkDoc = init.dbStore.properties.find(eq("chunk", chunkCoor)).first();
        if(chunkDoc != null) {
            chunkFaction = chunkDoc.get("faction").toString();
        }
        else return;
        
        if(!userFaction.contentEquals(chunkFaction)) {
            p.sendMessage(ChatColor.RED + "You arent allowed to interact on this property");
            event.setCancelled(true);
        }
        
        
    }
}
