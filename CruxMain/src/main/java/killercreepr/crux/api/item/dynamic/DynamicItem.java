package killercreepr.crux.api.item.dynamic;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.item.SimpleCruxItem;
import killercreepr.crux.core.item.dynamic.BukkitDynamicItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface DynamicItem extends Cloneable {
    static Builder builder(){
        return new BukkitDynamicItem.Builder();
    }

    @NotNull String material();
    @NotNull String amount();
    @Nullable Map<String, DynamicItemComponent> components();

    @NotNull DynamicItem withType(@NotNull String material);
    @NotNull DynamicItem withAmount(@NotNull String amount);
    @NotNull DynamicItem withComponent(@NotNull DynamicItemComponent component);
    @NotNull DynamicItem mergeComponent(@NotNull DynamicItemComponent component);
    @NotNull DynamicItem mergeItem(@NotNull DynamicItem item);

    @Nullable
    SimpleCruxItem build(@NotNull TextParserContext context);

    @NotNull
    CompletableFuture<SimpleCruxItem> buildCompletely(@NotNull TextParserContext context);

    default @Nullable ItemStack buildItem(@NotNull TextParserContext context){
        SimpleCruxItem i = build(context);
        return i==null?null:i.item();
    }

    @NotNull
    SimpleCruxItem applyComponents(@NotNull SimpleCruxItem item, @NotNull TextParserContext context);

    @NotNull DynamicItem clone();

    interface Builder{
        Builder material(String material);
        Builder amount(String amount);
        Builder components(Map<String, DynamicItemComponent> components);
        Builder addComponent(DynamicItemComponent component);
        DynamicItem build();
        Builder components(@Nullable Collection<DynamicItemComponent> components);
        Builder components(@Nullable DynamicItemComponent... components);

        Builder addComponents(@Nullable Map<String, DynamicItemComponent> components);
        Builder addComponents(@Nullable Collection<DynamicItemComponent> components);
        Builder addComponents(@Nullable DynamicItemComponent... components);
    }
}
