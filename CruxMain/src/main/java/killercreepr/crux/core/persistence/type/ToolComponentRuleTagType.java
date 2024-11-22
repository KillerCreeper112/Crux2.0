package killercreepr.crux.core.persistence.type;

import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.block.tag.BlockTag;
import killercreepr.crux.api.item.component.ToolComponent;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.block.predicate.BlockAllPredicate;
import killercreepr.crux.core.block.predicate.BlockTagPredicate;
import killercreepr.crux.core.block.predicate.BlockTypePredicate;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.core.util.CruxTag;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ToolComponentRuleTagType implements PersistentDataType<PersistentDataContainer, ToolComponent.Rule> {
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<ToolComponent.Rule> getComplexType() {
        return ToolComponent.Rule.class;
    }

    public List<String> predicateToString(BlockPredicate predicate){
        if(predicate instanceof BlockAllPredicate all){
            List<String> list = new ArrayList<>(all.getChildren().size());
            all.getChildren().forEach(pr -> list.addAll(predicateToString(pr)));
            return list;
        }
        if(predicate instanceof BlockTypePredicate t){
            return List.of(t.getType().asString());
        }
        if(predicate instanceof BlockTagPredicate t){
            return List.of("#" + t.getTag().key().asString());
        }
        throw new UnsupportedOperationException("BlockPredicate not supported.");
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull ToolComponent.Rule complex, @NotNull PersistentDataAdapterContext context) {
        if(!(complex instanceof ToolComponent.Simple.Rule s)) throw new IllegalArgumentException(complex.getClass().getSimpleName() + " not supported!");
        PersistentDataContainer c = context.newPersistentDataContainer();
        BlockPredicate predicate = s.getPredicate();
        if(predicate != null){
            List<String> list = predicateToString(predicate);
            if(list.size() > 1){
                CruxTag.set(c, "blocks", PersistentDataType.LIST.strings(), list);
            }else if(!list.isEmpty()) CruxTag.set(c, "blocks", STRING, list.getFirst());
        }
        CruxTag.set(c, "speed", PersistentDataType.FLOAT, complex.getSpeed());
        CruxTag.set(c, "correct_for_drops", PersistentDataType.BOOLEAN, s.getCorrectToolForDrops());
        return c;
    }

    public BlockPredicate parsePredicate(@NotNull String blockTag){
        if(blockTag.startsWith("#")){
            BlockTag tag = CruxRegistries.BLOCK_TAG.get(Crux.key(blockTag.substring(1)));
            if(tag==null) return null;
            return BlockPredicate.fromTag(tag);
        }
        return BlockPredicate.fromType(Crux.key(blockTag));
    }

    public BlockPredicate parsePredicate(@NotNull PersistentDataContainer c){
        String blockTag = CruxTag.get(c, "blocks", PersistentDataType.STRING, null);
        if(blockTag != null){
            return parsePredicate(blockTag);
        }
        List<String> list = CruxTag.get(c, "blocks", PersistentDataType.LIST.strings(), null);
        if(list == null || list.isEmpty()) return null;
        Collection<BlockPredicate> predicates = new ArrayList<>();
        for(String s : list){
            BlockPredicate predicate = parsePredicate(s);
            predicates.add(predicate);
        }
        return BlockPredicate.fromAllOf(predicates);
    }

    @Override
    public @NotNull ToolComponent.Rule fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext ctx) {
        BlockPredicate predicate = parsePredicate(c);
        Float speed = CruxTag.get(c, "speed", PersistentDataType.FLOAT, null);
        Boolean correctTool = CruxTag.get(c, "correct_for_drops", PersistentDataType.BOOLEAN, null);
        return new ToolComponent.Simple.Rule(
            predicate, speed, correctTool
        );
    }
}
