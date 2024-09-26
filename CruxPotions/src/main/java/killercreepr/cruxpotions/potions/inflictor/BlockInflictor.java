package killercreepr.cruxpotions.potions.inflictor;

import killercreepr.crux.data.Holder;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BlockInflictor implements PotionInflictor, Holder<Block> {
    protected final @NotNull Block block;
    public BlockInflictor(@NotNull World world, @NotNull Vector vector) {
        this.block = world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    public BlockInflictor(@NotNull Block block) {
        this.block = block;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                 "world", block.getWorld().getName(),
                "x", block.getX(),
                "y", block.getY(),
                "z", block.getZ()
        );
    }

    public static @NotNull BlockInflictor deserialize(@NotNull Map< String, Object> map){
        World world = Bukkit.getWorld((String) map.getOrDefault("world", ""));
        if(world == null) throw new RuntimeException("World cannot be null!");
        return new BlockInflictor(world, new Vector(
                (int) map.getOrDefault("x", 0),
                (int) map.getOrDefault("y", 0),
                (int) map.getOrDefault("z", 0)
        ));
    }

    @Override
    public @NotNull Block value() {
        return block;
    }
}
