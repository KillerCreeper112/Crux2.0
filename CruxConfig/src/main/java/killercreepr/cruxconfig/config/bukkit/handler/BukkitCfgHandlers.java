package killercreepr.cruxconfig.config.bukkit.handler;

import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.data.CreateSound;
import killercreepr.crux.data.CreateTitle;
import killercreepr.crux.data.MsgContainer;
import killercreepr.crux.item.BukkitDynamicItem;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.bukkit.handler.impl.*;
import killercreepr.cruxconfig.config.bukkit.handler.impl.item.FileDynamicItem;
import killercreepr.cruxconfig.config.common.FileRegistry;
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
    public static final FilePotionEffect POTION_EFFECT = new FilePotionEffect();
    public static final FileNumberProvider NUMBER_PROVIDER = new FileNumberProvider();
    public static final FileMsgContainer MSG_CONTAINER = new FileMsgContainer();
    public static final FileCreateSound CREATE_SOUND = new FileCreateSound();
    public static final FileCreateTitle CREATE_TITLE = new FileCreateTitle();
    public static final FilePotionEffectType POTION_EFFECT_TYPE = new FilePotionEffectType();
    public static final FileMaterial MATERIAL = new FileMaterial();
    public static final FileComponent COMPONENT = new FileComponent();
    public static final FileItemStack ITEM_STACK = new FileItemStack();
    public static final FileDynamicItem DYNAMIC_ITEM = new FileDynamicItem();

    public static void initJson(@NotNull JsonRegistry registry){
        registry.registerHandler(
                new GenericJsonHandler<>("vector", Vector.class),
                new GenericJsonHandler<>("uuid", UUID.class),
                new GenericJsonHandler<>("potion_effect", PotionEffect.class)
        );
    }

    public static void init(@NotNull FileRegistry registry){
        registry.registerHandler(PotionEffect.class, POTION_EFFECT);
        registry.registerHandler(NumberProvider.class, NUMBER_PROVIDER);
        registry.registerHandler(MsgContainer.class, MSG_CONTAINER);
        registry.registerHandler(CreateSound.class, CREATE_SOUND);
        registry.registerHandler(CreateTitle.class, CREATE_TITLE);
        registry.registerHandler(PotionEffectType.class, POTION_EFFECT_TYPE);
        registry.registerHandler(Material.class, MATERIAL);
        registry.registerHandler(Component.class, COMPONENT);
        registry.registerHandler(ItemStack.class, ITEM_STACK);
        registry.registerHandler(BukkitDynamicItem.class, DYNAMIC_ITEM);
        DYNAMIC_ITEM.registerComponents(registry);

        registry.registerHandler(TrimMaterial.class, new FileGenericKeyedRegistry<>(RegistryKey.TRIM_MATERIAL){
            @Override
            public @NotNull String jsonSerializerID() {
                return "trim_material";
            }
        });
        registry.registerHandler(TrimPattern.class, new FileGenericKeyedRegistry<>(RegistryKey.TRIM_PATTERN){
            @Override
            public @NotNull String jsonSerializerID() {
                return "trim_pattern";
            }
        });

        registerEnums(registry,
                AttributeModifier.Operation.class,
                EquipmentSlot.class,
                World.Environment.class,
                ItemRarity.class,
                PotionEffectType.Category.class
        );
    }

    public static void initYaml(@NotNull YamlRegistry registry){
        registry.registerHandler(new AutoYamlSerializer<>(ArmorTrim.class));
    }

    @SafeVarargs
    public static void registerEnums(@NotNull FileRegistry registry, @NotNull Class<? extends Enum<?>>... enums){
        for(Class<? extends Enum<?>> e : enums){
            registry.registerHandler(e, new FileGenericEnum(e){
                @Override
                public @NotNull String jsonSerializerID() {
                    return e.getSimpleName().toLowerCase();
                }
            });
        }
    }
}
