package killercreepr.crux.util;

import killercreepr.crux.Crux;
import killercreepr.crux.tags.format.Format;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class CruxItem implements Cloneable {
    public static boolean isEmpty(@Nullable ItemStack i){
        //pre 1.20.5
        //return i == null || i.getType().isEmpty();
        //post 1.20.5
        return i == null || i.isEmpty();
    }
    public static final @NotNull Component NO_ITALICS = Component.empty().decoration(TextDecoration.ITALIC, false);
    protected @NotNull ItemStack item;
    protected @NotNull Format format;
    public CruxItem(@NotNull Material material) {
        this(new ItemStack(material));
    }
    public CruxItem(@NotNull ItemStack item) {
        this(Crux.FORMAT, item);
    }

    public CruxItem(@NotNull Format format, @NotNull Material material) {
        this(format, new ItemStack(material));
    }
    public CruxItem(@NotNull Format format, @NotNull ItemStack item) {
        this.format = format;
        this.item = item;
    }

    private Component c(@Nullable String s){
        if(s==null) return null;
        return c(format.deserialize(s));
    }

    private Component c(@Nullable Component s){
        if(s==null) return null;
        return NO_ITALICS.append(s);
    }

    private List<Component> c(@Nullable Collection<Component> s){
        if(s==null) return null;
        List<Component> newList = new ArrayList<>();
        for(Component c : s){
            newList.add(c(c));
        }
        return newList;
    }

    public @Nullable ItemMeta meta(){
        return item.getItemMeta();
    }

    public @NotNull Optional<ItemMeta> getMeta(){
        return Optional.ofNullable(meta());
    }

    public CruxItem meta(@Nullable ItemMeta meta){
        item.setItemMeta(meta);
        return this;
    }

    public CruxItem addFlags(@NotNull ItemFlag @NotNull... flags){
        return editMeta(meta -> meta.addItemFlags(flags));
    }

    public CruxItem addLoreFromString(@Nullable Collection<String> lore){
        return lore == null ? this : addLoreFromString(lore.toArray(new String[0]));
    }

    public CruxItem addLoreFromString(@NotNull String @NotNull... lore){
        List<Component> built = new ArrayList<>();
        for(String s : lore){
            built.add(c(s));
        }
        return addLore(built);
    }

    public CruxItem addLore(@Nullable Collection<Component> lore){
        return lore == null ? this : addLore(lore.toArray(new Component[0]));
    }

    public CruxItem addLore(@NotNull Component @NotNull... lore){
        List<Component> itemLore = lore();
        if(itemLore == null) itemLore = new ArrayList<>();
        else itemLore = new ArrayList<>(itemLore);
        itemLore.addAll(List.of(lore));
        lore(itemLore);
        return this;
    }

    public @NotNull Material material() {
        return item.getType();
    }

    public CruxItem material(@NotNull Material material) {
        //pre 1.20.5 this.item.setType(material);
        //post 1.20.5
        this.item = this.item.withType(material);
        return this;
    }

    public @Nullable Component displayName() {
        ItemMeta meta = meta();
        return meta==null?null : meta.displayName();
    }

    public CruxItem displayName(@Nullable String name) {
        return displayName(c(name));
    }

    public CruxItem displayName(@Nullable Component name) {
        return editMeta(meta -> meta.displayName(c(name)));
    }

    public @Nullable List<Component> lore() {
        ItemMeta meta = meta();
        return meta==null?null:meta.lore();
    }

    public CruxItem loreFromString(@Nullable List<String> lore) {
        return addLoreFromString(lore);
    }

    public CruxItem lore(@Nullable List<Component> lore) {
        return editMeta(meta -> meta.lore(c(lore)));
    }

    public @NotNull Collection<ItemFlag> flags() {
        ItemMeta meta = meta();
        return meta == null ? new HashSet<>() : meta.getItemFlags();
    }

    public CruxItem flags(@Nullable Collection<ItemFlag> flags) {
        return editMeta(meta ->{
            meta.removeItemFlags(ItemFlag.values());
            if(flags != null) meta.addItemFlags(flags.toArray(new ItemFlag[0]));
        });
    }

    public boolean unbreakable() {
        ItemMeta meta = meta();
        return meta != null && meta.isUnbreakable();
    }

    public CruxItem unbreakable(boolean unbreakable) {
        return editMeta(meta -> meta.setUnbreakable(unbreakable));
    }

    public CruxItem glow(boolean value){
        //pre 1.20.5
        /*enchant(Enchantment.DURABILITY, 1);
        addFlags(ItemFlag.HIDE_ENCHANTS);*/

        //post 1.20.5
        editMeta(meta -> meta.setEnchantmentGlintOverride(value));
        return this;
    }

    public CruxItem enchant(@NotNull Enchantment e, int level){
        return enchant(e, level, true);
    }

    public CruxItem enchant(@NotNull Enchantment e, int level, boolean ignoreLevelRestriction){
        editMeta(meta -> meta.addEnchant(e, level, ignoreLevelRestriction));
        return this;
    }

    public CruxItem amount(int amount){
        item.setAmount(amount);
        return this;
    }

    public int amount(){
        return item.getAmount();
    }

    public CruxItem addAttribute(@NotNull Attribute attribute, @NotNull AttributeModifier modifier){
        return editMeta(meta -> meta.addAttributeModifier(attribute, modifier));
    }

    public CruxItem addAttribute(@NotNull Attribute attribute, @NotNull String name, double amount, @NotNull AttributeModifier.Operation operation,
                                 @Nullable EquipmentSlot slot){
        return addAttribute(attribute, new AttributeModifier(UUID.randomUUID(), name, amount, operation, slot));
    }

    public CruxItem editMeta(@NotNull Consumer<ItemMeta> consumer){
        return editMeta(ItemMeta.class, consumer);
    }

    public <T extends ItemMeta> CruxItem editMeta(@NotNull Class<T> clazz, @NotNull Consumer<T> consumer){
        ItemMeta meta = meta();
        if(meta==null) return this;
        if(!clazz.isAssignableFrom(meta.getClass())) return this;
        consumer.accept(clazz.cast(meta));
        meta(meta);
        return this;
    }

    public @NotNull ItemStack item(){
        return item;
    }

    public CruxItem item(@NotNull ItemStack item){
        this.item = item;
        return this;
    }

    public @NotNull CruxItem clone() {
        try {
            CruxItem item = (CruxItem) super.clone();
            item.item(this.item.clone());
            return item;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public @NotNull Format getFormat() {
        return format;
    }
}
