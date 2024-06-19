package killercreepr.cruxblocks.block.active;

import com.destroystokyo.paper.ParticleBuilder;
import killercreepr.crux.Crux;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.context.BlockContext;
import killercreepr.cruxblocks.block.context.BlockContextImpl;
import killercreepr.cruxblocks.event.CruxBlockBreakEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Represents a crux block that is placed into the world.
 */
public interface ActiveCruxBlock {
    default void placed(@NotNull BlockContext ctx){}
    default void broken(@NotNull BlockContext ctx){}
    @NotNull Block getBlock();
    @NotNull
    CruxBlock getCruxBlock();

    default @NotNull CruxBlockBreakEvent breakBlock(@Nullable Entity e, @Nullable ItemStack tool){
        Block block = getBlock();
        Collection<ItemStack> drops = getDrops(e, tool);
        CruxBlockBreakEvent event = new CruxBlockBreakEvent(this, new BlockContextImpl(block, e),
            drops, tool);
        if(!event.callEvent()) return event;
        drops = event.getDrops();
        BlockData data = block.getBlockData();
        Crux.handlers().block().setType(getBlock(), Material.AIR);
        CruxBlock custom = getCruxBlock();
        if(custom.getSoundGroup() == null){
            block.getWorld().playSound(block.getLocation().toCenterLocation(), Sound.BLOCK_WOOD_BREAK, 1f, 1f);
        }else{
            block.getWorld().playSound(block.getLocation().toCenterLocation(), custom.getSoundGroup().getBreakSound(),
                custom.getSoundGroup().getVolume(), custom.getSoundGroup().getPitch());
        }

        new ParticleBuilder(Particle.BLOCK)
            .count(20)
            .offset(.5, .5, .5)
            .extra(.1)
            .data(data)
            .spawn()
        ;

        if(drops != null){
            Location x = block.getLocation().toCenterLocation().subtract(0, .5, 0);
            for(ItemStack i : drops){
                block.getWorld().dropItem(x, i);
            }
        }
        /*todo if(e != null) GrimItem.damage(e, tool, 1, EquipmentSlot.HAND);
        else GrimItem.damage(tool, 1);*/
        return event;
    }

    default @NotNull CruxBlockBreakEvent breakBlock(@Nullable ItemStack tool){
        return breakBlock(null, tool);
    }

    /**
     * Checks if this block is still valid based on the block that is representing it in the world.
     */
    boolean isValid();

    /**
     * Called to basically break the block if it's no longer in a valid location.
     * For example, a flower would probably want to break if it's no longer on a solid block.
     */
    default void update(){
        if(getCruxBlock().canPlace(new BlockContextImpl(getBlock(), null))) return;
        breakBlock(null);
    }

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
            if(lE.hasPotionEffect(PotionEffectType.MINING_FATIGUE)){
                speedMultiplier *= Math.pow(.3f, Math.min(lE.getPotionEffect(PotionEffectType.MINING_FATIGUE).getAmplifier(), 4));
            }
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
