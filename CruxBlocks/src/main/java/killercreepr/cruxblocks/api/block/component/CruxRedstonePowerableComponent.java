package killercreepr.cruxblocks.api.block.component;

import org.bukkit.block.Block;

public interface CruxRedstonePowerableComponent extends CruxBlockComponent {
    void powerChanged(Block from, int newPower);
}
