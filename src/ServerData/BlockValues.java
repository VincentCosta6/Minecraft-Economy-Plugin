package ServerData;

import java.io.*;
import java.util.*;

import org.bukkit.*;
import org.bukkit.block.Block;


public class BlockValues {
	
	public static HashMap<String, Float> blocks;
	
	public static Scanner scan;
	
	public BlockValues(String path) throws FileNotFoundException {
		blocks = new HashMap<String, Float>();
		
		scan = new Scanner(new File(path));
		String line;
		
		while(scan.hasNextLine()) {
                    line = scan.nextLine();

                    String[] split = line.split(";"); // Format  id:sub_id;value

                    if(split.length != 2) System.exit(1);

                    blocks.put(split[0], Float.parseFloat(split[1]));
		}

		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Economy Plugin] Loaded block values");
		
	}
	
	public String extractKeyFromBlock(Block block) {
            return block.getBlockData().getAsString().substring(10); // Remove minecraft: from the block data string
	}
	
	public HashMap<String, Float> getHashMap() {
            return this.blocks;
	}
}
