package killercreepr.crux.api.block.predicate;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.core.block.predicate.*;
import killercreepr.crux.api.data.tag.Tag;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

public interface BlockPredicate extends Predicate<CruxedBlock> {
    static BlockPredicate fromType(@NotNull Key type){
        return new BlockTypePredicate(type);
    }
    static BlockPredicate fromTag(@NotNull Tag<CruxedBlock> tag){
        return new BlockTagPredicate(tag);
    }
    static BlockPredicate fromAllOf(@NotNull Collection<BlockPredicate> children){
        return new BlockAllPredicate(children);
    }
    static BlockPredicate fromAnyOf(@NotNull Collection<BlockPredicate> children){
        return new BlockAnyPredicate(children);
    }
    static BlockPredicate fromInverted(@NotNull BlockPredicate children){
        return new BlockInvertPredicate(children);
    }
    static BlockPredicate fromAllOf(@NotNull BlockPredicate... children){
        return new BlockAllPredicate(Arrays.asList(children));
    }
    static BlockPredicate fromAnyOf(@NotNull BlockPredicate... children){
        return new BlockAnyPredicate(Arrays.asList(children));
    }
    boolean test(@NotNull CruxedBlock block);
}
