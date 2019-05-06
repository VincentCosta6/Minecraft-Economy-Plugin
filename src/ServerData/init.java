package ServerData;

import java.io.FileNotFoundException;
import java.net.UnknownHostException;

public class init {
	
	public static BlockValues blockValues;
	public static DatabaseStore dbStore;
	
	public static void Init() {
		try 
		{
                    blockValues = new BlockValues("./plugins/Economy/block-values.txt", "./plugins/Economy/blacklist-values.txt");
                    dbStore = new DatabaseStore();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		}
	}
}
