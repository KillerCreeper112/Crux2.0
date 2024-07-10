package killercreepr.cruxstructures.structure.active;

import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public interface ActiveStructure {
    @NotNull
    Structure parent();
    @NotNull
    Block center();

    @NotNull Block getMinPoint();
    @NotNull Block getMaxPoint();
}
