package killercreepr.crux.core.item.dynamic;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.item.dynamic.DynamicItemComponent;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.Crux;
import killercreepr.crux.paper.ItemHolder;
import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class BukkitDynamicItem implements DynamicItem {
    protected final @NotNull String material;
    protected final @Nullable String amount;
    protected final @Nullable Map<String, DynamicItemComponent> components;

    public BukkitDynamicItem(@NotNull String material, @Nullable String amount, @Nullable Map<String, DynamicItemComponent> components) {
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
    public @NotNull DynamicItem withType(@NotNull String material) {
        return new BukkitDynamicItem(material, amount, components);
    }

    @Override
    public @NotNull DynamicItem withAmount(@NotNull String amount) {
        return new BukkitDynamicItem(material, amount, components);
    }

    @Override
    public @NotNull DynamicItem withComponent(@NotNull DynamicItemComponent component) {
        return new Builder(material)
                .amount(amount)
                .components(components)
                .addComponent(component)
                .build();
    }

    @Override
    public @NotNull DynamicItem mergeItem(@NotNull DynamicItem item) {
        Map<String, DynamicItemComponent> components = item.components();
        if(components == null) return this;
        DynamicItem i = this;
        for(DynamicItemComponent c : components.values()){
            i = i.mergeComponent(c);
        }
        return i;
    }

    @Override
    public @NotNull DynamicItem mergeComponent(@NotNull DynamicItemComponent component) {
        return new Builder(material)
            .amount(amount)
            .components(components)
            .mergeComponent(component)
            .build();
    }

    protected @Nullable Material matchMaterial(@NotNull String s){
        try{
            return Registry.MATERIAL.get(Key.key(s.toLowerCase()));
        }catch (InvalidKeyException ignored){}
        return null;
    }

    public @Nullable ItemStack buildBase(@NotNull TextParserContext context){
        String parsed = context.deserializeString(material());

        try{
            ItemHolder itemHolder = Crux.handlers().item().getItem(Crux.key(parsed));
            if(itemHolder != null){
                return itemHolder.value();
            }
        }catch (Exception ignored){}

        Material material = matchMaterial(parsed);
        ItemStack built;
        if(material == null){
            try{
                built = Bukkit.getItemFactory().createItemStack(parsed);
            }catch (IllegalArgumentException ignored){
                try{
                    built = ItemStack.deserializeBytes(Base64.getDecoder().decode(parsed));
                }catch (Exception ignored_){
                    return null;
                }
            }
        }else built = new ItemStack(material);
        return built;
    }

    @Override
    public @Nullable CruxItem build(@NotNull TextParserContext context) {
        ItemStack built = buildBase(context);
        if(built==null) return null;
        CruxItem item = CruxItem.wrap(built);
        if(amount != null){
            item.amount((int) Double.parseDouble(context.deserializeString(amount)));
        }
        return applyComponents(item, context);
    }

    @Override
    public @NotNull CompletableFuture<CruxItem> buildCompletely(@NotNull TextParserContext context) {
        return CompletableFuture.supplyAsync(() -> build(context));
    }

    @Override
    public @NotNull CruxItem applyComponents(@NotNull CruxItem item, @NotNull TextParserContext context) {
        if(components == null) return item;
        for(DynamicItemComponent component : components.values()){
            component.apply(item, context);
        }
        return item;
    }

    @Override
    public @NotNull BukkitDynamicItem clone() {
        try {
            BukkitDynamicItem item = (BukkitDynamicItem) super.clone();
            Map<String, DynamicItemComponent> components;
            if(this.components == null) components = null;
            else{
                components = new HashMap<>();
                this.components.forEach((id, comp) ->{
                    components.put(id, comp.clone());
                });
            }
            BukkitDynamicItem.Builder builder = new Builder(item.material())
                    .amount(amount())
                    .components(components)
                    ;
            return builder.build();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public static class Builder implements DynamicItem.Builder{
        private @NotNull String material;
        private @Nullable String amount = "1";
        private final Map<String, DynamicItemComponent> components = new HashMap<>();

        public Builder(@NotNull String material) {
            this.material = material;
        }

        public Builder() {
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

        public Builder mergeComponent(@NotNull DynamicItemComponent component){
            DynamicItemComponent c = components.get(component.name());
            if(c == null) return addComponent(component);
            return addComponent(c.merge(component));
        }

        public Builder addComponents(@Nullable Map<String, DynamicItemComponent> c){
            if(c==null) return this;
            components.putAll(c);
            return this;
        }

        public Builder addComponents(@Nullable Collection<DynamicItemComponent> components){
            if(components==null) return this;
            components.forEach(this::addComponent);
            return this;
        }

        @Override
        public Builder addComponents(@Nullable DynamicItemComponent... components) {
            if(components==null) return this;
            return addComponents(Arrays.asList(components));
        }

        public Builder components(@Nullable Map<String, DynamicItemComponent> components){
            this.components.clear();
            return addComponents(components);
        }

        public Builder components(@Nullable Collection<DynamicItemComponent> components){
            this.components.clear();
            return addComponents(components);
        }

        @Override
        public Builder components(@Nullable DynamicItemComponent... components) {
            return components(components == null ? null : Arrays.asList(components));
        }

        public @NotNull BukkitDynamicItem build(){
            Objects.requireNonNull(material, "Material cannot be null!");
            return new BukkitDynamicItem(material, amount, components.isEmpty()?null:components);
        }
    }
}
