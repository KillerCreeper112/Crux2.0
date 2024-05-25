package killercreepr.cruxconfig.config.bukkit.handlers;

import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.data.CreateSound;
import killercreepr.crux.data.CreateTitle;
import killercreepr.crux.data.MsgContainer;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.bukkit.yaml.handler.*;
import killercreepr.cruxconfig.config.common.json.JsonRegistry;
import killercreepr.cruxconfig.config.common.json.container.GenericJsonHandler;
import killercreepr.cruxconfig.config.common.yaml.automatic.AutoYamlSerializer;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BukkitCfgHandlers {
    public static void initJson(@NotNull JsonRegistry registry){
        registry.registerContainerHandler(
                new GenericJsonHandler<>("vector", Vector.class),
                new GenericJsonHandler<>("uuid", UUID.class)
        );
    }

    public static final YamlPotionEffect POTION_EFFECT = new YamlPotionEffect();
    public static final YamlNumberProvider NUMBER_PROVIDER = new YamlNumberProvider();
    public static final YamlMsgContainer MSG_CONTAINER = new YamlMsgContainer();
    public static final YamlCreateSound CREATE_SOUND = new YamlCreateSound();
    public static final YamlCreateTitle CREATE_TITLE = new YamlCreateTitle();
    public static final YamlPotionEffectType POTION_EFFECT_TYPE = new YamlPotionEffectType();
    public static final YamlMaterial MATERIAL = new YamlMaterial();
    public static final YamlComponent COMPONENT = new YamlComponent();
    public static final YamlItemStack ITEM_STACK = new YamlItemStack();

    public static void initYaml(@NotNull YamlRegistry registry){
        registry.registerHandler(PotionEffect.class, POTION_EFFECT);
        registry.registerHandler(NumberProvider.class, NUMBER_PROVIDER);
        registry.registerHandler(MsgContainer.class, MSG_CONTAINER);
        registry.registerHandler(CreateSound.class, CREATE_SOUND);
        registry.registerHandler(CreateTitle.class, CREATE_TITLE);
        registry.registerHandler(PotionEffectType.class, POTION_EFFECT_TYPE);
        registry.registerHandler(Material.class, MATERIAL);
        registry.registerHandler(Component.class, COMPONENT);
        registry.registerHandler(ItemStack.class, ITEM_STACK);

        registry.registerHandler(TrimMaterial.class, new YamlGenericKeyedRegistry<>(RegistryKey.TRIM_MATERIAL));
        registry.registerHandler(TrimPattern.class, new YamlGenericKeyedRegistry<>(RegistryKey.TRIM_PATTERN));
        registry.registerHandler(new AutoYamlSerializer<>(ArmorTrim.class));

        registerEnums(registry,
                AttributeModifier.Operation.class,
                EquipmentSlot.class,
                World.Environment.class,
                ItemRarity.class
        );
    }

    @SafeVarargs
    public static void registerEnums(@NotNull YamlRegistry registry, @NotNull Class<? extends Enum<?>>... enums){
        for(Class<? extends Enum<?>> e : enums){
            registry.registerHandler(e, new YamlGenericEnum(e));
        }
    }
}
