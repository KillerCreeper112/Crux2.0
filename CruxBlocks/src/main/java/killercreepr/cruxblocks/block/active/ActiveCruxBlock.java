package killercreepr.cruxblocks.block.active;

import killercreepr.cruxblocks.block.CruxBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Represents a crux block that is placed into the world.
 */
public interface ActiveCruxBlock {
    default void placed(@Nullable Entity e){}
    default void broken(@Nullable Entity e){}
    @NotNull Block getBlock();
    @NotNull
    CruxBlock getCruxBlock();

    void breakBlock(@Nullable Entity e, @Nullable ItemStack tool);

    default void breakBlock(@Nullable ItemStack tool){
        breakBlock(null, tool);
    }

    /**
     * Checks if this block is still valid based on the block that is representing it in the world.
     */
    boolean isValid();

    void update();

    default boolean canHarvest(@Nullable Entity e, @Nullable ItemStack tool){
        return true;
    }

    @Nullable
    default Collection<ItemStack> getDrops(@Nullable Entity e, @Nullable ItemStack tool){
        return null;
    }

    default boolean isPreferredTool(@Nullable ItemStack item){ return false; }

    @Nullable
    default Collection<ItemStack> getDrops(){ return getDrops(null, null); }

    default float getMineSpeed(@Nullable Entity e, @Nullable ItemStack tool, boolean includeEnchants){
        boolean preferredTool = isPreferredTool(tool);
        boolean canHarvest = canHarvest(e, tool);

        float speedMultiplier = 1f;
        if(canHarvest && includeEnchants) {
            //enchants
        }
        //if(preferredTool) speedMultiplier = (float) GrimAttribute.get(e, GrimAttribute.MINING_SPEED);

        if(e instanceof LivingEntity lE){
            if(lE.hasPotionEffect(PotionEffectType.HASTE)){
                speedMultiplier *= 0.2f * (lE.getPotionEffect(PotionEffectType.HASTE).getAmplifier() + 1f);
            }
            /*if(lE.hasPotionEffect(PotionEffectType.SLOW_DIGGING)){
                speedMultiplier *= Math.pow(.3f, Math.min(lE.getPotionEffect(PotionEffectType.SLOW_DIGGING).getAmplifier(), 4));
            }*/
            if(lE.getEyeLocation().getBlock().getType() == Material.WATER /*&& doesNotHaveAquaAffinity*/){
                speedMultiplier /= 5f;
            }
        }
        if(e != null && e.getFallDistance() > 0f) speedMultiplier /= 5f;
        float damage = speedMultiplier / getCruxBlock().getHardness();
        if(canHarvest) damage /= 30f;
        else damage /= 100f;
        return damage;
    }
}
