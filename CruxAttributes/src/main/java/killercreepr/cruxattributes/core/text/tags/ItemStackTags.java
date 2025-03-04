package killercreepr.cruxattributes.core.text.tags;

import killercreepr.crux.api.data.Holder;
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
                Collection<CruxAttributeInstance> instances = CruxAttribute.getInstances(object);
                if(instances.isEmpty()) return List.of();

                List<String> attributeFormat = this.cfg.value().CRUX_ATTRIBUTES_ITEM_FORMAT().value();
                List<String> modFormat =  this.cfg.value().CRUX_ATTRIBUTES_ITEM_MODIFIER_FORMAT().value();
                List<String> modMultiplyFormat =  this.cfg.value().CRUX_ATTRIBUTES_ITEM_MODIFIER_MULTIPLY_FORMAT().value();
                if(attributeFormat == null || modMultiplyFormat == null) return null;

                List<String> list = new ArrayList<>();
                for(CruxAttributeInstance instance : instances){
                    Map<CruxSlotGroup, Map<CruxAttribute, Collection<CruxAttributeModifier>>> modifiers = new HashMap<>();
                    for (CruxAttributeModifier modifier : instance.getModifiers()) {
                        modifiers.computeIfAbsent(modifier.getSlotGroup(), e -> new HashMap<>())
                            .computeIfAbsent(instance.getAttribute(), e -> new ArrayList<>())
                            .add(modifier);
                    }

                    modifiers.forEach((slot, map) ->{
                        MergedTagContainer tags = TagContainer.merged(tagParser)
                            .add(Tag.parsed("slot_group_when_in_slot", slot.getWhenInSlot()))
                            .hook(instance);
                        list.addAll(ctx.deserializeStringList(attributeFormat, tags));

                        map.forEach((attribute, mod) ->{
                            for (CruxAttributeModifier m : mod) {
                                MergedTagContainer modTags = TagContainer.merged(tagParser)
                                    .addAll(tags)
                                    .hook(m)
                                    ;
                                List<String> f = m.getOperation() == CruxAttribute.Operation.MULTIPLY ? modMultiplyFormat : modFormat;
                                list.addAll(ctx.deserializeStringList(f, modTags));
                            }
                        });
                    });
                }
                return list;
            }));
    }
}
