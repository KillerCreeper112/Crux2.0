package killercreepr.cruxblocks.core.block.component.standard;

import killercreepr.crux.api.entity.predicate.EntityPredicate;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.api.block.component.CruxMinerMineComponent;
import killercreepr.cruxblocks.api.mining.user.Miner;
import killercreepr.cruxblocks.core.mining.user.EntityMiner;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ApplyPotionEffectsMinerMIneComponent implements CruxMinerMineComponent {
    protected final Collection<PotionEffect> potionEffects;
    protected final EntityPredicate filter;
    protected final Float mineSpeed;

    public ApplyPotionEffectsMinerMIneComponent(Collection<PotionEffect> potionEffects, EntityPredicate filter, Float mineSpeed) {
        this.potionEffects = potionEffects;
        this.filter = filter;
        this.mineSpeed = mineSpeed;
    }
    public Collection<PotionEffect> getPotionEffects() {
        return potionEffects;
    }

    public EntityPredicate getFilter() {
        return filter;
    }

    @Override
    public @Nullable Float onMinerMine(@Nullable Miner miner, @NotNull ActiveCruxBlock block) {
        if(miner == null) return mineSpeed;
        if(!(miner instanceof EntityMiner m) || !(m.getEntity() instanceof LivingEntity lv)) return mineSpeed;
        if(filter != null && !filter.test(lv)) return mineSpeed;
        lv.addPotionEffects(potionEffects);
        return mineSpeed;
    }
}
