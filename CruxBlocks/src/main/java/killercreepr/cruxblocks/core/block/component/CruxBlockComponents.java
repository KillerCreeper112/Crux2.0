package killercreepr.cruxblocks.core.block.component;

import killercreepr.crux.api.block.sound.CreateBlockSoundGroup;
import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.api.component.serialization.ComponentSerializer;
import killercreepr.crux.api.component.serialization.PersistentDataSerializer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.persistence.CruxPersist;
import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxblocks.api.block.component.BushBlock;
import killercreepr.cruxblocks.api.block.component.BushGroup;
import killercreepr.cruxblocks.api.block.component.DirectionalBlock;
import killercreepr.cruxblocks.api.block.component.DirectionalGroup;
import killercreepr.cruxblocks.api.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.core.block.component.standard.EntitySpawnerComponent;
import killercreepr.cruxblocks.core.components.persistence.BlocksDynamicPersistence;
import killercreepr.cruxblocks.core.persistence.CruxBlocksPersistTags;
import killercreepr.cruxblocks.core.persistence.CruxBlocksPersistence;
import killercreepr.cruxblocks.core.registries.CruxBlocksRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.UnaryOperator;

public class CruxBlockComponents {
    public static final DataComponentType<DirectionalBlock> DIRECTIONAL_BLOCK = register("directional_block", builder ->
        builder);
    public static final DataComponentType<DirectionalGroup> DIRECTIONAL_GROUP = register("directional_group", builder ->
        builder);

    public static final DataComponentType<BushBlock> BUSH_BLOCK = register("bush_block", builder ->
        builder);
    public static final DataComponentType<BushGroup> BUSH_GROUP = register("bush_group", builder ->
        builder);

    public static final DataComponentType<CreateBlockSoundGroup> BLOCK_SOUND_GROUP = register("block_sound_group", builder -> builder);

    public static final DataComponentType<Boolean> REQUIRES_CORRECT_TOOL_FOR_DROPS = register("requires_correct_tool_for_drops",
        builder -> builder.textParser(Boolean.class));
    public static final DataComponentType<Float> EXPLOSION_RESISTANCE = register("explosion_resistance",
        builder -> builder.textParser(Float.class));
    public static final DataComponentType<Boolean> PISTON_IMMOVABLE = register("piston_immovable",
        builder -> builder.textParser(Boolean.class));
    public static final DataComponentType<EntitySpawnerComponent> ENTITY_SPAWNER = register("entity_spawner",
        builder -> builder);

    public static final DataComponentType<CruxBlockGroup> BLOCK_GROUP = register("block_group", builder ->
        builder.persistent(PersistentDataSerializer.create(Crux.key(CruxBlocksPersistTags.CRUX_BLOCK_GROUP.tagName()),
            CruxBlocksPersistence.CRUX_BLOCK_GROUP))
            .textParserUnchecked(new ComponentTextInputParser<CruxBlockGroup>() {
                @Override
                public @NotNull CruxBlockGroup parse(@NotNull Object object) throws IllegalArgumentException {
                    return Objects.requireNonNull(CruxBlocksRegistries.BLOCK.getGroup(Crux.key(object.toString())),
                        "BlockGroup of " + object + " not found!");
                }
            })
    );

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
