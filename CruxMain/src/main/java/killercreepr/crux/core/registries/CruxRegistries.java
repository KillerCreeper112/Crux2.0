package killercreepr.crux.core.registries;

import killercreepr.crux.api.block.sound.CreateBlockSoundGroup;
import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.api.registry.Registry;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.registry.SimpleKeyedRegistry;
import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.crux.core.registry.SimpleRegistry;
import killercreepr.crux.api.block.tag.BlockTag;
import killercreepr.crux.api.entity.tag.EntityTag;
import killercreepr.crux.api.item.tag.ItemTag;
import killercreepr.crux.core.data.tick.CruxTick;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class CruxRegistries {
    public static final MappedRegistry<String, CruxPlugin> PLUGIN = new SimpleMappedRegistry<>(){
        @Override
        public @NotNull CruxPlugin register(@NotNull CruxPlugin object) {
            return register(object.getName(), object);
        }

        @Override
        public boolean unregister(@NotNull CruxPlugin object) {
            return remove(object.getName()) != null;
        }
    };

    public static final CruxModuleRegistry MODULES = new CruxModuleRegistry();

    public static final KeyedRegistry<CruxTick> TICK = new SimpleKeyedRegistry<>();

    public static final MappedRegistry<String, Boolean> BOOLEAN_MAPPED = SimpleMappedRegistry.fromHashMap();

    public static final KeyedRegistry<LootTable<ItemStack>> ITEM_LOOT_TABLE = new SimpleKeyedRegistry<>();
    public static final KeyedRegistry<LootTable<Key>> KEY_LOOT_TABLE = new SimpleKeyedRegistry<>();
    public static final KeyedRegistry<LootTable<NumberProvider>> NUMBER_LOOT_TABLE = new SimpleKeyedRegistry<>();

    public static final KeyedRegistry<ItemTag> ITEM_TAG = new SimpleKeyedRegistry<>();
    public static final KeyedRegistry<BlockTag> BLOCK_TAG = new SimpleKeyedRegistry<>();
    public static final KeyedRegistry<EntityTag> ENTITY_TAG = new SimpleKeyedRegistry<>();
    public static final MappedRegistry<Key, CreateBlockSoundGroup> BLOCK_SOUND_GROUP = new SimpleMappedRegistry<>();

    public static final MappedRegistry<Key, DataComponentType<?>> DATA_COMPONENT_TYPE = new SimpleMappedRegistry<>();
    public static final MappedRegistry<Class<?>, ComponentTextInputParser<?>> DATA_COMPONENT_TEXT_PARSER_TYPE = new SimpleMappedRegistry<>();

    public static final Registry<PersistentDataType<?, ?>> PERSISTENT_DATA_TYPE = SimpleRegistry.fromSet(
        PersistentDataType.BYTE,
        PersistentDataType.SHORT,
        PersistentDataType.INTEGER,
        PersistentDataType.LONG,
        PersistentDataType.FLOAT,
        PersistentDataType.DOUBLE,
        PersistentDataType.STRING,
        PersistentDataType.BYTE_ARRAY,
        PersistentDataType.INTEGER_ARRAY,
        PersistentDataType.LONG_ARRAY,
        PersistentDataType.TAG_CONTAINER,

        PersistentDataType.LIST.booleans(),
        PersistentDataType.LIST.dataContainers(),
        PersistentDataType.LIST.longs(),
        PersistentDataType.LIST.bytes(),
        PersistentDataType.LIST.doubles(),
        PersistentDataType.LIST.floats(),
        PersistentDataType.LIST.strings(),

        //custom
        CruxPersistence.CRUX_KEY,
        CruxPersistence.BLOCK_POS,
        CruxPersistence.UUID,
        CruxPersistence.LOCATION,
        CruxPersistence.VECTOR,
        CruxPersistence.POTION_EFFECT,

        CruxPersistence.LIST.LOCATION,
        CruxPersistence.LIST.UUID,
        CruxPersistence.LIST.POTION_EFFECT
    );
}
