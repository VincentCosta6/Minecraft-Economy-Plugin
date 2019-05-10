/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

/**
 *
 * @author costa
 */
public class MobSpawnEvents implements Listener {
	
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        //if(event.getSpawnReason() == SpawnReason.NATURAL)
            //event.setCancelled(true);
        
        if(event.getEntity().getAttribute(Attribute.GENERIC_FOLLOW_RANGE) != null) {
            event.getEntity().getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
        }
        
        event.getEntity().setRemoveWhenFarAway(true);
        
    }
    
    @EventHandler
    public void onTargetChange(EntityTargetEvent event) {
        if(event.getTarget() == null) return;
        
        if(event.getReason() == TargetReason.TARGET_ATTACKED_ENTITY || event.getReason() == TargetReason.TARGET_ATTACKED_NEARBY_ENTITY) {
            event.setCancelled(true);
        }
        
        if(event.getEntity().getLocation().distance(event.getTarget().getLocation()) > 32) {
            event.getEntity().teleport(event.getEntity());
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(event.getDroppedExp() <= 0) return;
        
        if(event.getEntityType() == EntityType.WITHER) {
            String name = event.getEntity().getCustomName();
            
            if(name == null) return;
            
            if(name.contentEquals("Night King")) {
                for(Entity e: Bukkit.getWorlds().get(0).getEntities()) {
                    if(e instanceof Mob) {
                        String mobName = e.getCustomName();
                        
                        if(mobName == null) continue;
                        
                        if(mobName.contentEquals("Wight")) {
                            e.remove();
                        }
                    }
                }
            }
            Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "THE NIGHT KING HAS BEEN SLAIN!! Winter is finally over!!");
            
            Entry.Entry.winter.isWinter = false;
            Entry.Entry.winter.winterArrivalTime = 999999999;
            Entry.Entry.winter.nightKingSlain = true;
            
            Bukkit.getWorlds().get(0).setThundering(false);
            Bukkit.getWorlds().get(0).setTime(6000);
        }
        
        
        
    }
}
