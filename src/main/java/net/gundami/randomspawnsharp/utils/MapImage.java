package net.gundami.randomspawnsharp.utils;

import net.fabricmc.loader.api.FabricLoader;
import net.gundami.randomspawnsharp.RandomSpawnSharp;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

public class MapImage {
    private BufferedImage img = null;
    public MapImage(){
        String filePath = RandomSpawnSharp.spawnMapPath;
        File f = null;

        try{
            f = new File(filePath);
            img = ImageIO.read(f);
        }catch(IOException e){
            System.out.println(e);
        }
    }
    public boolean checkPoint(int x, int z){
        int gray= img.getRGB(x, z)& 0xFF;
        return gray == 0;
    }
}
