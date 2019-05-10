/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 *
 * @author costa
 */
public class WeatherEvents implements Listener {
	
	
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if((Bukkit.getWorlds().get(0).isThundering()) && !Entry.Entry.winter.isWinter) return;
        
        if(!Entry.Entry.winter.isWinter && (!Bukkit.getWorlds().get(0).isThundering())) event.setCancelled(true);
    }
    
}
