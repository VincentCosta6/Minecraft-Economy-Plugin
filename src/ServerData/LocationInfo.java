package ServerData;

import org.bukkit.Chunk;

public class LocationInfo {
    public Chunk chunk;
    public String factionName;
    
    public LocationInfo(Chunk chunk, String faction) {
        this.chunk = chunk;
        this.factionName = faction;
    }
}