package killercreepr.cruxentities.component;

import killercreepr.crux.api.loot.item.ItemLootTable;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.util.CruxMath;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Random;

public record LaunchDrops(NumberProvider force, NumberProvider yForce) {

    public void launchDrops(Location center, Collection<ItemStack> items){
        World world = center.getWorld();
        Random random = CruxMath.random();

        ItemLootTable.defaultShuffleAndSplitItems(items, CruxMath.random(8, 16), random);

        int count = items.size();
        double angleStep = (2 * Math.PI) / count;

        int i = 0;
        for (ItemStack item : items) {
            // spawn the item
            Item dropped = world.dropItem(center, item);

            // calculate direction in a circle
            double angle = i * angleStep;
            double x = Math.cos(angle);
            double z = Math.sin(angle);

            Vector velocity = new Vector(x, yForce.sample(random).floatValue(), z).normalize().multiply(
                force.sample(random).floatValue()
            );

            dropped.setVelocity(velocity);
            i++;
        }
    }
}
