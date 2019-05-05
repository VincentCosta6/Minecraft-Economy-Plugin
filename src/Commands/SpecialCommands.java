package Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpecialCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            
            Player player = (Player) sender;
            Location loc = player.getLocation();
            
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
        
        return true;
    }
}
