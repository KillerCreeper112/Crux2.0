package killercreepr.cruxstructures.structure.impl;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BlockState;
import killercreepr.crux.data.BlockPos;
import net.kyori.adventure.key.Key;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

public class CfgStoredBlocksStructure extends CfgFAWEStructure{
    protected final @NotNull Collection<BlockPos> blocks;

    public CfgStoredBlocksStructure(@NotNull Key key, @NotNull ClipboardHolder holder, boolean persistent) {
        super(key, holder, persistent);
        blocks = calculateBlocks();
    }

    public CfgStoredBlocksStructure(@NotNull Key key, @NotNull String filename, boolean persistent) {
        super(key, filename, persistent);
        blocks = calculateBlocks();
    }

    public CfgStoredBlocksStructure(@NotNull Key key, @NotNull File schematicFile, boolean persistent) {
        super(key, schematicFile, persistent);
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
