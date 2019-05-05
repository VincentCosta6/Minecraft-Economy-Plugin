package Events;

import ServerData.init;
import com.connorlinfoot.titleapi.TitleAPI;
import static com.mongodb.client.model.Filters.eq;
import java.util.UUID;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_13_R2.PacketPlayOutTitle.EnumTitleAction;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;

import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class PlayerServerEvents implements Listener {
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        
        Document doc = init.dbStore.users.find(eq("uuid", uuid.toString())).first();
        
        if(doc == null) {
            Document newPlayer = new Document("uuid", uuid.toString())
                                      .append("username", event.getPlayer().getDisplayName())
                                      .append("money", 0)
                                      .append("land", new Document())
                                      .append("rank", "none")
                                      .append("faction", "none");
            init.dbStore.users.insertOne(newPlayer);
            
            TitleAPI.sendTitle(event.getPlayer(), 1, 100, 20, ChatColor.GOLD + "Welcome!");
            
            doc = newPlayer;
        }
        else {
            TitleAPI.sendTitle(event.getPlayer(), 1, 80, 20, ChatColor.GOLD + "Welcome back!", ChatColor.WHITE + "Your faction is " + ChatColor.RED + doc.get("faction"));
            event.getPlayer().setDisplayName("[" + doc.get("faction") + "]: " + event.getPlayer().getDisplayName() + "[" + doc.get("rank") + "]");
        }
        
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        
        Objective balance = board.registerNewObjective("balance", "dummy");
        balance.setDisplaySlot(DisplaySlot.SIDEBAR);
        balance.setDisplayName("Stats: "); 
        
        Score score = balance.getScore(ChatColor.GREEN + "$");
        
        try {
            double money = (double) doc.get("money");
            score.setScore((int) money); 
        }
        catch(Exception e) {
            int money = (int) doc.get("money");
            score.setScore(money); 
        }
        
        event.getPlayer().setScoreboard(board);
        
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        
    }
    
}