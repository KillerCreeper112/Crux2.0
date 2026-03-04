package killercreepr.crux.api.item.predicate;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.DecodeException;
import killercreepr.crux.api.codec.PolymorphicCodec;
import killercreepr.crux.api.codec.builder.PolymorphicCodecBuilder;
import killercreepr.crux.api.codec.node.DataArray;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.api.data.tag.Tag;
import killercreepr.crux.api.item.tag.ItemTag;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.codec.node.ObjectDataNode;
import killercreepr.crux.core.item.predicate.*;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface ItemPredicate extends Predicate<ItemStack> {
    Codec<ItemPredicate> SIMPLE_CODEC = new Codec<>() {
        @Override
        public ItemPredicate decode(DataNode node) throws DecodeException {
            if(node.isString()){
                String key = node.asString();
                if(key.startsWith("#")){
                    ItemTag tag = ItemTag.CODEC.decode(node);
                    if(tag==null) return null;
                    return ItemPredicate.fromTag(tag);
                }
                return ItemPredicate.fromType(Crux.key(key));
            }
            if(node instanceof DataArray a){
                Collection<ItemPredicate> children = LIST_CODEC.decode(a);
                if(children == null) return null;
                return ItemPredicate.fromAllOf(children);
            }
            return null;
        }

        @Override
        public DataNode encode(ItemPredicate value) {
            return null;
        }
    };

    PolymorphicCodec<ItemPredicate> CODEC = PolymorphicCodecBuilder.polymorphicBuilder(ItemPredicate.class, "type")
      .defaultCodec(SIMPLE_CODEC)
      .register("all_of", new Codec<>() {
          @Override
          public ItemPredicate decode(DataNode node) throws DecodeException {
              if(!(node instanceof ObjectDataNode o)) return null;
              var values = o.get("values");
              if(values == null) return null;
              Collection<ItemPredicate> valueList = LIST_CODEC.decode(values);
              if(valueList==null) return null;

              return ItemPredicate.fromAllOf(valueList);
          }

          @Override
          public DataNode encode(ItemPredicate value) {
              return null;
          }
      })
      .register("any_of", new Codec<>() {
          @Override
          public ItemPredicate decode(DataNode node) throws DecodeException {
              if(!(node instanceof ObjectDataNode o)) return null;
              var values = o.get("values");
              if(values == null) return null;
              Collection<ItemPredicate> valueList = LIST_CODEC.decode(values);
              if(valueList==null) return null;

              return ItemPredicate.fromAnyOf(valueList);
          }

          @Override
          public DataNode encode(ItemPredicate value) {
              return null;
          }
      })
      .build();

    Codec<List<ItemPredicate>> LIST_CODEC = Codec.listCodec(CODEC);

    static ItemPredicate fromType(@NotNull Key type){
        return new ItemTypePredicate(type);
    }
    static ItemPredicate fromTag(@NotNull Tag<ItemStack> tag){
        return new ItemTagPredicate(tag);
    }
    static ItemPredicate fromAllOf(@NotNull Collection<ItemPredicate> children){
        return new ItemAllPredicate(children);
    }
    static ItemPredicate fromAllOf(@NotNull ItemPredicate... children){
        return new ItemAllPredicate(Arrays.asList(children));
    }

    static ItemPredicate fromAnyOf(@NotNull Collection<ItemPredicate> children){
        return new ItemAnyPredicate(children);
    }
    static ItemPredicate fromAnyOf(@NotNull ItemPredicate... children){
        return new ItemAnyPredicate(Arrays.asList(children));
    }

    static ItemPredicate fromInverted(@NotNull ItemPredicate predicate){
        return new ItemInvertPredicate(predicate);
    }

    boolean test(@NotNull ItemStack item);
}
