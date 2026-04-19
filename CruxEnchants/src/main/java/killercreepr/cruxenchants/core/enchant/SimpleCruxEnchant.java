package killercreepr.cruxenchants.core.enchant;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.cruxenchants.api.enchant.ApplicableItemGroup;
import killercreepr.cruxenchants.api.enchant.ApplicableItemType;
import killercreepr.cruxenchants.api.enchant.CruxEnchant;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

public class SimpleCruxEnchant implements CruxEnchant {
    protected final Key key;
    protected final String description;
    protected final ApplicableItemGroup applicableItemGroup;
    protected final DataComponentHandler components;

    public SimpleCruxEnchant(Key key, String description, ApplicableItemGroup applicableItemGroup, DataComponentHandler components) {
        this.key = key;
        this.description = description;
        this.applicableItemGroup = applicableItemGroup;
      this.components = components;
    }

    @Override
    public @NotNull Enchantment enchantment() {
        return Objects.requireNonNull(
            RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(key)
        );
    }

    @Override
    public ApplicableItemGroup applicableItemGroup() {
        return applicableItemGroup;
    }

    @Override
    public int maxLevel() {
        return enchantment().getMaxLevel();
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String displayName() {
        return PlainTextComponentSerializer.plainText().serialize(enchantment().description());
    }

    @Override
    public Component displayName(int level) {
        return enchantment().displayName(level);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public DataComponentHandler getComponents() {
        return components;
    }

    public static class Builder implements CruxEnchant.Builder{
        protected Key key;
        protected String description;
        protected Collection<ApplicableItemType> types;
        protected ApplicableItemGroup group;
        protected DataComponentHandler components;

        @Override
        public CruxEnchant.Builder key(Key key) {
            this.key = key;
            return this;
        }

        @Override
        public CruxEnchant.Builder description(String description) {
            this.description = description;
            return this;
        }

        @Override
        public CruxEnchant.Builder applicableItemGroup(ApplicableItemGroup group) {
            this.group = group;
            return this;
        }

        @Override
        public CruxEnchant.Builder applicableItemTypes(ApplicableItemType... types) {
            this.types = Arrays.asList(types);
            return this;
        }

        @Override
        public CruxEnchant.Builder editComponents(Consumer<DataComponentHandler> consumer) {
            components();
            consumer.accept(components);
            return this;
        }

        private void components(){
            if(components == null) components = DataComponentHandler.simple();
        }

        @Override
        public CruxEnchant build() {
            components();
            if(description == null) description = "";
            ApplicableItemGroup group;
            if(this.types != null){
                var builder = ApplicableItemGroup.builder()
                    .addTypes(this.types);
                if(this.group != null) builder.add(this.group);
                group = builder.build();
            }else group = this.group;
            return new SimpleCruxEnchant(key, description, group, components);
        }
    }
}
