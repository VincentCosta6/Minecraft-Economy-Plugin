/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerData;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author costa
 */
public class WinterArrived extends BukkitRunnable {

    @Override
    public void run() {
        for(Entity e: Bukkit.getWorlds().get(0).getEntities()) {
            e.remove();
        }
    }
    
}
