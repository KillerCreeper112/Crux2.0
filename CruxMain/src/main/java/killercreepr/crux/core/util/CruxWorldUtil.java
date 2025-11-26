package killercreepr.crux.core.util;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.Crux;
import net.kyori.adventure.key.Key;
import org.apache.commons.io.FileUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CruxWorldUtil {
    public static boolean isLoaded(@NotNull World world, int x, int z) {
        return world.isChunkLoaded(x >> 4, z >> 4);
    }

    public static boolean isLoaded(@NotNull World world, @NotNull Location loc) {
        return isLoaded(world, loc.getBlockX(), loc.getBlockZ());
    }

    public static boolean isLoaded(@NotNull World world, @NotNull CruxPosition loc) {
        return isLoaded(world, loc.blockX(), loc.blockZ());
    }

    public static boolean isLoaded(@NotNull World world, @NotNull Vector loc) {
        return isLoaded(world, loc.getBlockX(), loc.getBlockZ());
    }

    public static boolean isLoaded(@NotNull Location loc) {
        return loc.getWorld() != null && isLoaded(loc.getWorld(), loc);
    }

    public static File copyWorld(String name, String copyName, boolean overwrite) {
        File source = getWorldFolder(name);
        if (source == null) return null;

        File destination = new File(source.getParentFile(), copyName);

        try {
            if (destination.exists()) {
                if (!overwrite) {
                    return null;
                }
                FileUtils.deleteDirectory(destination);
            }

            FileUtils.copyDirectory(source, destination);
            return destination;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static World copyAndLoadWorld(String name, String copyName, boolean overwrite){
        File copied = copyWorld(name, copyName, overwrite);
        if(copied == null) return null;
        return Crux.getServer().createWorld(new WorldCreator(copyName));
    }

    public static boolean deleteWorld(@NotNull World world){
        if(Crux.getServer().getWorld(world.getUID()) != null){
            if(Crux.getServer().isTickingWorlds()){
                throw new IllegalStateException("Cannot unload world while Bukkit.isTickingWorlds is true!");
            }
            Crux.getServer().unloadWorld(world, false);
        }
        return deleteWorld(world.getName());
    }

    public static boolean deleteWorld(@NotNull String name){
        for(File f : Crux.getServer().getWorldContainer().listFiles()){
            if(!f.getName().equals(name) || !f.isDirectory()) continue;
            try{
                FileUtils.deleteDirectory(f);
                return true;
            }catch (IOException e){
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static List<String> getWorldsFromContainer(){
        List<String> list = new ArrayList<>();
        for(File f : Crux.getServer().getWorldContainer().listFiles()){
            if(!f.isDirectory()) continue;
            for(File folderF : f.listFiles()){
                if(folderF.getName().equals("level.dat")){
                    list.add(f.getName());
                    break;
                }
            }
        }
        return list;
    }

    public static World getOrLoadWorld(@NotNull Key key){
        World world = Crux.getServer().getWorld(key);
        if(world != null) return world;

        return getOrLoadWorld(key.value());
    }

    public static File getWorldFolder(String name){
        for(File f : Crux.getServer().getWorldContainer().listFiles()){
            if(!f.getName().equals(name)) continue;
            if(!f.isDirectory()) continue;
            return f;
        }
        return null;
    }

    public static World getOrLoadWorld(@NotNull String name){
        World world = Crux.getServer().getWorld(name);
        if(world != null) return world;
        for(File f : Crux.getServer().getWorldContainer().listFiles()){
            if(!f.getName().equals(name)) continue;
            if(!f.isDirectory()) continue;
            /*boolean foundLevel = false;
            for(File folderF : f.listFiles()){
                if(folderF.getName().equals("level.dat")){
                    foundLevel = true;
                    break;
                }
            }
            if(!foundLevel) break;*/
            return Crux.getServer().createWorld(new WorldCreator(name));
        }
        return null;
    }
}
