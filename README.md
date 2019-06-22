# Minecraft-Economy-Plugin
This plugin manages factions, territories, shop items, and mining payments. It uses a MongoDB database server for player info, territory info, and faction info.

This plugin comes with a secret "Winter is coming" mode that is a work in progress at the moment. After a certain amount of days the server will turn a large area into a snow biome, and forever make it nighttime and snow. During this, all kinds of mobs will target the player up to 100 blocks away and will all spawn with the "Wight" name tag. The only way to end Winter is to kill the "Night King" who is a Wither that spawns somewhere unknown. After killing the Night King all Wights will die, it will stop snowing, and daytime will return. Warning this is extremely buggy at times and often requires the player to rejoin the server once Winter starts. It is also extremely difficult to beat and your base will get beaten up from all of the explosions

If you want to run this plugin on your server you will need to 
1) Set up a MongoDB server
2) Create a MongoDB database and call it minecraft
3) Create 3 collections called: "factions", "properties", and "users" in the "minecraft" database

(Optional: for larger servers) To make the database become more performant we need to create indexes on some of the keys. All of which are of type String. I recommend ticking "Build index in the background" and "Create unique index" 
1) Create an index for "name" in the factions collection
2) Create an index for "chunk" in the properties collection
3) Create an index for "username" and "uuid" in the users collection

This plugin is also dependant on a couple of external jar files that you will need to build with. We need these to interface with MongoDB to make calls and also to take advantage of Spigot and TitleAPI. To download and run this plugin, the dependencies are not required but if you want to develop for this plugin then they are.

1) [bson-3.8.1](https://repo1.maven.org/maven2/org/mongodb/bson/3.8.1/bson-3.8.1.jar)
2) [mongodb-driver-core-3.8.1](https://repo1.maven.org/maven2/org/mongodb/mongodb-driver-core/3.8.1/mongodb-driver-core-3.8.1.jar)
3) [mongo-java-driver-3.8.1](https://repo1.maven.org/maven2/org/mongodb/mongo-java-driver/3.8.1/mongo-java-driver-3.8.1.jar)
3) [spigot-1.13.2](https://cdn.getbukkit.org/spigot/spigot-1.13.2.jar)
4) [TitleAPI-1.7.7](https://www.spigotmc.org/resources/api-titleapi-1-7-1-8-1-9.1047/)
