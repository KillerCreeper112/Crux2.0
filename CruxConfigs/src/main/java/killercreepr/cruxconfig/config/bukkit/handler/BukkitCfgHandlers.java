package killercreepr.cruxconfig.config.bukkit.handler;

import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.data.BlockPos;
import killercreepr.crux.data.LocationPos;
import killercreepr.crux.data.StoredChunk;
import killercreepr.crux.data.StoredWorld;
import killercreepr.crux.data.communication.CreateSound;
import killercreepr.crux.data.communication.CreateTitle;
import killercreepr.crux.data.communication.MsgContainer;
import killercreepr.crux.item.ItemHolder;
import killercreepr.crux.item.StoredItem;
import killercreepr.crux.item.dynamic.BukkitDynamicItem;
import killercreepr.crux.item.dynamic.components.DynamicPersistentTag;
import killercreepr.crux.loot.api.conditions.LootCondition;
import killercreepr.crux.loot.api.functions.LootFunction;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.bukkit.handler.impl.*;
import killercreepr.cruxconfig.config.bukkit.handler.impl.item.FileDynamicItem;
import killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.FileDynamicPersistentTag;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileLootCondition;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileLootFunction;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.StandardFileLootConditions;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.StandardFileLootFunctions;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.handler.AutoFileHandler;
import killercreepr.cruxconfig.config.common.json.JsonRegistry;
import killercreepr.cruxconfig.config.common.json.container.GenericJsonHandler;
import killercreepr.cruxconfig.config.common.yaml.automatic.AutoYamlSerializer;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.UUID;

public class BukkitCfgHandlers {
    public static final FilePotionEffect POTION_EFFECT = new FilePotionEffect();
    public static final FileNumberProvider NUMBER_PROVIDER = new FileNumberProvider();
    public static final FileMsgContainer MSG_CONTAINER = new FileMsgContainer();
    public static final FileCreateSound CREATE_SOUND = new FileCreateSound();
    public static final FileCreateTitle CREATE_TITLE = new FileCreateTitle();
    public static final FilePotionEffectType POTION_EFFECT_TYPE = new FilePotionEffectType();
    public static final FileMaterial MATERIAL = new FileMaterial();
    public static final FileEntityType ENTITY_TYPE = new FileEntityType();
    public static final FileComponent COMPONENT = new FileComponent();
    public static final FileItemStack ITEM_STACK = new FileItemStack();
    public static final FileDynamicItem DYNAMIC_ITEM = new FileDynamicItem();
    public static final FileUUID UUID = new FileUUID();
    public static final FileColor COLOR = new FileColor();
    public static final FileKey KEY = new FileKey();
    public static final FileItemHolder ITEM_HOLDER = new FileItemHolder();
    public static final FileInstant INSTANT = new FileInstant();
    public static final FileStoredItem STORED_ITEM = new FileStoredItem();
    public static final FileDynamicPersistentTag DYNAMIC_PERSISTENT_TAG = new FileDynamicPersistentTag();
    public static final FileLootCondition LOOT_CONDITION = new FileLootCondition();
    public static final FileLootFunction LOOT_FUNCTION = new FileLootFunction();

    public static void initJson(@NotNull JsonRegistry registry){
        registry.registerHandler(
                new GenericJsonHandler<>("vector", Vector.class),
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
        //registry.registerHandler(ItemStack.class, ITEM_STACK);
        registry.registerHandler(BukkitDynamicItem.class, DYNAMIC_ITEM);
        registry.registerHandler(UUID.class, UUID);
        registry.registerHandler(Color.class, COLOR);
        registry.registerHandler(Key.class, KEY);
        registry.registerHandler(ItemHolder.class, ITEM_HOLDER);
        registry.registerHandler(Instant.class, INSTANT);
        registry.registerHandler(EntityType.class, ENTITY_TYPE);
        registry.registerHandler(StoredItem.class, STORED_ITEM);
        registry.registerHandler(DynamicPersistentTag.class, DYNAMIC_PERSISTENT_TAG);
        registry.registerHandler(LootCondition.class, LOOT_CONDITION);
        registry.registerHandler(LootFunction.class, LOOT_FUNCTION);
        StandardFileLootConditions.register(LOOT_CONDITION);
        StandardFileLootFunctions.register(LOOT_FUNCTION);

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
            PotionEffectType.Category.class,
            Particle.class,
            Instrument.class,
            BlockFace.class,
            Axis.class
        );

        registry.registerHandler(StoredChunk.class, new AutoFileHandler<>(StoredChunk.class));
        registry.registerHandler(StoredWorld.class, new AutoFileHandler<>(StoredWorld.class));
        registry.registerHandler(BlockPos.class, new AutoFileHandler<>(BlockPos.class));
        registry.registerHandler(LocationPos.class, new AutoFileHandler<>(LocationPos.class));
    }

    public static void initYaml(@NotNull YamlRegistry registry){
        registry.registerHandler(new AutoYamlSerializer<>(ArmorTrim.class));
    }

    @SafeVarargs
    public static void registerEnums(@NotNull FileRegistry registry, @NotNull Class<? extends Enum<?>>... enums){
        for(Class<? extends Enum<?>> e : enums){
            registry.registerHandler(e, new FileGenericEnum(e));
        }
    }
}
