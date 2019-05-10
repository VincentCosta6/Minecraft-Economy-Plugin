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
import static com.mongodb.client.model.Updates.inc;
import java.util.UUID;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

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
            String[] messages = {
                ChatColor.AQUA + "/f claim" + ChatColor.WHITE + " claims the chunk you are on for $100",
                ChatColor.AQUA + "/f land" + ChatColor.WHITE + " displays how many chunks your faction owns",
                ChatColor.AQUA + "/f send <player> <amount>" + ChatColor.WHITE + " send the amount to the target player",
                ChatColor.AQUA + "/f update" + ChatColor.WHITE + " manually update your balance on your HUD",
            };
            
            player.sendMessage(messages);
            
            return true;
        }
        else if(args[0].contentEquals("claim")) {
            
            if(args.length == 1)
                return claimOne(player, playerChunk);
            
        }
        else if(args[0].contentEquals("land")) {
            
            if(args.length == 1)
                return landSize(player);
            
        }
        else if(args[0].contentEquals("send")) {
            
            if(args.length != 3) {
                player.sendMessage(ChatColor.RED + "Usage /f send <name> <amount>");
                return false;
            }
            
            return transferMoney(player, args);
        }
        else if(args[0].contentEquals("update")) {
            
            return updateScoreboard(player);
            
        }
        
        
        return false;
    }
    
    public boolean claimOne(Player player, Chunk playerChunk) {
        String parsedValue = playerChunk.getX() + "," + playerChunk.getZ();
            
            Document property = init.dbStore.properties.find(eq("chunk", parsedValue)).first();
            Document user = init.dbStore.users.find(eq("uuid", player.getUniqueId().toString())).first();
            
            if(user == null) return false;
            
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
            
            UUID uuid = player.getUniqueId();
        
            Document playerDoc = init.dbStore.users.find(eq("uuid", uuid.toString())).first();
            
            if(Float.parseFloat(playerDoc.get("money").toString()) < 100) {
                player.sendMessage(ChatColor.RED + "You dont have enough money to buy this chunk");
                return false;
            }

            init.dbStore.users.updateOne(playerDoc, inc("money", -100.0));

            updateScoreboard(player, 100, true);
            
            Document newProperty = new Document("chunk", parsedValue).append("faction", faction);
            
            init.dbStore.properties.insertOne(newProperty);
            
            player.sendMessage(ChatColor.GREEN + "You have claimed chunk " + parsedValue + " for $100");
            
            SpecialCommands.addChunkToMap(playerChunk, faction);
            
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
    
    public boolean transferMoney(Player player, String[] args) {
        String name = args[1];
        String amountStr = args[2];
        
        float amount = 0.0f;
        
        try {
            amount = Float.parseFloat(amountStr);
            if(amount < 0) {
                player.sendMessage(ChatColor.RED + "You cant send a negative number, if you didnt you probably sent a very large number and it overflowed back to negative");
                return false;
            }
            else if(amount == 0) {
                player.sendMessage(ChatColor.RED + "Amount cant be zero");
                return false;
            }
            else if(amount > 100000000) {
                player.sendMessage(ChatColor.RED + "cant send more than 100 million at a time");
                return false;
            }
            
            String uuid = player.getUniqueId().toString();
            
            Document playerDoc = init.dbStore.users.find(eq("uuid", uuid)).first();
            
            if(playerDoc == null) return false;
            
            if(Float.parseFloat(playerDoc.get("money").toString()) < amount) {
                player.sendMessage(ChatColor.RED + "You dont have enough money!");
                return false;
            }
            
            Document toDoc = init.dbStore.users.find(eq("username", name)).first();
            
            if(toDoc == null) {
                player.sendMessage(ChatColor.RED + "This player does not exist!");
                return false;
            }
            
            Player to = Bukkit.getPlayer(toDoc.get("username").toString());
            
            
            if(to != null)
                to.sendMessage(ChatColor.YELLOW + name + ChatColor.WHITE + " has sent you " + ChatColor.GREEN + "$" + amount);
            player.sendMessage("You sent " + ChatColor.YELLOW + toDoc.get("username") + ChatColor.GREEN + " $" + amount);
            
            Bukkit.getConsoleSender().sendMessage(name + " has sent " + toDoc.get("username") + " $" + amount);
            
            init.dbStore.users.updateOne(playerDoc, inc("money", -amount));
            init.dbStore.users.updateOne(toDoc, inc("money", amount));
            
            updateScoreboard(player, (int) amount, true);
            
            if(to != null)
                updateScoreboard(to, (int) amount, false);
            
            return true;
        }
        catch(NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Amount must be a valid number");
            return false;
        }
        
    }
    
    public boolean updateScoreboard(Player p) {
        Document playerDoc = init.dbStore.users.find(eq("uuid", p.getUniqueId().toString())).first();
        
        return updateScoreboard(p, (int) Float.parseFloat(playerDoc.get("money").toString()));
    }
    
    public boolean updateScoreboard(Player p, int newValue) {
        Objective objective = p.getScoreboard().getObjective("balance");

        Score score = objective.getScore(ChatColor.GREEN + "$");
        
        score.setScore(newValue);
        
        return true;
    }
    
    public boolean updateScoreboard(Player p, int amount, boolean subtract) {
        Objective objective = p.getScoreboard().getObjective("balance");

        Score score = objective.getScore(ChatColor.GREEN + "$");
        
        int money;
        
        if(subtract)
            money = score.getScore() - amount;
        else 
            money = score.getScore() + amount;
        
        score.setScore(money);
        
        return true;
    }
}


