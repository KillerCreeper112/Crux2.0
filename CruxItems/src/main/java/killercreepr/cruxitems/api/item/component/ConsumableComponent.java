package killercreepr.cruxitems.api.item.component;

import killercreepr.cruxitems.api.item.consume.ItemConsumeContext;
import killercreepr.cruxitems.api.item.consume.ItemConsumeResult;
import org.jetbrains.annotations.NotNull;

public interface ConsumableComponent {
    @NotNull
    ItemConsumeResult onUse(@NotNull ItemConsumeContext ctx);
    boolean isUsable(@NotNull ItemConsumeContext ctx);
}
