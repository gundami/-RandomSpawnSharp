package net.gundami.randomspawnsharp.utils;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncTask {
    private final ExecutorService executor;

    public AsyncTask() {
        executor = Executors.newSingleThreadExecutor((r)->{
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        });
    }
    public void randomSpawn(RandomPoint randomPoint, ServerPlayerEntity serverPlayerEntity){
        executor.submit(()->{
            BlockPos blockPos = null;
            blockPos = randomPoint.getRandomPoint(serverPlayerEntity.getWorld());
            serverPlayerEntity.teleport(serverPlayerEntity.getWorld(),blockPos.getX(),blockPos.getY(),blockPos.getZ(),0,0);
            serverPlayerEntity.setSpawnPoint(serverPlayerEntity.getWorld().getRegistryKey(),blockPos,0,true,false);
        });

    }
}
