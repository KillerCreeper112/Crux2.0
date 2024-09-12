package killercreepr.crux.block.predicate;

import killercreepr.crux.data.tag.Tag;
import killercreepr.crux.block.CruxedBlock;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

public interface BlockPredicate {
    static BlockPredicate fromType(@NotNull Key type){
        return new BlockTypePredicate(type);
    }
    static BlockPredicate fromTag(@NotNull Tag<CruxedBlock> tag){
        return new BlockTagPredicate(tag);
    }
    static BlockPredicate fromAllOf(@NotNull Collection<BlockPredicate> children){
        return new BlockAllPredicate(children);
    }
    static BlockPredicate fromAllOf(@NotNull BlockPredicate... children){
        return new BlockAllPredicate(Arrays.asList(children));
    }
    boolean test(@NotNull CruxedBlock block);
}
