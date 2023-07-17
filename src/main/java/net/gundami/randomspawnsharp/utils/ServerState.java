package net.gundami.randomspawnsharp.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ServerState extends PersistentState {
    public ArrayList<UUID> players = new ArrayList<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        // Putting the 'players' hashmap, into the 'nbt' which will be saved.
        NbtCompound playersNbtCompound = new NbtCompound();
        players.forEach((UUID)->{
            playersNbtCompound.putBoolean(UUID.toString(),true);

        });
        nbt.put("players", playersNbtCompound);

        return nbt;
    }

    public static ServerState createFromNbt(NbtCompound tag) {
        ServerState serverState = new ServerState();

        // Here we are basically reversing what we did in ''writeNbt'' and putting the data inside the tag back to our hashmap
        NbtCompound playersTag = tag.getCompound("players");
        playersTag.getKeys().forEach(key -> {
            UUID uuid = UUID.fromString(key);
            serverState.players.add(uuid);
        });

        return serverState;
    }

    public static ServerState getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        ServerState serverState = persistentStateManager.getOrCreate(
                ServerState::createFromNbt,
                ServerState::new,
                "RandomSpawnSharp");

        return serverState;
    }

}
