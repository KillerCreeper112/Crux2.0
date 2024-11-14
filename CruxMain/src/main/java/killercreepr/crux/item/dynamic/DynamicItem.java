package killercreepr.crux.item.dynamic;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.util.CruxItem;
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
    CruxItem build(@NotNull TextParserContext context);

    @NotNull
    CompletableFuture<CruxItem> buildCompletely(@NotNull TextParserContext context);

    default @Nullable ItemStack buildItem(@NotNull TextParserContext context){
        CruxItem i = build(context);
        return i==null?null:i.item();
    }

    @NotNull CruxItem applyComponents(@NotNull CruxItem item, @NotNull TextParserContext context);

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
