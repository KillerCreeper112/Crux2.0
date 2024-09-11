package killercreepr.cruxstructures.structure.impl;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BlockState;
import killercreepr.crux.data.BlockPos;
import killercreepr.cruxstructures.structure.module.StructureModule;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class CfgStoredBlocksStructure extends CfgFAWEStructure{
    protected final @NotNull Collection<BlockPos> blocks;

    public CfgStoredBlocksStructure(@NotNull Key key, @NotNull ClipboardHolder holder, boolean persistent, @NotNull List<StructureModule> modules) {
        super(key, holder, persistent, modules);
        this.blocks = calculateBlocks();
    }

    public CfgStoredBlocksStructure(@NotNull Key key, @NotNull String filename, boolean persistent, @NotNull List<StructureModule> modules) {
        super(key, filename, persistent, modules);
        this.blocks = calculateBlocks();
    }

    public CfgStoredBlocksStructure(@NotNull Key key, @NotNull File schematicFile, boolean persistent, @NotNull List<StructureModule> modules) {
        super(key, schematicFile, persistent, modules);
        this.blocks = calculateBlocks();
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
