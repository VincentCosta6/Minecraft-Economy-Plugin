# Minecraft-Economy-Plugin
This plugin manages factions, territories, shop items, and mining payments. It uses a MongoDB database server for player info, territory info, and faction info.

This plugin comes with a secret "Winter is coming" mode that is a work in progress at the moment. After a certain amount of days the server will turn a large area into a snow biome, and forever make it nighttime and snow. During this, all kinds of mobs will target the player up to 100 blocks away and will all spawn with the "Wight" name tag. The only way to end Winter is to kill the "Night King" who is a Wither that spawns somewhere unknown. After killing the Night King all Wights will die, it will stop snowing, and daytime will return. Warning this is extremely buggy at times and often requires the player to rejoin the server once it starts. It is also extremely difficult to beat and your base will get beaten up from all of the explosions

If you want to run this plugin you will need to 
1) Set up a MongoDB database and call it minecraft
2) Create 3 collections called: "factions", "properties", and "users"

To make the database become more performant we need to create indexes on some of the keys
1) Create an index for "name" in the factions collection
2) Create an index for "chunk" in the properties collection
3) Create an index for "username" and "uuid" in the users collection

These are all string types ^
