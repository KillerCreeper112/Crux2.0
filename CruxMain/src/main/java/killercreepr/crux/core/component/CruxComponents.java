package killercreepr.crux.core.component;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.parser.persistent.PersistentTextParser;
import killercreepr.crux.api.item.component.ToolComponent;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;

import java.util.function.UnaryOperator;

public class CruxComponents {
    public static void register(){}

    public static final DataComponentType<Float> HARDNESS = register("hardness", builder -> builder
        .inputParser(PersistentTextParser.createFloat("hardness")));

    public static final DataComponentType<Boolean> UNBREAKABLE = register("unbreakable", builder -> builder
        .inputParser(PersistentTextParser.createBool("unbreakable")));

    public static final DataComponentType<ToolComponent> TOOL = register("tool",
        builder -> builder.inputParser(ToolComponent.INPUT_PARSER));

    /*public static final DataComponentType<ToolComponent> TOOL = register("tool",
        builder -> builder.persistent(PersistentDataSerializer.create(Crux.key(CruxPersist.TOOL.tagName()), CruxPersistence.TOOL_COMPONENT))
            .textParser(new ComponentTextInputParser<>() {
                @Override
                public @NotNull ToolComponent decodeObject(@NotNull Object object) throws IllegalArgumentException {
                    Map<?, ?> map = (Map<?, ?>) object;
                    Object defaultMiningSpeedObject = map.get("default_mining_speed");
                    float defaultMiningSpeed = (float) CruxMath.evaluate(defaultMiningSpeedObject == null ? "1" : defaultMiningSpeedObject.toString());
                    List<ToolComponent.Rule> rules = new ArrayList<>();
                    if(map.get("rules") instanceof List<?> list){
                        for(Object o : list){
                            Map<?, ?> oo = (Map<?, ?>) o;
                            BlockPredicate predicate = oo.containsKey("blocks") ? parsePredicate(oo.get("blocks")) : null;
                            Float speed = oo.containsKey("speed") ? (float) CruxMath.evaluate(oo.get("speed").toString()) : null;
                            Boolean isCorrect = ComponentParserTypes.BOOLEAN.attemptDecodeObject(oo.get("correct_for_drops"));
                            ToolComponent.Rule rule = new ToolComponent.Simple.Rule(predicate, speed, isCorrect);
                            rules.add(rule);
                        }
                    }
                    return new ToolComponent.Simple(
                        defaultMiningSpeed,
                        rules.isEmpty() ? null : rules
                    );
                }
            }));*/

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
