package ServerData;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class WaveData {
    public Location spawnLocation;
    public EntityType type;
    public Entity instance;
    public Player target;
    
    public WaveData(EntityType t, Location l, Player target) {
        spawnLocation = l;
        type = t;
        this.target = target;
    }
}
