package Commands;

import ServerData.BlockValues;
import com.google.common.io.Files;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmnd, String string, String[] args) {
        if(!(sender instanceof Player)) return false;
        
        Player player = (Player) sender;
        
        if(!player.isOp()) {
            player.sendMessage(ChatColor.RED + "You do not have permission to perform this command");
            return false;
        }
        
        if(args == null || args.length != 2) {
            player.sendMessage("You must pass in an argument!");
            return false;
        }
        else {
            BlockValues.blocks.put(args[0], Float.parseFloat(args[1]));
            try {
                addToFile(args[0], Float.parseFloat(args[1]));
                return true;
            } catch (IOException ex) {
                
            }
            
        }
        
        
        return true;
    }
    
    public void addToFile(String key, float val) throws IOException {
        FileWriter fw = null;
        
        try {
            String filename= "./plugins/Economy/block-values.txt";
            fw = new FileWriter(filename,true);
            fw.write("\n" + key + ";" + val);
        } 
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } 
        catch (IOException ex) {
            Logger.getLogger(AdminCommands.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            fw.close();
        }
        
    }
    
}
