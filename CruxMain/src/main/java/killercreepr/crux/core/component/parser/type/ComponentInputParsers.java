package killercreepr.crux.core.component.parser.type;

import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.block.predicate.BlockPredicateComponent;
import killercreepr.crux.api.block.tag.BlockTag;
import killercreepr.crux.api.component.parser.persistent.ComponentInputField;
import killercreepr.crux.api.component.parser.persistent.PersistentTextParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.block.predicate.BlockAllPredicate;
import killercreepr.crux.core.registries.CruxRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class ComponentInputParsers {
    //blocks:#string
    //blocks:[#list,dirt]
    //blocks:{type:any_of,terms:[]}

    public static PersistentTextParser<BlockPredicate> blockPredicate(@NotNull Key key){
        return PersistentTextParser.alternativeBuilder(BlockPredicate.class)
            .add(PersistentTextParser.singleBuilder(BlockPredicate.class)
                .field(key, ComponentInputField.createList(ComponentInputField.createString(e -> e), e ->{
                    if(!(e instanceof BlockPredicateComponent cc)) throw new IllegalArgumentException("BlockPredicate is not component type!");
                    return cc.encodeToParser();
                }))
                .output(e ->{
                    Collection<BlockPredicate> predicates = new ArrayList<>();
                    e.toList().forEach(s ->{
                        BlockPredicate predicate = SIMPLE_BLOCK_PREDICATE.decodeObject(s);
                        predicates.add(predicate);
                    });
                    return new BlockAllPredicate(predicates);
                })
                .build())
            .build()
            ;
    }

    public static final PersistentTextParser<BlockPredicate> SIMPLE_BLOCK_PREDICATE = PersistentTextParser.singleBuilder(BlockPredicate.class)
        .field("d", ComponentInputField.createString(e ->{
            for(String s : ((BlockPredicateComponent) e).encodeToParser()){
                return s;
            }
            throw new IllegalStateException("NO shouldn't happen");
        }))
        .apply(e ->{
            String string = e.decode(Object::toString);
            if(string.startsWith("#")){
                BlockTag tag = CruxRegistries.BLOCK_TAG.get(Crux.key(string.substring(1)));
                if(tag != null) return BlockPredicate.fromTag(tag);
                return null;
            }
            return BlockPredicate.fromType(Crux.key(string));
        });/*PersistentTextParser.alternativeBuilder(BlockPredicate.class)
        .add(PersistentTextParser.mapBuilder(BlockPredicate.class)
            .field("type", ComponentInputField.createString(e ->{
                if(e instanceof BlockAllPredicate) return "all_of";
                if(e instanceof BlockAnyPredicate) return "any_of";
                return "unknown";
            }))
            .field("terms", ComponentInputField.createList(ComponentInputField.createKey(e -> e), e -> e))
            .apply(e ->{
                String type = e.decode("type");
                Collection<BlockPredicate> predicates = e.decode("terms");
                switch (type.toLowerCase()){
                    case "any_of" ->{
                        return BlockPredicate.fromAnyOf()
                    }
                }
            }))
        .build();*/
}
