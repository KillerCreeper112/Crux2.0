package killercreepr.cruxenchants.api.enchant;

import killercreepr.cruxenchants.core.enchant.SimpleApplicableItemGroup;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

public interface ApplicableItemGroup {
    static Builder builder(){
        return new SimpleApplicableItemGroup.Builder();
    }

    static ApplicableItemGroup itemGroup(ApplicableItemType... types){
        return new SimpleApplicableItemGroup(Arrays.asList(types));
    }

    ApplicableItemGroup ARMOR = builder().add(
        ApplicableItemType.HELMET, ApplicableItemType.CHESTPLATE, ApplicableItemType.LEGGINGS, ApplicableItemType.BOOTS
    ).build();

    ApplicableItemGroup TOOLS = builder().add(
        ApplicableItemType.PICKAXE, ApplicableItemType.AXE, ApplicableItemType.SHOVEL, ApplicableItemType.HOE
    ).build();

    ApplicableItemGroup WEAPONS = builder().add(
        ApplicableItemType.AXE, ApplicableItemType.SWORD
    ).build();

    ApplicableItemGroup SHARP_WEAPONS = builder().add(
        ApplicableItemType.SWORD
    ).build();

    @NotNull Collection<ApplicableItemType> types();

    @NotNull String formatSymbols();

    interface Builder{
        Builder add(ApplicableItemType... types);
        Builder addTypes(Collection<ApplicableItemType> types);
        Builder addGroups(Collection<ApplicableItemGroup> types);
        Builder add(ApplicableItemGroup... groups);
        ApplicableItemGroup build();
    }
}
