package killercreepr.cruxitems.api.item.consume;

import killercreepr.crux.api.item.CruxItem;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemConsumeContext {
    @NotNull
    Player getPlayer();
    @NotNull
    CruxItem getItem();
    @Nullable ItemStack getReplacement();
}
