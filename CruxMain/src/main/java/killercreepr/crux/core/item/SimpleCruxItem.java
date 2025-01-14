package killercreepr.crux.core.item;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.item.CruxItemType;
import killercreepr.crux.api.text.format.FormatSerializer;
import killercreepr.crux.api.text.provider.StringTagProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.persistence.CruxPersist;
import killercreepr.crux.core.text.format.FormatParserContext;
import killercreepr.crux.core.util.CruxKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class SimpleCruxItem implements CruxItem {
    protected @NotNull ItemStack item;
    protected @NotNull FormatSerializer format;
    public SimpleCruxItem(@NotNull Material material) {
        this(new ItemStack(material));
    }
    public SimpleCruxItem(@NotNull ItemStack item) {
        this(Crux.format(), item);
    }

    public SimpleCruxItem(@NotNull FormatSerializer format, @NotNull Material material) {
        this(format, new ItemStack(material));
    }
    public SimpleCruxItem(@NotNull FormatSerializer format, @NotNull ItemStack item) {
        this.format = format;
        this.item = item;
    }

    private Component cNoItalics(@Nullable String s){
        if(s==null) return null;
        return cNoItalics(format.deserialize(s, StringTagProvider.build(format.tags().buildStringTags(item))));
    }

    private Component c(@Nullable String s){
        if(s==null) return null;
        return format.deserialize(s, StringTagProvider.build(format.tags().buildStringTags(item)));
    }

    private Component cNoItalics(@Nullable Component s){
        if(s==null) return null;
        return CruxItem.NO_ITALICS.append(s);
    }

    private List<Component> cNoItalics(@Nullable Collection<Component> s){
        if(s==null) return null;
        List<Component> newList = new ArrayList<>();
        for(Component c : s){
            newList.add(cNoItalics(c));
        }
        return newList;
    }

    public @Nullable ItemMeta meta(){
        return item.getItemMeta();
    }

    public @NotNull Optional<ItemMeta> getMeta(){
        return Optional.ofNullable(meta());
    }

    @Override
    public @Nullable Key itemModel() {
        ItemMeta meta = meta();
        if(meta == null) return null;
        return meta.hasItemModel() ? meta.getItemModel() : null;
    }

    @Override
    public SimpleCruxItem itemModel(@Nullable Key key){
        return editMeta(meta -> meta.setItemModel(key == null ? null : CruxKey.key(key)));
    }

    public SimpleCruxItem meta(@Nullable ItemMeta meta){
        item.setItemMeta(meta);
        return this;
    }

    public SimpleCruxItem addFlags(@NotNull ItemFlag @NotNull... flags){
        return editMeta(meta -> meta.addItemFlags(flags));
    }

    public SimpleCruxItem removeFlags(@NotNull ItemFlag @NotNull... flags){
        return editMeta(meta -> meta.removeItemFlags(flags));
    }

    public SimpleCruxItem addLoreFromString(@Nullable Collection<String> lore){
        if(lore==null) return this;
        FormatParserContext context = buildFormatContext();
        return addLore(context.deserializeList(lore));
    }

    public @NotNull FormatParserContext buildFormatContext(){
        return new FormatParserContext(format, null, null, format.tags().buildTags(item()));
    }

    public SimpleCruxItem addLoreFromString(@NotNull String @Nullable... lore){
        if(lore==null) return this;
        return addLoreFromString(Arrays.stream(lore).toList());
    }

    public SimpleCruxItem addLore(@Nullable Collection<Component> lore){
        return lore == null ? this : addLore(lore.toArray(new Component[0]));
    }

    public SimpleCruxItem addLore(@NotNull Component @NotNull... lore){
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

    public SimpleCruxItem material(@NotNull Material material) {
        //pre 1.20.5 this.item.setType(material);
        //post 1.20.5
        this.item = this.item.withType(material);
        return this;
    }

    public @Nullable Component displayName() {
        ItemMeta meta = meta();
        return meta==null?null : meta.displayName();
    }

    @Deprecated(forRemoval = true)
    public SimpleCruxItem displayName(@Nullable String name) {
        return displayName(cNoItalics(name));
    }

    @Deprecated(forRemoval = true)
    public SimpleCruxItem displayName(@Nullable Component name) {
        return editMeta(meta -> meta.displayName(cNoItalics(name)));
    }

    public SimpleCruxItem customName(@Nullable String name) {
        return customName(c(name));
    }

    public SimpleCruxItem customName(@Nullable Component name) {
        return editMeta(meta -> meta.displayName(name));
    }

    public SimpleCruxItem itemName(@Nullable String name) {
        return itemName(c(name));
    }

    public SimpleCruxItem itemName(@Nullable Component name) {
        return editMeta(meta -> meta.itemName(name));
    }

    public @Nullable List<Component> lore() {
        ItemMeta meta = meta();
        return meta==null?null:meta.lore();
    }

    public SimpleCruxItem loreFromString(@Nullable List<String> lore) {
        if(lore==null) return this;
        FormatParserContext context = buildFormatContext();
        return lore(context.deserializeList(lore));
    }

    public SimpleCruxItem lore(@Nullable List<Component> lore) {
        return editMeta(meta -> meta.lore(cNoItalics(lore)));
    }

    public @NotNull Collection<ItemFlag> flags() {
        ItemMeta meta = meta();
        return meta == null ? new HashSet<>() : meta.getItemFlags();
    }

    public SimpleCruxItem flags(@Nullable Collection<ItemFlag> flags) {
        return editMeta(meta ->{
            meta.removeItemFlags(ItemFlag.values());
            if(flags != null) meta.addItemFlags(flags.toArray(new ItemFlag[0]));
        });
    }

    public SimpleCruxItem customModelData(@Nullable Integer data){
        return editMeta(meta -> meta.setCustomModelData(data));
    }

    public boolean unbreakable() {
        ItemMeta meta = meta();
        return meta != null && meta.isUnbreakable();
    }

    public SimpleCruxItem unbreakable(boolean unbreakable) {
        return editMeta(meta -> meta.setUnbreakable(unbreakable));
    }

    public SimpleCruxItem glow(boolean value){
        //pre 1.20.5
        /*enchant(Enchantment.DURABILITY, 1);
        addFlags(ItemFlag.HIDE_ENCHANTS);*/

        //post 1.20.5
        editMeta(meta -> meta.setEnchantmentGlintOverride(value));
        return this;
    }

    public SimpleCruxItem enchant(@NotNull Enchantment e, int level){
        return enchant(e, level, true);
    }

    public SimpleCruxItem enchant(@NotNull Enchantment e, int level, boolean ignoreLevelRestriction){
        editMeta(meta -> meta.addEnchant(e, level, ignoreLevelRestriction));
        return this;
    }

    public SimpleCruxItem amount(int amount){
        item.setAmount(amount);
        return this;
    }

    public int amount(){
        return item.getAmount();
    }

    @Deprecated(since = "Mojang did weird thing and now hide_attributes does not hide base attributes" +
        " Keep in mind, this function will add a luck attribute to the item and add hide_attributes.")
    public SimpleCruxItem hideAttributes(){
        return editMeta(meta ->{
            meta.addAttributeModifier(Attribute.LUCK, new AttributeModifier(
                Crux.key("hide"), 0D, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HAND
            ));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        });
    }

    public SimpleCruxItem addAttribute(@NotNull Attribute attribute, @NotNull AttributeModifier modifier){
        return editMeta(meta -> meta.addAttributeModifier(attribute, modifier));
    }

    public SimpleCruxItem addAttribute(@NotNull Attribute attribute, @NotNull NamespacedKey key, double amount, @NotNull AttributeModifier.Operation operation,
                                       @Nullable EquipmentSlotGroup slot){
        return addAttribute(attribute, new AttributeModifier(key, amount, operation, slot));
    }

    public SimpleCruxItem color(@Nullable Color color){
        return editMeta(meta ->{
           if(meta instanceof LeatherArmorMeta m) m.setColor(color);
           else if(meta instanceof PotionMeta m) m.setColor(color);
           else if(meta instanceof MapMeta m) m.setColor(color);
        });
    }

    public @Nullable Color color(){
        ItemMeta meta = meta();
        if(meta==null) return null;
        if(meta instanceof LeatherArmorMeta m) m.getColor();
        if(meta instanceof PotionMeta m) m.getColor();
        if(meta instanceof MapMeta m) m.getColor();
        return null;
    }

    public SimpleCruxItem editMeta(@NotNull Consumer<ItemMeta> consumer){
        return editMeta(ItemMeta.class, consumer);
    }
    public SimpleCruxItem edit(@NotNull Consumer<ItemStack> consumer){
        consumer.accept(item);
        return this;
    }

    public <T extends ItemMeta> SimpleCruxItem editMeta(@NotNull Class<T> clazz, @NotNull Consumer<T> consumer){
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

    public SimpleCruxItem item(@NotNull ItemStack item){
        this.item = item;
        return this;
    }

    public @NotNull SimpleCruxItem clone() {
        try {
            SimpleCruxItem item = (SimpleCruxItem) super.clone();
            item.item(this.item.clone());
            return item;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public @NotNull FormatSerializer getFormat() {
        return format;
    }

    @Override
    public <T> T getOrDefaultData(DataComponentType<? extends T> type) {
        T value = get(type);
        if(value != null) return value;
        CruxItemType itemType = Crux.handlers().item().getItemType(item);
        if(itemType == null) return null;
        return itemType.getDefaultData(type);
    }

    @Override
    public boolean hasOrDefaultData(DataComponentType<?> type) {
        return getOrDefaultData(type) != null;
    }

    @Override
    public <T> Collection<T> getAllOfTypeOrDefaultData(Class<T> type) {
        CruxItemType itemType = Crux.handlers().item().getItemType(item);
        if(itemType == null || itemType.getDefaultData().isEmpty()) return getAllOfType(type);
        Collection<T> list = new ArrayList<>(getAllOfType(type));
        itemType.getDefaultData().forEach(defaultType ->{
            if(has(defaultType)) return;
            Object value = itemType.getDefaultData(defaultType);
            if(!type.isAssignableFrom(value.getClass())) return;
            list.add(type.cast(value));
        });
        return list;
    }

    @Override
    public <T> void forEachAllOfTypeOrDefaultData(Class<T> type, Consumer<T> consumer) {
        forEachAllOfType(type, consumer);
        CruxItemType itemType = Crux.handlers().item().getItemType(item);
        if(itemType == null || itemType.getDefaultData().isEmpty()){
            return;
        }
        itemType.getDefaultData().forEach(defaultType ->{
            if(has(defaultType)) return;
            Object value = itemType.getDefaultData(defaultType);
            if(!type.isAssignableFrom(value.getClass())) return;
            consumer.accept(type.cast(value));
        });
    }

    @Override
    public @Nullable PersistentDataHolder getComponentsPersistentHolder() {
        return item.getItemMeta();
    }

    @Override
    public void onComponentsPersistentContainerChanged(@NotNull PersistentDataContainer data) {
        PersistentDataHolder item = getComponentsPersistentHolder();
        CruxPersist.COMPONENTS.set(item, data.isEmpty() ? null : data);
        item().setItemMeta((ItemMeta) item);
    }

    @Override
    public void clearComponents() {
        CruxPersist.COMPONENTS.set(item, null);
    }
}
