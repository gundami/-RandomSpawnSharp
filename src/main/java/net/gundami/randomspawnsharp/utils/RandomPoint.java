package net.gundami.randomspawnsharp.utils;

import net.gundami.randomspawnsharp.RandomSpawnSharp;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;


import java.util.Random;
import static net.minecraft.world.Heightmap.Type.*;

public class RandomPoint {
    int xMin=0;
    int xMax=0;
    int zMin=0;
    int zMax=0;
    public RandomPoint(int xMin, int xMax, int zMin, int zMax){
        this.xMax=xMax;
        this.xMin=xMin;
        this.zMax=zMax;
        this.zMin=zMin;
    }

    public BlockPos getRandomPoint(ServerWorld serverWorld){
        Random rand = new Random();
        int x,y,z;
        Block topBlock;

        MapImage mapImage = new MapImage();

        do {
            x = rand.nextInt(xMax - xMin)-Math.abs(xMin);
            z = rand.nextInt(zMax - zMin)-Math.abs(zMin);
        }while (!mapImage.checkPoint(x+Math.abs(xMin),z+Math.abs(zMin)));
        int[] chunk = chunkPosition(x,z);

        serverWorld.getBlockState(new BlockPos(x,0,z)).getBlock();
        y = serverWorld.getTopY(WORLD_SURFACE, x, z) - 1;

        return new BlockPos(x,y+1,z);

    }

    private int[] chunkPosition(int x, int z){

        int[] result = new int[2];
        if (x>=0&&z>=0){
            result[0] = (int) Math.ceil(((float)x+1)/16);
            result[1] = (int) Math.ceil(((float)z+1)/16);
        } else if (x>=0&&z<=-1) {
            result[0] = (int) Math.ceil(((float)x+1)/16);
            result[1] = (int) Math.floor((float)z/16);
        } else if (x<=-1&&z<=-1) {
            result[0] = (int) Math.floor((float)x/16);
            result[1] = (int) Math.floor((float)z/16);
        } else if (x<=-1&&z>=0) {
            result[0] = (int) Math.floor((float)x/16);
            result[1] = (int) Math.ceil(((float)z+1)/16);
        }
        return result;
    }


}
