package killercreepr.cruxblocks.block.active;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.context.BlockContextImpl;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
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

    default void breakBlock(@Nullable Entity e, @Nullable ItemStack tool){
        Block block = getBlock();
        Collection<ItemStack> drops = getDrops(e, tool);
        /*CustomBlockBreakEvent event = new CustomBlockBreakEvent(this, e, tool, drops);
        if(!event.callEvent()) return;*/
        BlockData data = block.getBlockData();
        //todo custom settype
        getBlock().setType(Material.AIR);
        CruxBlock custom = getCruxBlock();
        if(custom.getSoundGroup() == null){
            block.getWorld().playSound(block.getLocation().toCenterLocation(), Sound.BLOCK_WOOD_BREAK, 1f, 1f);
        }else{
            block.getWorld().playSound(block.getLocation().toCenterLocation(), custom.getSoundGroup().getBreakSound(),
                custom.getSoundGroup().getVolume(), custom.getSoundGroup().getPitch());
        }

        /*todo new CreateWorldParticle(new CreateBlockData(Particle.BLOCK_CRACK, 20, data)
            .values(.5, .5, .5, .1), new DynamicLocation(block.getLocation().toCenterLocation()))
            .play();*/

        if(drops != null){
            Location x = block.getLocation().toCenterLocation().subtract(0, .5, 0);
            for(ItemStack i : drops){
                block.getWorld().dropItem(x, i);
            }
        }
        /*todo if(e != null) GrimItem.damage(e, tool, 1, EquipmentSlot.HAND);
        else GrimItem.damage(tool, 1);*/
        return;
    }

    default void breakBlock(@Nullable ItemStack tool){
        breakBlock(null, tool);
    }

    /**
     * Checks if this block is still valid based on the block that is representing it in the world.
     */
    boolean isValid();

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
