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
    public static void initYaml(@NotNull YamlRegistry registry){
        registry.registerHandler(PotionEffect.class, new YamlPotionEffect());
        registry.registerHandler(NumberProvider.class, new YamlNumberProvider());
        registry.registerHandler(MsgContainer.class, new YamlMsgContainer());
        registry.registerHandler(CreateSound.class, new YamlCreateSound());
        registry.registerHandler(CreateTitle.class, new YamlCreateTitle());
        registry.registerHandler(PotionEffectType.class, new YamlPotionEffectType());
        registry.registerHandler(Material.class, new YamlMaterial());
        registerEnums(registry,
                AttributeModifier.Operation.class,
                EquipmentSlot.class,
                World.Environment.class,
                ItemRarity.class
        );
        registry.registerHandler(TrimMaterial.class, new YamlGenericKeyedRegistry<>(RegistryKey.TRIM_MATERIAL));
        registry.registerHandler(TrimPattern.class, new YamlGenericKeyedRegistry<>(RegistryKey.TRIM_PATTERN));
        registry.registerHandler(new AutoYamlSerializer<>(ArmorTrim.class));
        registry.registerHandler(Component.class, new YamlComponent());
    }

    @SafeVarargs
    public static void registerEnums(@NotNull YamlRegistry registry, @NotNull Class<? extends Enum<?>>... enums){
        for(Class<? extends Enum<?>> e : enums){
            registry.registerHandler(e, new YamlGenericEnum(e));
        }
    }
}
