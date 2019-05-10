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
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
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
        
        if(event.getPlayer().isOp()) return;
        
        String blockType = event.getClickedBlock().getType().name();
        
        if(BlockValues.blacklistedInteractions.containsKey(blockType)) {
            if(!isAllowed(event.getPlayer(), event.getClickedBlock())) {
                event.getPlayer().sendMessage(ChatColor.RED + "You arent allowed to interact on this property!");
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        
        if(!isAllowed(event.getPlayer(), event.getBlock())) {
            event.getPlayer().sendMessage(ChatColor.RED + "You arent allowed to build on this property");
            event.setCancelled(true);
        }
    }
    
    public boolean isAllowed(Player p, Block b) {
        
        Chunk chunk = b.getLocation().getChunk();
        String chunkCoor = chunk.getX() + "," + chunk.getZ();
                
        String userFaction = init.dbStore.users.find(eq("uuid", p.getUniqueId().toString())).first().get("faction").toString();
        String chunkFaction;
        
        Document chunkDoc = init.dbStore.properties.find(eq("chunk", chunkCoor)).first();
        
        if(chunkDoc != null) {
            chunkFaction = chunkDoc.get("faction").toString();
        }
        else return true;
        
        return userFaction.contentEquals(chunkFaction);
    }
}
