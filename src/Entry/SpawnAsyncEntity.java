/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entry;

import ServerData.WaveData;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author costa
 */
public class SpawnAsyncEntity extends BukkitRunnable {
    List<WaveData> list;
    
    public SpawnAsyncEntity(List<WaveData> list) {
        this.list = list;
    }

    @Override
    public void run() {
        
        Player p = null;
        
        for(Player o: Bukkit.getOnlinePlayers()) {
            p = o;
            break;
        }
        
        for(WaveData d: list) {
            Entity t = Bukkit.getWorlds().get(0).spawnEntity(d.spawnLocation, d.type);
            
            d.instance = t;

            t.setCustomName(d.type == EntityType.WITHER ? "Night King" : "Wight");
            
            t.setSilent(true);
            t.setCustomNameVisible(true);
            
            
            Mob m = (Mob) t;
            
            if(m.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null)
                m.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(16.0);
            if(m.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null)
                m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);
            
            m.setTarget(p);
        }
        
    }
}
