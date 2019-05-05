/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerData;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;

/**
 *
 * @author costa
 */
public class PublicHashMaps {
    public static HashMap<UUID, String> playerFactions = new HashMap(); // UUID of player and their faction name
    public static HashMap<String, String> landOwnership = new HashMap(); // chunk x,z string and string of faction name
    
    public void factionChange(Player p, String factionName) {
        UUID uuid = p.getUniqueId();
        
        if(playerFactions.containsKey(uuid)) {
            playerFactions.replace(uuid, factionName);
        }
        else {
            playerFactions.put(uuid, factionName);
        }
    }
    
    public void landPlotChange(String coor, String faction) {
        if(landOwnership.containsKey(coor)) {
            landOwnership.replace(coor, faction);
        }
        else {
            landOwnership.put(coor, faction);
        }
    }
}
