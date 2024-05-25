package killercreepr.cruxconfig.config.bukkit.handlers;

import killercreepr.crux.data.CreateSound;
import killercreepr.crux.data.CreateTitle;
import killercreepr.crux.data.MsgContainer;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.bukkit.yaml.handler.*;
import killercreepr.cruxconfig.config.common.json.JsonRegistry;
import killercreepr.cruxconfig.config.common.json.container.GenericJsonHandler;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
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
                World.Environment.class
        );
    }

    @SafeVarargs
    public static void registerEnums(@NotNull YamlRegistry registry, @NotNull Class<? extends Enum<?>>... enums){
        for(Class<? extends Enum<?>> e : enums){
            registry.registerHandler(e, new YamlGenericEnum(e));
        }
    }
}
