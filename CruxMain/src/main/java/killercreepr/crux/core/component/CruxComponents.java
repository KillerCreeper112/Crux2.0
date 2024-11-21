package killercreepr.crux.core.component;

import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.block.tag.BlockTag;
import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.serialization.PersistentDataSerializer;
import killercreepr.crux.api.item.component.ToolComponent;
import killercreepr.crux.core.component.parser.type.ComponentParserTypes;
import killercreepr.crux.core.persistence.CruxPersist;
import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.core.util.CruxMath;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class CruxComponents {
    public static void register(){}

    public static final DataComponentType<Float> HARDNESS = register("hardness", builder -> builder
        .textParser(Float.class));
    public static final DataComponentType<Boolean> UNBREAKABLE = register("unbreakable", builder -> builder
        .textParser(Boolean.class));
    public static final DataComponentType<ToolComponent> TOOL = register("tool",
        builder -> builder.persistent(PersistentDataSerializer.create(Crux.key(CruxPersist.TOOL.tagName()), CruxPersistence.TOOL_COMPONENT))
            .textParser(new ComponentTextInputParser<>() {
                @Override
                public @NotNull ToolComponent parse(@NotNull Object object) throws IllegalArgumentException {
                    Map<?, ?> map = (Map<?, ?>) object;
                    Object defaultMiningSpeedObject = map.get("default_mining_speed");
                    float defaultMiningSpeed = (float) CruxMath.evaluate(defaultMiningSpeedObject == null ? "1" : defaultMiningSpeedObject.toString());
                    List<ToolComponent.Rule> rules = new ArrayList<>();
                    if(map.get("rules") instanceof List<?> list){
                        for(Object o : list){
                            Map<?, ?> oo = (Map<?, ?>) o;
                            BlockPredicate predicate = oo.containsKey("blocks") ? parsePredicate(oo.get("blocks")) : null;
                            Float speed = oo.containsKey("speed") ? (float) CruxMath.evaluate(oo.get("speed").toString()) : null;
                            Boolean isCorrect = ComponentParserTypes.BOOLEAN.attemptParse(oo.get("correct_for_drops"));
                            ToolComponent.Rule rule = new ToolComponent.Simple.Rule(predicate, speed, isCorrect);
                            rules.add(rule);
                        }
                    }
                    return new ToolComponent.Simple(
                        defaultMiningSpeed,
                        rules.isEmpty() ? null : rules
                    );
                }
            }));

    public static BlockPredicate parsePredicate(@NotNull String blockTag){
        if(blockTag.startsWith("#")){
            BlockTag tag = CruxRegistries.BLOCK_TAG.get(Crux.key(blockTag.substring(1)));
            if(tag==null) return null;
            return BlockPredicate.fromTag(tag);
        }
        return BlockPredicate.fromType(Crux.key(blockTag));
    }

    public static BlockPredicate parsePredicate(@NotNull Object text){
        if(text instanceof List<?> list){
            Collection<BlockPredicate> predicates = new ArrayList<>();
            for(Object s : list){
                BlockPredicate predicate = parsePredicate(s);
                predicates.add(predicate);
            }
            return BlockPredicate.fromAllOf(predicates);
        }
        return parsePredicate(text.toString());
    }

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
