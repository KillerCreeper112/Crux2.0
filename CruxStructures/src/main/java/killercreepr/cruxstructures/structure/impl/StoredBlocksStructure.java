package killercreepr.cruxstructures.structure.impl;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BlockState;
import killercreepr.crux.data.BlockPos;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

public class StoredBlocksStructure extends FAWEStructure{
    protected final @NotNull Collection<BlockPos> blocks;
    public StoredBlocksStructure(@NotNull Key key, @NotNull ClipboardHolder holder) {
        super(key, holder);
        blocks = calculateBlocks();
    }

    public StoredBlocksStructure(@NotNull Key key, @NotNull String filename) {
        super(key, filename);
        blocks = calculateBlocks();
    }

    public StoredBlocksStructure(@NotNull Key key, @NotNull File schematicFile) {
        super(key, schematicFile);
        blocks = calculateBlocks();
    }

    public @NotNull Collection<BlockPos> calculateBlocks(){
        Collection<BlockPos> list = new HashSet<>();
        Clipboard clipboard = holder.getClipboards().getFirst();
        clipboard.forEach(block ->{
            BlockState state = clipboard.getBlock(block);
            if(state.isAir()) return;
            list.add(new BlockPos(block.x(), block.y(), block.z()));
        });
        return list;
    }
}
