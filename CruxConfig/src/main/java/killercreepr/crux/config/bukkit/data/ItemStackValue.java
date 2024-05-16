package killercreepr.crux.config.bukkit.data;

import killercreepr.crux.Crux;
import killercreepr.crux.config.bukkit.file.CruxConfig;
import killercreepr.crux.config.bukkit.value.ConfigValue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class ItemStackValue extends ConfigValue<ItemStack> {
    private final static Component NO_ITALICS = Component.empty().decoration(TextDecoration.ITALIC, false);
    public ItemStackValue(@Nullable ItemStack defaultValue) {
        super(ItemStack.class, defaultValue);
    }

    public ItemStackValue() {
        this(null);
    }

    @Override
    public @Nullable ItemStack get(@NotNull CruxConfig crux, @NotNull String path) {
        path = addDot(path);
        FileConfiguration cfg = crux.config();
        //todo Deprecated for now
        /*if(cfg.isString(path + "base")){
            String s = cfg.getString(path + "base");
            if(s != null && crux instanceof ItemConfig cruxItem) return cruxItem.getBase(path, s);
        }*/

        String s = cfg.getString(path + "material");
        if(s == null) return null;
        Material m;
        try{ m = Material.valueOf(s.toUpperCase()); }
        catch (IllegalArgumentException e){
            Crux.log(Level.WARNING, "Material does not exist!: '" + s + "' from file: " + crux.file().getName());
            return null;
        }
        return get(crux, path, new ItemStack(m, cfg.getInt(path + "amount", 1)));
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable ItemStack object) {

    }

    public @Nullable ItemStack get(@NotNull CruxConfig crux, @NotNull String path, @Nullable ItemStack i){
        if(!crux.file().exists()) return null;
        path = addDot(path);
        FileConfiguration cfg = crux.config();
        String s;
        if(cfg.isString(path + "material")){
            s = cfg.getString(path + "material");
            if(s != null){
                if(i == null) i = new ItemStack(Material.AIR);
                try{ i.setType(Material.valueOf(s.toUpperCase())); }
                catch (IllegalArgumentException e){
                    Crux.log(Level.WARNING, "Material does not exist!: '" + s + "' (" + crux.file().getAbsolutePath() + ")");
                    return null;
                }
            }
        }
        if(i == null) return null;
        final ItemMeta meta = i.getItemMeta();
        if(meta == null) return i;
        if(cfg.isString(path + "name")){
            s = cfg.getString(path + "name");
            if(s != null){
                meta.displayName(NO_ITALICS.append(MiniMessage.miniMessage().deserialize(s)));
            }
        }
        if(cfg.isList(path + "lore")){
            List<Component> lore = new ArrayList<>();
            for(String e : cfg.getStringList(path + "lore")){
                lore.add(NO_ITALICS.append(MiniMessage.miniMessage().deserialize(e)));
            }
            meta.lore(lore.isEmpty() ? null : lore);
        }

        if(cfg.isInt(path + "custom_model_data")){
            int x = cfg.getInt(path + "custom_model_data");
            meta.setCustomModelData(x == 0 ? null : x);
        }
        if(cfg.isList(path + "flags")){
            for(String flagName : cfg.getStringList(path + "flags")){
                try{
                    ItemFlag flag = ItemFlag.valueOf(flagName.toUpperCase());
                    meta.addItemFlags(flag);
                }catch (IllegalArgumentException e){
                    Crux.log(Level.WARNING, "Flag '" + flagName + "' not found! Values you can use:" + Arrays.asList(ItemFlag.values())
                            + "(" + crux.file().getAbsolutePath() + ")");
                }
            }
        }
        if(cfg.getBoolean(path + "hide_flags")){
            meta.addItemFlags(ItemFlag.values());
        }else meta.removeItemFlags(ItemFlag.values());
        if(cfg.isBoolean(path + "unbreakable")) meta.setUnbreakable(cfg.getBoolean(path + "unbreakable"));

        ConfigurationSection section = cfg.getConfigurationSection(path + "enchants");
        if(section != null){
            for(String x : section.getKeys(false)){
                Enchantment e = Enchantment.getByKey(new NamespacedKey(NamespacedKey.MINECRAFT, x.toLowerCase()));
                if(e == null){
                    Crux.log(Level.WARNING, "No enchantment of '" + x + "' exists (at file: " + crux.file().getName() + ")");
                    continue;
                }
                int level = cfg.getInt(path + "enchants." + x);
                if(level == 0) continue;
                meta.addEnchant(e, level, true);
            }
        }

        section = cfg.getConfigurationSection(path + "attributes");
        if(section != null){
            for(String x : section.getKeys(false)){
                Attribute a;
                try{
                    a = Attribute.valueOf(x.toUpperCase());
                }catch (IllegalArgumentException e){
                    Crux.log(Level.WARNING, "Attribute of '" + x + "' not found! (" + crux.file().getAbsolutePath() + ") Values you can use: " +
                            Arrays.asList(Attribute.values()));
                    continue;
                }

                ConfigurationSection subSection = cfg.getConfigurationSection(path + "attributes." + x);
                if(subSection == null) continue;
                for(String subX : subSection.getKeys(false)){
                    AttributeModifier m = new AttributeModifierValue().get(crux, path + "attributes." + x + "." + subX + ".");
                    if(m != null) meta.addAttributeModifier(a, m);
                }
            }
        }

        if(cfg.isString(path + "color")){
            Color c = hexToColor(cfg.getString(path + "color", ""));
            if(meta instanceof LeatherArmorMeta l){
                l.setColor(c);
            }else if(meta instanceof PotionMeta l){
                l.setColor(c);
            }else if(meta instanceof FireworkEffectMeta l){
                l.setEffect(FireworkEffect.builder().withColor(c).build());
            }
        }
        i.setItemMeta(meta);

        //done with meta
        i.setAmount(cfg.getInt(path + "amount", i.getAmount()));
        return i;
    }

    public static Color hexToColor(@NotNull String hex){
        return Color.fromRGB(Integer.valueOf(hex.substring( 1, 3 ), 16 ),
                Integer.valueOf(hex.substring( 3, 5 ), 16 ),
                Integer.valueOf(hex.substring( 5, 7 ), 16 ));
    }
}
