package killercreepr.cruxattributes.core.values;

import killercreepr.crux.api.data.Holder;
import killercreepr.cruxattributes.api.values.ValuesProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DefaultValues implements ValuesProvider {
    public static final Holder<List<String>> CRUX_ATTRIBUTES_ITEM_FORMAT = Holder.direct(List.of(
        "<gray><slot_group_when_in_slot>:"
    ));
    public static final Holder<List<String>> CRUX_ATTRIBUTES_ITEM_MODIFIER_FORMAT = Holder.direct(List.of(
        "{e{IF(\"<crux_attribute_is_negative:\"<crux_attribute_modifier_amount>\">\" == \"true\",\"<red>\", \"<blue>+\")}}{{<crux_attribute_modifier_amount>}[1]} <crux_attribute_instance_attribute/name>"
    ));
    public static final Holder<List<String>> CRUX_ATTRIBUTES_ITEM_MODIFIER_MULTIPLY_FORMAT = Holder.direct(List.of(
        "{e{IF(\"<crux_attribute_is_negative:\"<crux_attribute_modifier_amount>\">\" == \"true\",\"<red>\", \"<blue>+\")}}{{<crux_attribute_modifier_amount>*100}[1]}% <crux_attribute_instance_attribute/name>"
    ));
    @Override
    public @NotNull Holder<List<String>> CRUX_ATTRIBUTES_ITEM_FORMAT() {
        return CRUX_ATTRIBUTES_ITEM_FORMAT;
    }

    @Override
    public @NotNull Holder<List<String>> CRUX_ATTRIBUTES_ITEM_MODIFIER_FORMAT() {
        return CRUX_ATTRIBUTES_ITEM_MODIFIER_FORMAT;
    }

    @Override
    public @NotNull Holder<List<String>> CRUX_ATTRIBUTES_ITEM_MODIFIER_MULTIPLY_FORMAT() {
        return CRUX_ATTRIBUTES_ITEM_MODIFIER_MULTIPLY_FORMAT;
    }
}
