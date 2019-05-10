package Events;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.*;
import org.bukkit.event.block.*;

import ServerData.*;
import com.mongodb.BasicDBObject;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;
import com.mongodb.client.result.UpdateResult;
import java.util.UUID;
import org.bson.Document;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class BlockEvents implements Listener {
	
    HashMap<String, Type> timings = new HashMap();
	
    @EventHandler
    public void onBlockbreak(BlockBreakEvent event) {
        
        if(event.getPlayer().isOp()) return;
        
        if(!checkUserCanMine(event.getPlayer(), event.getBlock())) {
            event.setCancelled(true);
            return;
        }
        
        //giveMoneyForMining(event);
    }
    
    boolean checkUserCanMine(Player p, Block b) {
        Chunk blockChunk = b.getLocation().getChunk();
        String coor = blockChunk.getX() + "," + blockChunk.getZ();
        
        String userFaction = init.dbStore.users.find(eq("uuid", p.getUniqueId().toString())).first().get("faction").toString();
        String chunkFaction;
        
        Document chunk = init.dbStore.properties.find(eq("chunk", coor)).first();
        if(chunk == null) {
            return true;
        }
        else {
            chunkFaction = chunk.get("faction").toString();
        }
        
        if(chunkFaction == null || chunkFaction.contentEquals("none") || chunkFaction.contentEquals(userFaction)) {
            
            return true;
        }
        else if(true) {
            
            // Check for war
            
            p.sendMessage("This land belongs to " + ChatColor.RED + chunkFaction + ChatColor.WHITE + "! Declare war to mine or place blocks");
            return false;
        }
        
        return false;
    }
    
    void giveMoneyForMining(BlockBreakEvent event) {
        String key = init.blockValues.extractKeyFromBlock(event.getBlock());

        Float value = init.blockValues.getHashMap().get(key);

        if(value == null) return;

        determineMessageString(event, key, value);

        UUID uuid = event.getPlayer().getUniqueId();
        
        Document player = init.dbStore.users.find(eq("uuid", uuid.toString())).first();
        
        init.dbStore.users.updateOne(player, inc("money", value));
        
        Objective objective = event.getPlayer().getScoreboard().getObjective("balance");
        
        Score score = objective.getScore(ChatColor.GREEN + "$");
        
        int money = score.getScore() + value.intValue();
        score.setScore(money);
    }
	
    void determineMessageString(BlockBreakEvent event, String key, Float value) {
        Type t = timings.get(event.getPlayer().getUniqueId().toString());
        long currentTime = System.currentTimeMillis();
        boolean same = false;

        if(t == null) {
                timings.put(event.getPlayer().getUniqueId().toString(), new Type(key, currentTime));
                printNewBlockMined(event, value);
        }
        else if((same = t.type.contentEquals(key)) && t.tickTime >= currentTime - 3000) {
                t.tickTime = currentTime;
                printBlockMined(event, value);
        }
        else if(!same) {
                t.type = key;
                t.tickTime = currentTime;
                printNewBlockMined(event, value);
        }
        else if(t.tickTime <= currentTime - 3000) {
                t.tickTime = currentTime;
                printNewBlockMined(event, value);
        }
    }
	
    void printNewBlockMined(BlockBreakEvent event, float value) {
        event.getPlayer().sendMessage(ChatColor.WHITE + "You mined " + 
                                    ChatColor.YELLOW + event.getBlock().getType().toString().toLowerCase().replace("_"," ") + 
                                    ChatColor.WHITE + " for" + 
                                    ChatColor.GREEN + " $" + String.format("%.02f", value));
    }
    void printBlockMined(BlockBreakEvent event, float value) {
        event.getPlayer().sendMessage(ChatColor.GREEN + "+$" + String.format("%.02f", value));
    }
}

class Type {
	String type;
	long tickTime;
	
	public Type(String type, long tickTime) {
		this.type = type;
		this.tickTime = tickTime;
	}
}