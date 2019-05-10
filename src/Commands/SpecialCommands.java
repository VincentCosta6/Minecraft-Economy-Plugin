package Commands;

import ServerData.init;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SpecialCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            
            Player player = (Player) sender;
            Location loc = player.getLocation();
            
            if(!player.isOp()) return false;
                
            World world = Bukkit.getWorlds().get(0);
            
            int radius = Integer.parseInt(args[0]);

            for(double X = -radius; X < radius; X++ )
                for(double Y = -radius; Y < radius; Y++ )
                    for(double Z = -radius; Z < radius; Z++ )
                        if(Math.sqrt((X * X) + (Y * Y) + (Z * Z)) <= radius) {
                            Block block = world.getBlockAt(loc.getBlockX() + (int) X, loc.getBlockY() + (int) Y, loc.getBlockZ() + (int) Z);
                            if(block.getType() != Material.BEDROCK)
                                block.setType(Material.AIR);
                        }
            
        }
        else {
            for(Entity t: Bukkit.getWorlds().get(0).getEntities()) {
                if(t.getName().contentEquals("Wight")) {
                    t.remove();
                }
                else if(t.getName().contentEquals("Night King")) {
                    t.remove();
                }
            }
        }
        
        return true;
    }
    
    public static void addChunkToMap(Chunk l, String faction) {
        String chunkData = l.getX() + "," + l.getZ();
        
        int startX = l.getX() * 16;
        int startZ = l.getZ() * 16;
        
        int endX = startX + 16;
        int endZ = startZ + 16;
        
        String color = null;
        
        switch(faction) {
            case "Reach":
                color = "00FF00";
                break;
            case "House Stark":
                color = "FF0000";
                break;
            case "The Pinheads":
                color = "ffa500";
                break;
            default:
                color = "ff0000";
                break;
        }
        
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker addcorner " + startX + " " + 0 + " " + startZ + " world");
        
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker addcorner " + endX + " " + 0 + " " + endZ + " world");
        
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker addarea " + faction + " color:" + color + " fillcolor:" + color + " opacity:1.0 fillopacity:0.3 id:" + chunkData);
        
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker clearcorners");
    }
    
}
