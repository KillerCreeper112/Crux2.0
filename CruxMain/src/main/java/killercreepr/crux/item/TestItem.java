package killercreepr.crux.item;

import killercreepr.crux.util.CruxItem;
import killercreepr.crux.valueproviders.InputContext;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TestItem implements DynamicItem{
    protected final @NotNull String material;
    protected final @Nullable String amount;
    protected final @Nullable Map<String, DynamicItemComponent> components;

    public TestItem(@NotNull String material, @Nullable String amount, @Nullable Map<String, DynamicItemComponent> components) {
        this.material = material;
        this.amount = amount;
        this.components = components == null ? null : Collections.unmodifiableMap(components);
    }

    @Override
    public @NotNull String material() {
        return material;
    }

    @Override
    public @NotNull String amount() {
        return amount==null?"1":amount;
    }

    @Override
    public @Nullable Map<String, DynamicItemComponent> components() {
        return components;
    }

    @Override
    public @Nullable CruxItem build(@NotNull InputContext context) {
        Material material = Material.matchMaterial(context.input(material()));
        if(material == null) return null;
        CruxItem item = new CruxItem(new ItemStack(material, amount==null?1:(int) Double.parseDouble(context.input(amount))));
        for(DynamicItemComponent component : components.values()){
            component.apply(item, context);
        }
        return item;
    }

    public static class Builder{
        private @NotNull String material;
        private @Nullable String amount;
        private final Map<String, DynamicItemComponent> components = new HashMap<>();

        public Builder(@NotNull String material) {
            this.material = material;
        }

        public Builder material(@NotNull String material) {
            this.material = material; return this;
        }

        public Builder amount(@Nullable String amount) {
            this.amount = amount; return this;
        }

        public Builder addComponent(@NotNull DynamicItemComponent c){
            components.put(c.name(), c);
            return this;
        }

        public @NotNull TestItem build(){
            return new TestItem(material, amount, components.isEmpty()?null:components);
        }
    }
}
