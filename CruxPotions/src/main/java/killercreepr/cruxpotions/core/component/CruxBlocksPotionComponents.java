package killercreepr.cruxpotions.core.component;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxblocks.api.block.component.CruxEntityMoveInsideBlockComponent;
import killercreepr.cruxpotions.api.potion.StoredPotion;
import killercreepr.cruxpotions.core.component.parser.PotionCompParsers;

import java.util.List;
import java.util.function.UnaryOperator;

public class CruxBlocksPotionComponents {
    public static void register(){}
    public static final DataComponentType<CruxEntityMoveInsideBlockComponent> GENERIC_CRUX_POTIONS_ENTITY_MOVE_INSIDE = register("generic_crux_potions_entity_move_inside",
        builder -> builder);

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
