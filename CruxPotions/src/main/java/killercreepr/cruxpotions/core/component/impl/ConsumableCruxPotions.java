package killercreepr.cruxpotions.core.component.impl;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.cruxitems.api.item.component.ConsumableComponent;
import killercreepr.cruxitems.api.item.consume.ItemConsumeContext;
import killercreepr.cruxitems.api.item.consume.ItemConsumeResult;
import killercreepr.cruxpotions.api.entity.PotionHolder;
import killercreepr.cruxpotions.api.potion.StoredPotion;
import killercreepr.cruxpotions.core.entity.memory.SimplePotionHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConsumableCruxPotions implements ConsumableComponent {
    protected final List<StoredPotion> cruxPotions;

    public ConsumableCruxPotions(List<StoredPotion> potions) {
        this.cruxPotions = potions;
    }

    public List<StoredPotion> getCruxPotions() {
        return cruxPotions;
    }

    @Override
    public @NotNull ItemConsumeResult onConsume(@NotNull ItemConsumeContext ctx) {

        var e = ctx.getPlayer();
        PotionHolder holder = EntityMemory.getOrCreateDataHolder(e, SimplePotionHolder.class, SimplePotionHolder::new);
        if(holder == null) return ItemConsumeResult.empty();

        cruxPotions.forEach(stored ->{
            holder.addPotion(stored.create(e));
        });

        return ItemConsumeResult.empty();
    }

    @Override
    public boolean isConsumable(@NotNull ItemConsumeContext ctx) {
        return true;
    }
}
