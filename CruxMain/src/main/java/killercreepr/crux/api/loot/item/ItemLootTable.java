package killercreepr.crux.api.loot.item;

import com.google.common.collect.Lists;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.util.CruxCollection;
import killercreepr.crux.core.util.CruxMath;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface ItemLootTable extends LootTable<ItemStack> {
    static void fillInventory(List<ItemLootTable> lootTables, @NotNull Inventory inventory, @NotNull LootContext context){

    }

    default void fillInventory(@NotNull Inventory inventory, @NotNull LootContext context){
        fillInventory(inventory, context, CruxMath.random());
    }

    default void fillInventory(@NotNull Inventory inventory, @NotNull LootContext context, Random random) {
        List<ItemStack> randomItems = populateLoot(context);
        List<Integer> availableSlots = this.getAvailableSlots(inventory, random);

        this.shuffleAndSplitItems(randomItems, availableSlots.size(), random);
        for (ItemStack itemStack : randomItems) {
            if (availableSlots.isEmpty()) {
                Crux.logWarning("Tried to over-fill a container");
                return;
            }
            inventory.setItem(availableSlots.remove(availableSlots.size() - 1), itemStack);
        }
    }

    private List<Integer> getAvailableSlots(Inventory inventory, Random random) {
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < inventory.getSize(); ++i) {
            if (CruxItem.isEmpty(inventory.getItem(i))) {
                list.add(i);
            }
        }

        CruxCollection.shuffle(list, random);
        return list;
    }

    default void shuffleAndSplitItems(List<ItemStack> stacks, int emptySlotsCount, Random random){
        defaultShuffleAndSplitItems(stacks, emptySlotsCount, random);
    }

    static void defaultShuffleAndSplitItems(Collection<ItemStack> stacks, int emptySlotsCount, Random random) {
        List<ItemStack> list = Lists.newArrayList();
        Iterator<ItemStack> iterator = stacks.iterator();

        while(iterator.hasNext()) {
            var itemStack = iterator.next();
            if (itemStack.isEmpty()) {
                iterator.remove();
            } else if (itemStack.getAmount() > 1) {
                list.add(itemStack);
                iterator.remove();
            }
        }

        while(emptySlotsCount - stacks.size() - list.size() > 0 && !list.isEmpty()) {
            ItemStack itemStack1 = list.remove(CruxMath.random(0, list.size() - 1, random));
            int randomInt = CruxMath.random(1, itemStack1.getAmount() / 2);
            ItemStack itemStack2 = itemStack1.clone();
            itemStack2.setAmount(randomInt);
            itemStack1.setAmount(itemStack1.getAmount()-randomInt);

            if (itemStack1.getAmount() > 1 && random.nextBoolean()) {
                list.add(itemStack1);
            } else {
                stacks.add(itemStack1);
            }

            if (itemStack2.getAmount() > 1 && random.nextBoolean()) {
                list.add(itemStack2);
            } else {
                stacks.add(itemStack2);
            }
        }

        stacks.addAll(list);
        if(stacks instanceof List<ItemStack> listCast){
            CruxCollection.shuffle(listCast, random);
        }
    }

    /*default void fillInventory(@NotNull Inventory inventory, @NotNull LootContext context) {
        Random random = context.getRandom();
        for(ItemStack i : populateLoot(context)){
            int index = random.nextInt(0, inventory.getSize());
            ItemStack empty = inventory.getItem(index);
            while(!CruxItem.isEmpty(empty)){
                index++;
                if(index >= inventory.getSize()) return;
                empty = inventory.getItem(index);
            }
            inventory.setItem(index, i);
        }
    }*/
}
