package killercreepr.cruxenchants.core.enchant;

import killercreepr.cruxenchants.api.enchant.ApplicableItemGroup;
import killercreepr.cruxenchants.api.enchant.ApplicableItemType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SimpleApplicableItemGroup implements ApplicableItemGroup {
    protected final List<ApplicableItemType> types;
    protected final String formatted;
    public SimpleApplicableItemGroup(Collection<ApplicableItemType> types) {
        this.types = Collections.unmodifiableList(sort(types));
        this.formatted = buildFormatSymbols();
    }

    @Override
    public @NotNull Collection<ApplicableItemType> types() {
        return types;
    }

    public List<ApplicableItemType> sort(Collection<ApplicableItemType> types){
        List<ApplicableItemType> sorted = new ArrayList<>(types);
        sorted.sort(Comparator.comparing(e -> e.key().value()));
        return sorted;
    }

    @Override
    public @NotNull String formatSymbols() {
        return formatted;
    }

    public @NotNull String buildFormatSymbols() {
        StringBuilder builder = new StringBuilder("<font:\"crux:applicable_item_type\">");
        for(var t : types){
            builder.append(t.symbol());
        }
        builder.append("</font>");
        return builder.toString();
    }

    public static class Builder implements ApplicableItemGroup.Builder{
        protected final Collection<ApplicableItemType> types = new HashSet<>();
        @Override
        public ApplicableItemGroup.Builder add(ApplicableItemType... types) {
            this.types.addAll(Arrays.asList(types));
            return this;
        }

        @Override
        public ApplicableItemGroup.Builder addTypes(Collection<ApplicableItemType> types) {
            this.types.addAll(types);
            return this;
        }

        @Override
        public ApplicableItemGroup.Builder addGroups(Collection<ApplicableItemGroup> types) {
            for(var group : types){
                this.types.addAll(group.types());
            }
            return this;
        }

        @Override
        public ApplicableItemGroup.Builder add(ApplicableItemGroup... groups) {
            for(var group : groups){
                types.addAll(group.types());
            }
            return this;
        }

        @Override
        public ApplicableItemGroup build() {
            return new SimpleApplicableItemGroup(types);
        }
    }
}
