package Winter;

import Entry.Entry;
import ServerData.WaveData;
import com.connorlinfoot.titleapi.TitleAPI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Logic extends BukkitRunnable {

    public boolean isWinter = false;
    
    public boolean nightKingSlain = false;
    
    public boolean first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth;
    
    public List<Monster> mons = new ArrayList();
    
    public World world;
    
    Thread thread;
    
    public long winterArrivalTime = 20 * 24000;
    
    public List<List<WaveData>> mobs = new ArrayList();
    
    public Entry e;
    
    public Logic(Entry e) {
        super();
        
        this.e = e;
        
        thread = new Thread();
        world = Bukkit.getWorlds().get(0);
    }
    
    EntityType[] chooseFrom = { EntityType.WITCH, EntityType.GHAST, EntityType.CAVE_SPIDER, EntityType.CREEPER, EntityType.ENDERMAN, EntityType.SKELETON, EntityType.VEX, EntityType.WITHER_SKELETON, EntityType.ZOMBIE, EntityType.PHANTOM, EntityType.DROWNED };
    
    @Override
    public void run() {
        world.setTime(0);
        while(true) {
            if(nightKingSlain) return;
            
            if(!isWinter) {
                // Check if it should be winter
                try {
                    Thread.sleep(1000);
                    if(Bukkit.getOnlinePlayers().isEmpty()) continue;
                    
                    if(world.getFullTime() >= winterArrivalTime) {
                        isWinter = true;
                        world.setThundering(true);
                        world.setTime(18000);
                        
                        e.winterArrived();
                        
                        int random = (int) (Math.random() * Bukkit.getOnlinePlayers().size());
                        
                        Player p = null;
                        
                        int count = 0;
                        for(Player o: Bukkit.getOnlinePlayers()) {
                            if(count == random) {
                                p = o;
                            }
                            
                            TitleAPI.sendTitle(o, 20, 80, 20, ChatColor.RED + "Winter has arrived!!", ChatColor.DARK_RED + "All animals have died to the intense cold");
                            
                            o.playSound(o.getLocation(), Sound.AMBIENT_CAVE, 50, 300);
                            
                            count++;
                        }
                        
                        List<WaveData> wave = new ArrayList();
                        
                        WaveData w = new WaveData(EntityType.WITHER, new Location(Bukkit.getWorlds().get(0), 100f, 80f, -500f), p);
                        
                        wave.add(w);
                        
                        e.spawnEntites(wave);
                    }
                    else {
                        world.setStorm(false);
                        
                    }
                    
                    if(!first && winterArrivalTime - world.getFullTime() <= 500) {
                        Bukkit.broadcastMessage(ChatColor.UNDERLINE + "" + ChatColor.DARK_RED + "An enourmous figure arises from the storm and is approaching rapidly...");
                        first = true;
                    }
                    else if(!second && winterArrivalTime - world.getFullTime() <= 1000) {
                        Bukkit.broadcastMessage(ChatColor.UNDERLINE + "" + ChatColor.DARK_RED + "You feel a cold shiver down your spine");
                        second = true;
                    }
                    else if(!third && winterArrivalTime - world.getFullTime() <= 1500) {
                        Bukkit.broadcastMessage(ChatColor.UNDERLINE + "" + ChatColor.DARK_RED + "A mysterious shadow begins to form in the storm");
                        third = true;
                    }
                    else if(!fourth && winterArrivalTime - world.getFullTime() <= 3000) {
                        Bukkit.broadcastMessage(ChatColor.UNDERLINE + "" + ChatColor.DARK_RED + "Out on the horizon you can see a massive storm approaching");
                        fourth = true;
                    }
                    else if(!fifth && winterArrivalTime - world.getFullTime() <= 6000) {
                        Bukkit.broadcastMessage(ChatColor.UNDERLINE + "" + ChatColor.DARK_RED + "You start struggling to breathe as you lungs feel like they are going to collapse");
                        fifth = true;
                    }
                    else if(!sixth && winterArrivalTime - world.getFullTime() <= 24000) {
                        Bukkit.broadcastMessage(ChatColor.UNDERLINE + "" + ChatColor.DARK_RED + "The air around you suddenly becomes ice cold");
                        sixth = true;
                    }
                    else if(!seventh && winterArrivalTime - world.getFullTime() <= 48000) {
                        Bukkit.broadcastMessage(ChatColor.UNDERLINE + "" + ChatColor.DARK_RED + "It starts to become a little chilly");
                        seventh = true;
                    }
                    else if(!eighth && winterArrivalTime - world.getFullTime() <= 56000) {
                        Bukkit.broadcastMessage(ChatColor.UNDERLINE + "" + ChatColor.DARK_RED + "You notice the air seems....different, than usual");
                        eighth = true;
                    }
                    else if(!ninth && winterArrivalTime - world.getFullTime() <= 80000) {
                        Bukkit.broadcastMessage(ChatColor.UNDERLINE + "" + ChatColor.DARK_RED + "It hasnt rained in a while...");
                        ninth = true;
                    }
                    else if(!tenth && winterArrivalTime - world.getFullTime() <= 100000) {
                        Bukkit.broadcastMessage(ChatColor.UNDERLINE + "" + ChatColor.DARK_RED + "The sun's warm rays cast onto your face");
                        tenth = true;
                    }
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            }
            else {
                try {
                    Thread.sleep(1000 * 10);
                    world.setTime(18000);
                    world.setThundering(true);
                    mobLogic();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    }
    int playerToTarget = 0;
    public void mobLogic() {
        if(Bukkit.getOnlinePlayers().isEmpty()) return;
        
        int totalMobs = 0;
        
        for(List<WaveData> l: Entry.winter.mobs) {
            boolean oneAlive = false;
            for(int j = 0; j < l.size(); j++) {
                
                WaveData d = l.get(j); 
                
                if(d.instance == null) continue;
                
                if(!d.instance.isDead()) {
                    totalMobs++;
                    oneAlive = true;
                    
                    Mob m = (Mob) d.instance;
                    
                    m.setTarget(d.target);
                }
                else {
                    //l.remove(d);
                    //j--;
                }
            }
            
            //if(!oneAlive) Entry.winter.mobs.remove(l);
        }
        
        if(totalMobs > 35) return;
        
        Bukkit.broadcastMessage(totalMobs + "");
        
        playerToTarget = ++playerToTarget % Bukkit.getOnlinePlayers().size();
        
        Player p = null;
        
        int count = 0;
        for(Player o: Bukkit.getOnlinePlayers()) {
            if(count == playerToTarget) {
                p = o;
                break;
            }
            count++;
        }
        
        if(p == null) {
            Bukkit.broadcastMessage("Whoops");
            return;
        }
        
        int enemies = (int) (Math.random() * 8 + 6);
        
        List<WaveData> wave = new ArrayList();
        
        for(int i = 0; i < enemies; i++) {
            WaveData w = new WaveData(chooseRandom(), getRandomLocNear(p.getLocation()), p);
        
            wave.add(w);
        }
        
        mobs.add(wave);
        
        e.spawnEntites(wave);
    }
    
    public Location getRandomLocNear(Location l) {
        float xOffset = (float) Math.random() * 50 - 25;
        float zOffset = (float) Math.random() * 50 - 25;
        
        float y = Bukkit.getWorlds().get(0).getHighestBlockYAt(l);
        
        return new Location(Bukkit.getWorlds().get(0), (float) l.getX() + xOffset, y, (float) l.getZ() + zOffset);
    }
    
    public EntityType chooseRandom() {
        int pos = (int) (Math.random() * chooseFrom.length);
        
        return chooseFrom[pos];
    }
}

