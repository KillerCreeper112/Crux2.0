package killercreepr.crux.core.util;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.Crux;
import net.kyori.adventure.key.Key;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class CruxWorldUtil {
    public static final Map<String, Function<String, WorldCreator>> CUSTOM_WORLD_CREATORS = new HashMap<>();

    public static boolean isLoaded(Block block){
        return isLoaded(block.getWorld(), block.getX(), block.getZ());
    }
    public static boolean isLoaded(Block block, int offsetX, int offsetZ){
        return isLoaded(block.getWorld(), block.getX() + offsetX, block.getZ() + offsetZ);
    }
    public static boolean isLoaded(Block block, BlockFace offset){
        return isLoaded(block, offset.getModX(), offset.getModZ());
    }

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
        return copyWorld(name, copyName, overwrite, false);
    }

    public static File copyWorld(String name, String copyName, boolean overwrite, boolean includeUID) {
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

            if(!includeUID){
                destination.toPath().resolve("data/paper/metadata.dat").toFile().delete();

                /*applyToChildren(
                    destination,
                    file ->{
                        if(file.getName().equals("uid.dat")){
                            file.delete();
                            return true;
                        }
                        return false;
                    }
                );*/
            }

            return destination;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void applyToChildren(File folder, Function<File, Boolean> consumer){
        File[] list = folder.listFiles();
        if(list == null) return;
        for (File file : list) {
            var result = consumer.apply(file);
            if(result) break;
        }
    }

    public static World copyAndLoadWorld(String name, String copyName, boolean overwrite){
        return copyAndLoadWorld(name, copyName, overwrite, false);
    }

    public static WorldCreator getWorldCreator(String name){
        return getWorldCreator(name, false);
    }

    public static WorldCreator getWorldCreator(String name, boolean ignoreCustom){
        if(ignoreCustom) return new WorldCreator(name);
        var custom = CUSTOM_WORLD_CREATORS.get(name);
        if(custom == null) return new WorldCreator(name);
        return custom.apply(name);
    }

    public static World copyAndLoadWorld(String name, String copyName, boolean overwrite, boolean includeUID){
        File copied = copyWorld(name, copyName, overwrite, includeUID);
        if(copied == null) return null;
        return Crux.getServer().createWorld(getWorldCreator(copyName));
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

    public static CompletableFuture<Boolean> deleteWorldWhenSafe(@NotNull World world){
        if(Crux.getServer().getWorld(world.getUID()) != null){
            return unloadWorldWhenSafe(world, false).thenCompose(unloadSuccess -> {
                if (unloadSuccess) {
                    // Once the world is unloaded successfully, delete it
                    return CompletableFuture.completedFuture(deleteWorld(world.getName()));
                } else {
                    // If unloading failed, return a completed future with 'false' indicating failure
                    return CompletableFuture.completedFuture(false);
                }
            });
        }
        return CompletableFuture.completedFuture(deleteWorld(world.getName()));
    }

    public static CompletableFuture<Boolean> unloadWorldWhenSafe(World world, boolean save){
        if(!Crux.getServer().isTickingWorlds()){
            return CompletableFuture.completedFuture(Crux.getServer().unloadWorld(world, save));
        }
        Key key = world.key();
        CompletableFuture<Boolean> resultFuture = new CompletableFuture<>();
        new BukkitRunnable(){
            @Override
            public void run() {
                if(Crux.getServer().getWorld(key) == null){
                    cancel();
                    resultFuture.complete(false);
                    return;
                }
                if(Crux.getServer().isTickingWorlds()) return;
                cancel();
                resultFuture.complete(Crux.getServer().unloadWorld(world, save));
            }
        }.runTaskTimer(Crux.getMainPlugin(), 0L, 1L);
        return resultFuture;
    }

    public static boolean deleteWorld(@NotNull String name){
        for(File f : getWorldContainer().listFiles()){
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
        for(File f : getWorldContainer().listFiles()){
            if(!f.isDirectory()) continue;
            list.add(f.getName());
        }
        return list;
    }

    public static World getOrLoadWorld(@NotNull Key key){
        World world = Crux.getServer().getWorld(key);
        if(world != null) return world;

        return getOrLoadWorld(key.value());
    }

    public static Path getWorldPath(){
        if(Crux.getServer().getMinecraftVersion().startsWith("1.")){
            return Crux.getServer().getWorldContainer().toPath();
        }
        return Crux.getServer().getWorld(Key.key("overworld")).getWorldPath().getParent();
    }

    public static File getWorldContainer(){
        if(Crux.getServer().getMinecraftVersion().startsWith("1.")){
            return Crux.getServer().getWorldContainer();
        }
        return Crux.getServer().getWorld(Key.key("overworld")).getWorldPath().getParent().toFile();
        //return Crux.getMainPlugin().getDataPath().getParent().getParent().resolve("world/dimensions/minecraft").toFile();
    }

    public static File getWorldFolder(String name){
        var file = getWorldPath().resolve(name).toFile();
        if(!file.exists()) return null;
        return file;
        /*for(File f : getWorldContainer().listFiles()){
            if(!f.getName().equals(name)) continue;
            if(!f.isDirectory()) continue;
            return f;
        }
        return null;*/
    }

    public static World getOrLoadWorld(@NotNull String name){
        World world = Crux.getServer().getWorld(name);
        if(world != null) return world;
        File f = getWorldFolder(name);
        if(f == null) return null;
        return Crux.getServer().createWorld(getWorldCreator(name));
    }
}
