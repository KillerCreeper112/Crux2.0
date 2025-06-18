package killercreepr.cruxblocks.core.block.component;

import killercreepr.crux.api.block.sound.CreateBlockSoundGroup;
import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxblocks.api.block.component.*;
import killercreepr.cruxblocks.api.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.api.structure.component.StructureCruxBlockPlaceInsideComponent;
import killercreepr.cruxblocks.core.block.component.standard.EntitySpawnerComponent;
import killercreepr.cruxblocks.core.block.component.standard.InteractHarvestableBlockComponent;
import killercreepr.cruxblocks.core.block.component.standard.PlaceableCheckComponent;
import killercreepr.cruxblocks.core.component.PlacedCustomBlocksComponent;
import killercreepr.cruxblocks.core.registries.CruxBlocksRegistries;
import killercreepr.cruxblocks.core.structure.modules.PlaceCustomBlocksModule;
import net.kyori.adventure.key.Keyed;

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
    public static final DataComponentType<VineBlock> VINE_BLOCK = register("vine_block", builder ->
        builder);
    public static final DataComponentType<VineGroup> VINE_GROUP = register("vine_group", builder ->
        builder);
    public static final DataComponentType<PlaceableCheckComponent> PLACEABLE_CHECK = register("placeable_check", builder ->
        builder);

    public static final DataComponentType<CreateBlockSoundGroup> BLOCK_SOUND_GROUP = register("block_sound_group", builder -> builder);

    public static final DataComponentType<Boolean> REQUIRES_CORRECT_TOOL_FOR_DROPS = register("requires_correct_tool_for_drops",
        builder -> builder.textParser(PersistTextParser.BOOLEAN));
    public static final DataComponentType<Float> EXPLOSION_RESISTANCE = register("explosion_resistance",
        builder -> builder.textParser(PersistTextParser.FLOAT));
    public static final DataComponentType<Boolean> PISTON_IMMOVABLE = register("piston_immovable",
        builder -> builder.textParser(PersistTextParser.BOOLEAN));

    public static final DataComponentType<EntitySpawnerComponent> ENTITY_SPAWNER = register("entity_spawner",
        builder -> builder);

    public static final DataComponentType<CruxBlockGroup> BLOCK_GROUP = register("block_group", builder -> builder
            .persistTextParser(
                PersistTextParser.elementBuilder(CruxBlockGroup.class)
                .field(TextInputField.field(PersistTextParser.KEY, Keyed::key))
                .apply(ctx -> Objects.requireNonNull(CruxBlocksRegistries.BLOCK.getGroup(ctx.get()),
                    "BlockGroup of " + ctx.get() + " not found!"))
                .createInput(Crux.key("block_group"))
            )
    );
    public static final DataComponentType<PlaceCustomBlocksModule> PLACE_CUSTOM_BLOCKS = register("structure/place_custom_blocks",
        builder -> builder);

    public static final DataComponentType<PlacedCustomBlocksComponent> PLACED_CUSTOM_BLOCKS = register("structure/placed_custom_blocks",
        builder -> builder);
    public static final DataComponentType<StructureCruxBlockPlaceInsideComponent> CRUX_BLOCK_PLACE_INSIDE = register("structure/crux_block_place_inside",
        builder -> builder);

    public static final DataComponentType<InteractHarvestableBlockComponent> INTERACT_HARVESTABLE = register("interact_harvestable",
        builder -> builder);

    public static final DataComponentType<CruxRedstonePowerableComponent> GENERIC_REDSTONE_POWERABLE = register("generic_redstone_powerable",
        builder -> builder);

    public static final DataComponentType<CruxInteractablePhysicalBlockComponent> GENERIC_ENTITY_PHYSICAL_INTERACT = register("generic_entity_physical_interact",
        builder -> builder);

    public static final DataComponentType<CruxEntityMoveInsideBlockComponent> GENERIC_ENTITY_MOVE_INSIDE = register("generic_entity_move_inside",
        builder -> builder);

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
