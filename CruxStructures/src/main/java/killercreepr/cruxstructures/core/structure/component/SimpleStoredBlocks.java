package killercreepr.cruxstructures.core.structure.component;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockState;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.cruxstructures.api.component.StoredBlocks;
import killercreepr.cruxstructures.core.structure.CfgFAWEStructure;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

public class SimpleStoredBlocks extends SimpleBlockManipulatorComponent implements StoredBlocks {
    protected final @NotNull CfgFAWEStructure structure;
    protected final @NotNull Collection<CruxPosition> blocks;

    public SimpleStoredBlocks(boolean disableBlockBreak, boolean disableBlockPlace,@NotNull CfgFAWEStructure structure) {
        super(disableBlockBreak, disableBlockPlace);
        this.structure = structure;
        this.blocks = calculateBlocks();
    }

    public void onForEachBlock(Collection<CruxPosition> list, BlockVector3 block, BlockState state){
        if(state.isAir()) return;
        list.add(CruxPosition.block(block.x(), block.y(), block.z()));
    }

    public @NotNull Collection<CruxPosition> calculateBlocks(){
        Collection<CruxPosition> list = new HashSet<>();
        Clipboard clipboard = structure.getHolder().getClipboards().getFirst();
        clipboard.forEach(block ->{
            BlockState state = clipboard.getBlock(block);
            onForEachBlock(list, block, state);
        });
        return list;
    }

    @Override
    public @NotNull Collection<CruxPosition> getStoredBlocks() {
        return blocks;
    }
}
