package net.gundami.randomspawnsharp;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.gundami.randomspawnsharp.utils.AsyncTask;
import net.gundami.randomspawnsharp.utils.ConfigClass;
import net.gundami.randomspawnsharp.utils.RandomPoint;
import net.gundami.randomspawnsharp.utils.ServerState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static net.minecraft.server.command.CommandManager.*;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RandomSpawnSharp implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	public static String spawnMapPath;
	public static boolean firstJoinRandomSpawn;



	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("RandomSpawn#!");

		AutoConfig.register(ConfigClass.class,Toml4jConfigSerializer::new);
		ConfigClass config = AutoConfig.getConfigHolder(ConfigClass.class).getConfig();

		spawnMapPath = config.spawnMapPath;
		firstJoinRandomSpawn = config.firstJoinRandomSpawn;

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("rss")
				.requires(source -> source.hasPermissionLevel(4))
				.executes(context -> {
					if (context.getSource().isExecutedByPlayer()){
						//new AsyncTask().randomSpawn(new RandomPoint(config.xMin, config.xMax, config.zMin, config.zMax),context.getSource().getPlayer());
						ServerPlayerEntity serverPlayerEntity = context.getSource().getPlayer();
						BlockPos blockPos = null;
						blockPos = new RandomPoint(config.xMin, config.xMax, config.zMin, config.zMax).getRandomPoint(serverPlayerEntity.getWorld());
						serverPlayerEntity.teleport(serverPlayerEntity.getWorld(),blockPos.getX(),blockPos.getY(),blockPos.getZ(),0,0);
						serverPlayerEntity.setSpawnPoint(serverPlayerEntity.getWorld().getRegistryKey(),blockPos,0,true,false);

					}
					return 1;
				})));

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerState serverState = ServerState.getServerState(server);
			ServerPlayerEntity player = handler.player;
			if (!serverState.players.contains(player.getUuid()) && firstJoinRandomSpawn){
				LOGGER.info("Start finding new spawn point");
				BlockPos blockPos = null;
				blockPos = new RandomPoint(config.xMin, config.xMax, config.zMin, config.zMax).getRandomPoint(player.getWorld());
				player.teleport(player.getWorld(),blockPos.getX(),blockPos.getY(),blockPos.getZ(),0,0);
				player.setSpawnPoint(player.getWorld().getRegistryKey(),blockPos,0,true,false);				serverState.players.add(player.getUuid());
				serverState.markDirty();
			}
		});

	}
}
