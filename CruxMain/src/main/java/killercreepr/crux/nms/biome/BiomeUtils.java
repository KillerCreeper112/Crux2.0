package killercreepr.crux.nms.biome;

import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftWorld;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class BiomeUtils {
    public static @NotNull NamespacedKey getBiome(@NotNull Location l){
        return getBiome(l.getBlock());
    }

    public static @NotNull NamespacedKey getBiome(@NotNull Block l){
        ServerLevel w = ((CraftWorld)l.getWorld()).getHandle();
        BlockPos pos = new BlockPos(l.getX(), l.getY(), l.getZ());
        if (w.isLoaded(pos)) {
            LevelChunk chunk = w.getChunkAt(pos);
            Optional<ResourceKey<Biome>> optional = chunk.getNoiseBiome(l.getX() >> 2, l.getY() >> 2, l.getZ() >> 2).unwrapKey();
            if(optional.isPresent()){
                ResourceKey<Biome> biome = optional.get();
                return new NamespacedKey(
                        biome.location().getNamespace(),
                        biome.location().getPath()
                );
            }
        }
        return l.getBiome().getKey();
    }

    public static boolean setBiome(@NotNull Holder<Biome> biomeHolder, @NotNull Chunk c) {
        ServerLevel w = ((CraftWorld)c.getWorld()).getHandle();
        for (int x = 0; x <= 15; x++) {
            for (int z = 0; z <= 15; z++) {
                for(int y = 0; y <= c.getWorld().getMaxHeight(); y++) {
                    setBiome(c.getX() * 16 + x, y, c.getZ() * 16 + z, w, biomeHolder);
                }
            }
        }
        refreshChunksForAll(c);
        return true;
    }

    public static boolean setBiome(@NotNull Holder<Biome> biomeHolder, @NotNull Location l) {
        setBiome(l.getBlockX(), l.getBlockY(), l.getBlockZ(), ((CraftWorld)l.getWorld()).getHandle(), biomeHolder);
        refreshChunksForAll(l.getChunk());
        return true;
    }

    public static void setBiome(int x, int y, int z, @NotNull ServerLevel w, @NotNull Holder<Biome> bb) {
        BlockPos pos = new BlockPos(x, 0, z);
        if (w.isLoaded(pos)) {
            LevelChunk chunk = w.getChunkAt(pos);
            chunk.setBiome(x >> 2, y >> 2, z >> 2, bb);
            chunk.setUnsaved(true);
        }
    }

    public static void refreshChunksForAll(@NotNull Chunk chunk) {
        ServerLevel level = ((CraftChunk)(chunk)).getCraftWorld().getHandle();
        level.getChunkSource().chunkMap.resendBiomesForChunks(List.of(level.getChunk(chunk.getX(),chunk.getZ())));
    }
}
