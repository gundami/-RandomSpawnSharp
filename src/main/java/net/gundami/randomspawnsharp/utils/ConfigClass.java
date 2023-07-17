package net.gundami.randomspawnsharp.utils;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.ArrayList;

@Config(name = "RandomSpawnSharp")
public class ConfigClass implements ConfigData {

    public String spawnMapPath = "test";
    public boolean firstJoinRandomSpawn = true;
    public int xMax = 20;
    public int zMax = 20;
    public int xMin = -20;
    public int zMin = -20;

}
