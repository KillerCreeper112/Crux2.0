package killercreepr.cruxpotions.core.component;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxpotions.api.potion.StoredPotion;
import killercreepr.cruxpotions.core.component.parser.PotionCompParsers;

import java.util.List;
import java.util.function.UnaryOperator;

public class PotionComponents {
    public static void register(){}
    public static final DataComponentType<StoredPotion> STORED_CRUX_POTION = register("stored_crux_potion", builder -> builder
        .persistTextParser(PotionCompParsers.STORED_CRUX_POTION.createInput(Crux.key("stored_crux_potion"))));

    public static final DataComponentType<List<StoredPotion>> STORED_CRUX_POTIONS = register("stored_crux_potions", builder -> builder
        .persistTextParser(PotionCompParsers.LIST_STORED_CRUX_POTION.createInput(Crux.key("stored_crux_potions"))));

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
