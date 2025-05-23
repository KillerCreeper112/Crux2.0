package killercreepr.cruxattributes.core.text.tags;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringListResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeInstance;
import killercreepr.cruxattributes.api.attribute.CruxAttributeModifier;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxattributes.api.values.ValuesProvider;
import killercreepr.cruxattributes.core.component.CruxAttributeComponents;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemStackTags implements ObjectTag<ItemStack> {
    protected final Holder<ValuesProvider> cfg;

    public ItemStackTags(Holder<ValuesProvider> cfg) {
        this.cfg = cfg;
    }

    @Override
    public @NotNull Class<ItemStack> getObjectType() {
        return ItemStack.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("cruxattributes_");
    }

    @Override
    public @Nullable TagContainer<StringListResolver> requestStringLists(@NotNull ItemStack object, @NotNull TagParser tagParser) {
        return TagContainer.stringList(tagParser)
            .add(Tag.stringList("attributes", (args,ctx) ->{
                var comp = CruxItem.wrap(object).getOrDefaultData(CruxAttributeComponents.CRUX_ATTRIBUTES);
                if(comp == null) return List.of();
                Collection<CruxAttributeInstance> instances = comp.getAttributeInstances();
                if(instances.isEmpty()) return List.of();

                List<String> attributeFormat = this.cfg.value().CRUX_ATTRIBUTES_ITEM_FORMAT().value();
                List<String> modFormat =  this.cfg.value().CRUX_ATTRIBUTES_ITEM_MODIFIER_FORMAT().value();
                List<String> modMultiplyFormat =  this.cfg.value().CRUX_ATTRIBUTES_ITEM_MODIFIER_MULTIPLY_FORMAT().value();
                if(attributeFormat == null || modMultiplyFormat == null) return null;

                List<String> list = new ArrayList<>();
                Map<CruxSlotGroup, Map<CruxAttribute, List<CruxAttributeModifier>>> modifiers = new TreeMap<>();
                for(CruxAttributeInstance instance : instances){
                    for (CruxAttributeModifier modifier : instance.getModifiers()) {
                        modifiers.computeIfAbsent(modifier.getSlotGroup(), e -> new TreeMap<>())
                            .computeIfAbsent(instance.getAttribute(), e -> new ArrayList<>())
                            //.computeIfAbsent(instance.getAttribute(), e -> new ArrayList<>())
                            .add(modifier);
                    }

                }
                modifiers.forEach((slot, slotMap) ->{
                    MergedTagContainer tags = TagContainer.merged(tagParser)
                        .add(Tag.parsed("slot_group_when_in_slot", slot.getWhenInSlot()));
                    list.addAll(ctx.deserializeStringList(attributeFormat, tags));

                    slotMap.forEach((attribute, map) ->{
                        map.sort(Comparator.comparingDouble((CruxAttributeModifier m) ->
                            attribute.isNegative(m.getAmount()) ? -Math.abs(m.getAmount()) : m.getAmount()
                        ).reversed());

                        for (CruxAttributeModifier m : map) {
                            MergedTagContainer modTags = TagContainer.merged(tagParser)
                                .addAll(tags)
                                .hook(m)
                                .hook(attribute)
                                ;
                            List<String> f = m.getOperation() == CruxAttribute.Operation.MULTIPLY ? modMultiplyFormat : modFormat;
                            list.addAll(ctx.deserializeStringList(f, modTags));
                        }
                    });
                });
                return list;
            }));
    }
}
