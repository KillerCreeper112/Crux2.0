package killercreepr.cruxblocks.core.block.active.standard;

import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.core.block.active.SimpleActiveCruxBlock;
import killercreepr.cruxblocks.core.block.component.standard.PlaceableCheckComponent;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class ActivePlaceableCheckBlock extends SimpleActiveCruxBlock {
    protected final @NotNull PlaceableCheckComponent component;

    public ActivePlaceableCheckBlock(@NotNull Block block, @NotNull CruxBlock cruxBlock, @NotNull PlaceableCheckComponent component) {
        super(block, cruxBlock);
        this.component = component;
    }

    @Override
    public boolean isPlacementValid() {
        return component.isValid(block);
    }
}
