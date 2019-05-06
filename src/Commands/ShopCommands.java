package Commands;

import ServerData.BlockValues;
import ServerData.init;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;
import java.util.UUID;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

public class ShopCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        
        Player player = (Player) sender;
        
        if(args == null || args.length == 0) {
            
            
            return false;
        }
        else if(args[0].contentEquals("hand")) {
            ItemStack stack = player.getInventory().getItemInMainHand();
            
            String blockID = stack.getType().name();
            
            Float value = BlockValues.blocks.get(blockID);
            
            if(value == null) {
                player.sendMessage(ChatColor.RED + "You cant sell this item!");
                return false;
            }
            
            value *= stack.getAmount();
            
            UUID uuid = player.getUniqueId();
        
            Document playerDoc = init.dbStore.users.find(eq("uuid", uuid.toString())).first();

            init.dbStore.users.updateOne(playerDoc, inc("money", value));

            Objective objective = player.getScoreboard().getObjective("balance");

            Score score = objective.getScore(ChatColor.GREEN + "$");

            int money = score.getScore() + value.intValue();
            score.setScore(money);
            
            player.getInventory().getItemInMainHand().setType(Material.AIR);
            
            player.sendMessage("You sold " + ChatColor.YELLOW + blockID.toLowerCase() + " for " + ChatColor.GREEN + "$" + value);
            
            return true;
        }
        
        return false;
    }
}
