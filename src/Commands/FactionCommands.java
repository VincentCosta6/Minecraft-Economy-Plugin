/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Commands;

import Events.PlayerMovementEvents;
import ServerData.LocationInfo;
import ServerData.init;
import com.mongodb.client.FindIterable;
import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author costa
 */
public class FactionCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        
        Player player = (Player) sender;
        Chunk playerChunk = player.getLocation().getChunk();
        
        if(args == null || args.length == 0) {
            player.sendMessage("You must pass in an argument!");
            return false;
        }
        else if(args[0].contentEquals("claim")) {
            if(args.length == 1)
                return claimOne(player, playerChunk);
        }
        else if(args[0].contentEquals("land")) {
            if(args.length == 1)
                return landSize(player);
        }
        
        
        return false;
    }
    
    public boolean claimOne(Player player, Chunk playerChunk) {
        String parsedValue = playerChunk.getX() + "," + playerChunk.getZ();
            
            Document property = init.dbStore.properties.find(eq("chunk", parsedValue)).first();
            Document user = init.dbStore.users.find(eq("uuid", player.getUniqueId().toString())).first();
            
            String faction = user.get("faction").toString();
            
            if(faction.contentEquals("none")) {
                player.sendMessage(ChatColor.RED + "You are not in a faction, you cannot claim land!");
                return false;
            }
            
            if(property != null) {
                
                if(property.get("faction").toString().contentEquals(user.get("faction").toString()))
                    player.sendMessage(ChatColor.RED + "This land already belongs to your faction!");
                else    
                    player.sendMessage(ChatColor.RED + "This land belongs to " + ChatColor.AQUA + property.get("faction").toString());
                
                return false;
            }
            
            Document newProperty = new Document("chunk", parsedValue).append("faction", faction);
            
            init.dbStore.properties.insertOne(newProperty);
            
            player.sendMessage(ChatColor.GREEN + "You have claimed chunk " + parsedValue);
            return true;
    }
    
    public boolean landSize(Player player) {
        LocationInfo info = (LocationInfo) PlayerMovementEvents.locations.get(player);
        
        if(info == null) {
            return false;
        }
        
        String faction = info.factionName;
        
        int size = 0;
        FindIterable<Document> iterable = init.dbStore.properties.find(eq("faction", faction));
        for(Document d: iterable) {
            size++;
        }
        
        player.sendMessage(ChatColor.AQUA + faction + ChatColor.WHITE + " has " + size + " chunks of land");
        
        return true;
    }
}


