package killercreepr.cruxpotions.core.cruxblocks;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.entity.predicate.EntityPredicate;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.api.block.component.CruxMinerMineComponent;
import killercreepr.cruxblocks.api.mining.user.Miner;
import killercreepr.cruxblocks.core.mining.user.EntityMiner;
import killercreepr.cruxpotions.api.entity.PotionHolder;
import killercreepr.cruxpotions.api.potion.StoredPotion;
import killercreepr.cruxpotions.api.potion.inflictor.PotionInflictor;
import killercreepr.cruxpotions.core.entity.memory.SimplePotionHolder;
import killercreepr.cruxpotions.core.potions.inflictor.BlockInflictor;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ApplyCruxPotionEffectsMinerMineComponent implements CruxMinerMineComponent {
    protected final Collection<StoredPotion> potionEffects;
    protected final EntityPredicate filter;
    protected final Float mineSpeed;

    public ApplyCruxPotionEffectsMinerMineComponent(Collection<StoredPotion> potionEffects, EntityPredicate filter, Float mineSpeed) {
        this.potionEffects = potionEffects;
        this.filter = filter;
        this.mineSpeed = mineSpeed;
    }
    public Collection<StoredPotion> getPotionEffects() {
        return potionEffects;
    }

    public EntityPredicate getFilter() {
        return filter;
    }

    @Override
    public @Nullable Float onMinerMine(@Nullable Miner miner, @NotNull ActiveCruxBlock block) {
        if(miner == null) return mineSpeed;
        if(!(miner instanceof EntityMiner m) || !(m.getEntity() instanceof Entity e)) return mineSpeed;
        if(filter != null && !filter.test(e)) return mineSpeed;
        PotionHolder potionHolder = EntityMemory.getDataHolder(e, SimplePotionHolder.class);
        if(potionHolder == null) return mineSpeed;
        PotionInflictor inflictor = new BlockInflictor(e.getLocation().getBlock());

        for (StoredPotion effect : potionEffects) {
            potionHolder.addPotion(effect.create(e, inflictor));
        }
        return mineSpeed;
    }
}
