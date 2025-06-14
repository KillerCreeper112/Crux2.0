package killercreepr.cruxblocks.core.block.component.standard;

import killercreepr.crux.api.entity.predicate.EntityPredicate;
import killercreepr.cruxblocks.api.block.component.CruxInteractablePhysicalBlockComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ApplyPotionEffectsEntityPhysicalInteractComponent implements CruxInteractablePhysicalBlockComponent {
    protected final Collection<PotionEffect> potionEffects;
    protected final EntityPredicate filter;

    public ApplyPotionEffectsEntityPhysicalInteractComponent(Collection<PotionEffect> potionEffects, EntityPredicate filter) {
        this.potionEffects = potionEffects;
        this.filter = filter;
    }

    @Override
    public void onEntityPhysicalInteract(@NotNull Entity e, @NotNull PlayerInteractEvent event) {
        if(!(e instanceof LivingEntity lv)) return;
        if(filter != null && !filter.test(e)) return;
        lv.addPotionEffects(potionEffects);
    }

    @Override
    public void onEntityPhysicalInteract(@NotNull Entity e, @NotNull EntityInteractEvent event) {
        if(!(e instanceof LivingEntity lv)) return;
        if(filter != null && !filter.test(e)) return;
        lv.addPotionEffects(potionEffects);
    }
}
