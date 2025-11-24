package killercreepr.cruxconfig.config.bukkit.handler;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.api.block.CruxBlockWrapper;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.block.sound.CreateBlockSoundGroup;
import killercreepr.crux.api.block.tag.BlockTag;
import killercreepr.crux.api.communication.CreateSound;
import killercreepr.crux.api.communication.CreateTitle;
import killercreepr.crux.api.communication.boss.CreateBossBar;
import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.api.data.ParticleBuilderSupplier;
import killercreepr.crux.api.data.User;
import killercreepr.crux.api.data.WorldLocation;
import killercreepr.crux.api.data.WorldPosition;
import killercreepr.crux.api.data.world.StoredChunk;
import killercreepr.crux.api.data.world.StoredWorld;
import killercreepr.crux.api.enchantment.CruxLevelBasedValue;
import killercreepr.crux.api.enchantment.DropFormula;
import killercreepr.crux.api.entity.CruxEntitySnapshot;
import killercreepr.crux.api.entity.predicate.EntityPredicate;
import killercreepr.crux.api.entity.tag.EntityTag;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.item.dynamic.component.persistence.TypedDynamicPersistentTag;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import killercreepr.crux.api.item.tag.ItemTag;
import killercreepr.crux.api.key.tag.KeyTag;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.loot.entity.CruxEntitySnapshotLootTable;
import killercreepr.crux.api.loot.item.ItemLootFunction;
import killercreepr.crux.api.loot.item.ItemLootPool;
import killercreepr.crux.api.loot.item.ItemLootPoolObject;
import killercreepr.crux.api.loot.item.ItemLootTable;
import killercreepr.crux.api.loot.key.KeyLootTable;
import killercreepr.crux.api.loot.number.NumberLootTable;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.api.valueproviders.vector.NumberVector;
import killercreepr.crux.api.world.predicate.WorldPredicate;
import killercreepr.crux.core.communication.MsgContainer;
import killercreepr.crux.core.data.SimpleWorldLocation;
import killercreepr.crux.core.data.SimpleWorldPosition;
import killercreepr.crux.core.item.StoredItem;
import killercreepr.crux.core.item.dynamic.component.attribute.DynamicAttributeModifier;
import killercreepr.crux.core.loot.item.SimpleItemLootObject;
import killercreepr.crux.core.math.BlockPos;
import killercreepr.crux.core.math.LocationPos;
import killercreepr.crux.paper.ItemHolder;
import killercreepr.cruxconfig.config.bukkit.handler.impl.*;
import killercreepr.cruxconfig.config.bukkit.handler.impl.component.FileDataComponentAccessor;
import killercreepr.cruxconfig.config.bukkit.handler.impl.component.FileDataComponentHandler;
import killercreepr.cruxconfig.config.bukkit.handler.impl.component.FileTypedDataComponent;
import killercreepr.cruxconfig.config.bukkit.handler.impl.component.StandardCfgDataComponentTypes;
import killercreepr.cruxconfig.config.bukkit.handler.impl.item.FileDynamicItem;
import killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.FileDynamicPersistentTag;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.*;
import killercreepr.cruxconfig.config.bukkit.standard.CommonLootTableHandlers;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.handler.AutoFileHandler;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
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
import org.bukkit.potion.PotionType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.UUID;

public class BukkitCfgHandlers {
    public static final FilePotionEffect POTION_EFFECT = new FilePotionEffect();
    public static final FileNumberProvider NUMBER_PROVIDER = new FileNumberProvider();
    public static final FileNumberVector NUMBER_VECTOR = new FileNumberVector();
    public static final FileMsgContainer MSG_CONTAINER = new FileMsgContainer();
    public static final FileCreateSound CREATE_SOUND = new FileCreateSound();
    public static final FileCreateTitle CREATE_TITLE = new FileCreateTitle();
    public static final FileCreateBossBar CREATE_BOSS_BAR = new FileCreateBossBar();
    public static final FilePotionEffectType POTION_EFFECT_TYPE = new FilePotionEffectType();
    public static final FilePotionType POTION_TYPE = new FilePotionType();
    public static final FileMaterial MATERIAL = new FileMaterial();
    public static final FileEntityType ENTITY_TYPE = new FileEntityType();
    public static final FileComponent COMPONENT = new FileComponent();
    //todo not used public static final FileItemStack ITEM_STACK = new FileItemStack();
    public static final FileDynamicItem DYNAMIC_ITEM = new FileDynamicItem();
    public static final FileUUID UUID = new FileUUID();
    public static final FileColor COLOR = new FileColor();
    public static final FileKey KEY = new FileKey();
    public static final FileItemHolder ITEM_HOLDER = new FileItemHolder();
    public static final FileInstant INSTANT = new FileInstant();
    public static final FileStoredItem STORED_ITEM = new FileStoredItem();
    public static final FileDynamicPersistentTag DYNAMIC_PERSISTENT_TAG = new FileDynamicPersistentTag();
    public static final FileLootCondition LOOT_CONDITION = new FileLootCondition();
    public static final FileItemLootFunction ITEM_LOOT_FUNCTION = new FileItemLootFunction();
    public static final FileItemLootTable ITEM_LOOT_TABLE = new FileItemLootTable();
    public static final FileItemLootPool ITEM_LOOT_POOL = new FileItemLootPool();
    public static final FileSimpleItemLootObject SIMPLE_LOOT_OBJECT = new FileSimpleItemLootObject();
    public static final FileItemLootPoolObject ITEM_LOOT_POOL_OBJECT = new FileItemLootPoolObject();
    public static final FileCreateBlockSoundGroup CREATE_BLOCK_SOUND_GROUP = new FileCreateBlockSoundGroup();
    public static final FileItemTag ITEM_TAG = new FileItemTag();
    public static final FileItemPredicate ITEM_PREDICATE = new FileItemPredicate();
    public static final FileBlockTag BLOCK_TAG = new FileBlockTag();
    public static final FileEntityTag ENTITY_TAG = new FileEntityTag();
    public static final FileKeyTag KEY_TAG = new FileKeyTag();
    public static final FileBlockPredicate BLOCK_PREDICATE = new FileBlockPredicate();
    public static final FileEntityPredicate ENTITY_PREDICATE = new FileEntityPredicate();
    public static final FileWorldPredicate WORLD_PREDICATE = new FileWorldPredicate();
    public static final FileCruxBlockWrapper CRUX_BLOCK_WRAPPER = new FileCruxBlockWrapper();
    public static final FileTypedDataComponent TYPED_DATA_COMPONENT = new FileTypedDataComponent();
    public static final FileDataComponentHandler DATA_COMPONENT_HANDLER = new FileDataComponentHandler();
    public static final FileDataComponentAccessor DATA_COMPONENT_ACCESSOR = new FileDataComponentAccessor();
    public static final FileCruxEntitySnapshot CRUX_ENTITY_SNAPSHOT = new FileCruxEntitySnapshot();
    public static final FileVector VECTOR = new FileVector();
    public static final FileUser USER = new FileUser();
    public static final FileDynamicAttributeModifier DYNAMIC_ATTRIBUTE_MODIFIER = new FileDynamicAttributeModifier();
    public static final FileCruxLocation CRUX_LOCATION = new FileCruxLocation();
    public static final FileDropFormula DROP_FORMULA = new FileDropFormula();
    public static final FileParticle PARTICLE = new FileParticle();
    public static final FileParticleBuilderSupplier PARTICLE_BUILDER_SUPPLIER = new FileParticleBuilderSupplier();
    public static final FileGameRule GAME_RULE = new FileGameRule();
    public static final FileAttribute ATTRIBUTE = new FileAttribute();
    public static final FileAttributeModifier ATTRIBUTE_MODIFIER = new FileAttributeModifier();
    public static final FileNamedTextColor NAMED_TEXT_COLOR = new FileNamedTextColor();


    public static final FileCruxLevelBasedValue CRUX_LEVEL_BASED_VALUE = new FileCruxLevelBasedValue();

    public static final FileStoredWorld STORED_WORLD = new FileStoredWorld();
    public static final FileStoredChunk STORED_CHUNK = new FileStoredChunk();

    public static void initStandard(){
        CfgRegistries.FILE.forEach(BukkitCfgHandlers::init);
    }

    public static void init(@NotNull FileRegistry registry){
        registry.registerFileHandler(PotionEffect.class, POTION_EFFECT);
        registry.registerFileHandler(NumberProvider.class, NUMBER_PROVIDER);
        registry.registerFileHandler(MsgContainer.class, MSG_CONTAINER);
        registry.registerFileHandler(CreateSound.class, CREATE_SOUND);
        registry.registerFileHandler(CreateTitle.class, CREATE_TITLE);
        registry.registerFileHandler(CreateBossBar.class, CREATE_BOSS_BAR);
        registry.registerFileHandler(PotionEffectType.class, POTION_EFFECT_TYPE);
        registry.registerFileHandler(Material.class, MATERIAL);
        registry.registerFileHandler(Component.class, COMPONENT);
        //registry.registerHandler(ItemStack.class, ITEM_STACK);
        registry.registerFileHandler(DynamicItem.class, DYNAMIC_ITEM);
        registry.registerFileHandler(UUID.class, UUID);
        registry.registerFileHandler(Color.class, COLOR);
        registry.registerFileHandler(Key.class, KEY);
        registry.registerFileHandler(ItemHolder.class, ITEM_HOLDER);
        registry.registerFileHandler(Instant.class, INSTANT);
        registry.registerFileHandler(EntityType.class, ENTITY_TYPE);
        registry.registerFileHandler(StoredItem.class, STORED_ITEM);
        registry.registerFileHandler(TypedDynamicPersistentTag.class, DYNAMIC_PERSISTENT_TAG);
        registry.registerFileHandler(ItemLootTable.class, ITEM_LOOT_TABLE);
        registry.registerFileHandler(ItemLootPool.class, ITEM_LOOT_POOL);
        registry.registerFileHandler(LootCondition.class, LOOT_CONDITION);
        registry.registerFileHandler(ItemLootFunction.class, ITEM_LOOT_FUNCTION);
        registry.registerFileHandler(ItemLootPoolObject.class, ITEM_LOOT_POOL_OBJECT);
        registry.registerFileHandler(CreateBlockSoundGroup.class, CREATE_BLOCK_SOUND_GROUP);
        registry.registerFileHandler(ItemTag.class, ITEM_TAG);
        registry.registerFileHandler(ItemPredicate.class, ITEM_PREDICATE);
        registry.registerFileHandler(BlockTag.class, BLOCK_TAG);
        registry.registerFileHandler(EntityTag.class, ENTITY_TAG);
        registry.registerFileHandler(KeyTag.class, KEY_TAG);
        registry.registerFileHandler(BlockPredicate.class, BLOCK_PREDICATE);
        registry.registerFileHandler(EntityPredicate.class, ENTITY_PREDICATE);
        registry.registerFileHandler(CruxBlockWrapper.class, CRUX_BLOCK_WRAPPER);
        registry.registerFileHandler(CruxEntitySnapshot.class, CRUX_ENTITY_SNAPSHOT);
        registry.registerFileHandler(Vector.class, VECTOR);
        registry.registerFileHandler(CruxLocation.class, CRUX_LOCATION);
        registry.registerFileHandler(NumberVector.class, NUMBER_VECTOR);
        registry.registerFileHandler(DynamicAttributeModifier.class, DYNAMIC_ATTRIBUTE_MODIFIER);
        registry.registerFileHandler(KeyLootTable.class, CommonLootTableHandlers.KEY);
        registry.registerFileHandler(NumberLootTable.class, CommonLootTableHandlers.NUMBER);
        registry.registerFileHandler(CruxEntitySnapshotLootTable.class, CommonLootTableHandlers.CRUX_ENTITY_SNAPSHOT);
        registry.registerFileHandler(User.class, USER);
        registry.registerFileHandler(CruxLevelBasedValue.class, CRUX_LEVEL_BASED_VALUE);
        registry.registerFileHandler(DropFormula.class, DROP_FORMULA);
        registry.registerFileHandler(Particle.class, PARTICLE);
        registry.registerFileHandler(ParticleBuilderSupplier.class, PARTICLE_BUILDER_SUPPLIER);
        registry.registerFileHandler(PotionType.class, POTION_TYPE);
        registry.registerFileHandler(GameRule.class, GAME_RULE);
        registry.registerFileHandler(Attribute.class, ATTRIBUTE);
        registry.registerFileHandler(AttributeModifier.class, ATTRIBUTE_MODIFIER);
        registry.registerFileHandler(WorldPredicate.class, WORLD_PREDICATE);
        registry.registerFileHandler(NamedTextColor.class, NAMED_TEXT_COLOR);

        registry.registerFileHandler(StoredWorld.class, STORED_WORLD);
        registry.registerFileHandler(StoredChunk.class, STORED_CHUNK);

        StandardCfgDataComponentTypes.register(TYPED_DATA_COMPONENT.typeHandlers());
        registry.registerFileHandler(TypedDataComponent.class, TYPED_DATA_COMPONENT);
        registry.registerFileHandler(DataComponentHandler.class, DATA_COMPONENT_HANDLER);
        registry.registerFileHandler(DataComponentAccessor.class, DATA_COMPONENT_ACCESSOR);

        StandardFileLootConditions.register(LOOT_CONDITION);
        StandardFileLootFunctions.register(ITEM_LOOT_FUNCTION);
        StandardFileLootPoolObjects.register(ITEM_LOOT_POOL_OBJECT);

        registry.registerFileHandler(SimpleItemLootObject.class, SIMPLE_LOOT_OBJECT);

        DYNAMIC_ITEM.registerComponents(registry);

        registry.registerFileHandler(TrimMaterial.class, new FileGenericKeyedRegistry<>(RegistryKey.TRIM_MATERIAL){
            @Override
            public @NotNull String jsonSerializerID() {
                return "trim_material";
            }
        });
        registry.registerFileHandler(TrimPattern.class, new FileGenericKeyedRegistry<>(RegistryKey.TRIM_PATTERN){
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
            Instrument.class,
            BlockFace.class,
            Axis.class
        );

        registry.registerFileHandler(BlockPos.class, new AutoFileHandler<>(BlockPos.class));
        registry.registerFileHandler(LocationPos.class, new AutoFileHandler<>(LocationPos.class));
        registry.registerFileHandler(WorldPosition.class, new AutoFileHandler<>(SimpleWorldPosition.class));
        registry.registerFileHandler(WorldLocation.class, new AutoFileHandler<>(SimpleWorldLocation.class));
        registry.registerFileHandler(ArmorTrim.class, new AutoFileHandler<>(ArmorTrim.class));
        registry.registerFileHandler(BoundingBox.class, new AutoFileHandler<>(BoundingBox.class));
    }

    @SafeVarargs
    public static void registerEnums(@NotNull FileRegistry registry, @NotNull Class<? extends Enum<?>>... enums){
        for(Class<? extends Enum<?>> e : enums){
            registry.registerFileHandler(e, new FileGenericEnum(e));
        }
    }
}
