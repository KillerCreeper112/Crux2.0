package killercreepr.crux.core.component.parser.type;

import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.block.predicate.BlockPredicateComponent;
import killercreepr.crux.api.block.tag.BlockTag;
import killercreepr.crux.api.component.parser.persistent.ComponentInputField;
import killercreepr.crux.api.component.parser.persistent.PersistentTextParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.block.predicate.BlockAllPredicate;
import killercreepr.crux.core.block.predicate.BlockAnyPredicate;
import killercreepr.crux.core.registries.CruxRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ComponentInputParsers {
    /*public static PersistentTextParser<BlockPredicate> blockPredicate(@NotNull Key key){
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
                .canDecode(e -> e instanceof List<?>)
                .canEncode(e -> e instanceof BlockPredicateComponent)
                .build())
            .add(TYPED_BLOCK_PREDICATE)
            .build()
            ;
    }*/

    public static PersistentTextParser<BlockPredicate> blockPredicate(@NotNull Key key){
        return PersistentTextParser.singleBuilder(BlockPredicate.class)
            .field(ComponentInputField.createList(ComponentInputField.createString(e -> e), e ->{
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
            .canDecode(e -> e instanceof List<?>)
            .canEncode(e -> e instanceof BlockPredicateComponent)
            .build();
    }

    public static final PersistentTextParser<BlockPredicate> SIMPLE_BLOCK_PREDICATE = PersistentTextParser.singleBuilder(BlockPredicate.class)
        .canEncode(e -> e instanceof BlockPredicateComponent)
        .canDecode(e -> e instanceof String)
        .field(ComponentInputField.createString(e ->{
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
        });

    public static final PersistentTextParser<BlockPredicate> TYPED_BLOCK_PREDICATE = PersistentTextParser.mapBuilder(BlockPredicate.class)
        .canEncode(e -> e instanceof BlockPredicateComponent)
        .canDecode(e -> e instanceof Map<?,?>)
        .field("type", ComponentInputField.createString(e ->{
            if(e instanceof BlockAllPredicate) return "all";
            if(e instanceof BlockAnyPredicate) return "any";
            throw new IllegalStateException();
        }))
        .field("terms", ComponentInputField.createList(SIMPLE_BLOCK_PREDICATE,
            e -> ((BlockPredicateComponent)e).encodeToParser()))
        .apply(ctx ->{
            String type = ctx.decode("type");
            Collection<BlockPredicate> list = ctx.decode("terms");

            switch (type.toLowerCase()){
                case "all" ->{
                    return new BlockAllPredicate(list);
                }
                case "any" ->{
                    return new BlockAnyPredicate(list);
                }
            }
            throw new IllegalStateException();
        });
}
