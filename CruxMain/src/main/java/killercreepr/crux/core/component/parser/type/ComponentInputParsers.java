package killercreepr.crux.core.component.parser.type;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.block.tag.BlockTag;
import killercreepr.crux.api.communication.CreateSound;
import killercreepr.crux.api.component.parser.StringListEncodeComponent;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.api.data.ParticleBuilderSupplier;
import killercreepr.crux.api.entity.consumer.CruxEntityConsumer;
import killercreepr.crux.api.entity.predicate.EntityPredicate;
import killercreepr.crux.api.item.component.ToolComponent;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import killercreepr.crux.api.item.tag.ItemTag;
import killercreepr.crux.api.key.tag.KeyPredicate;
import killercreepr.crux.api.key.tag.KeyTag;
import killercreepr.crux.api.loot.item.ItemLootTable;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.api.valueproviders.vector.NumberVector;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.parser.hybrid.text.ListPersistTextParser;
import killercreepr.crux.core.entity.predicate.EntityAllPredicate;
import killercreepr.crux.core.entity.predicate.EntityAnyPredicate;
import killercreepr.crux.core.item.predicate.ItemAllPredicate;
import killercreepr.crux.core.item.predicate.ItemAnyPredicate;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.core.util.CruxKey;
import killercreepr.crux.paper.ItemHolder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.logging.Level;

public class ComponentInputParsers {
    public static PersistTextParser<Particle> PARTICLE = PersistTextParser.elementBuilder(Particle.class)
        .field(TextInputField.field(PersistTextParser.KEY, Keyed::key))
        .apply(ctx ->{
            Key key = ctx.get();
            return Registry.PARTICLE_TYPE.get(key);
        });
    public static PersistTextParser<Enchantment> ENCHANTMENT = PersistTextParser.elementBuilder(Enchantment.class)
        .field(TextInputField.field(PersistTextParser.KEY, Keyed::key))
        .apply(ctx ->{
            Key key = ctx.get();
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(key);
        });

    public static PersistTextParser<UUID> UUID = PersistTextParser.elementBuilder(UUID.class)
        .field(TextInputField.field(PersistTextParser.BYTE_ARRAY, d ->{
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(d.getMostSignificantBits());
            bb.putLong(d.getLeastSignificantBits());
            return bb.array();
        }))
        .apply(ctx ->{
            ByteBuffer bb = ByteBuffer.wrap(ctx.get());
            long firstLong = bb.getLong();
            long secondLong = bb.getLong();
            return new UUID(firstLong, secondLong);
        });

    public static PersistTextParser<NumberProvider> NUMBER_PROVIDER = PersistTextParser.elementBuilder(NumberProvider.class)
        .field(TextInputField.field(PersistTextParser.STRING, NumberProvider::serializeToString))
        .apply(ctx ->{
            String input = ctx.get();
            return NumberProvider.parseFromString(input);
        });

    public static PersistTextParser<CruxEntityConsumer> CRUX_ENTITY_CONSUMER = PersistTextParser.elementBuilder(CruxEntityConsumer.class)
        .field(TextInputField.field(PersistTextParser.KEY, CruxEntityConsumer::key))
        .apply(ctx ->{
            Key key = ctx.get();
            return Objects.requireNonNull(
                CruxRegistries.ENTITY_CONSUMER.get(key),
                "CruxEntityConsumer of " + key + " not found!"
            );
        });

    public static PersistTextParser<NumberVector> NUMBER_VECTOR = PersistTextParser.mapBuilder(NumberVector.class)
        .field("x", TextInputField.field(NUMBER_PROVIDER, NumberVector::x))
        .field("y", TextInputField.field(NUMBER_PROVIDER, NumberVector::y))
        .field("z", TextInputField.field(NUMBER_PROVIDER, NumberVector::z))
        .apply(ctx ->{
            return NumberVector.vector(
                ctx.get("x"),
                ctx.get("y"),
                ctx.get("z")
            );
        });

    public static PersistTextParser<ItemHolder> ITEM_HOLDER = PersistTextParser.elementBuilder(ItemHolder.class)
        .field(TextInputField.field(PersistTextParser.KEY, ItemHolder::key))
        .apply(ctx ->{
            Key key = ctx.get();
            return Objects.requireNonNull(
                Crux.handlers().item().getItem(key),
                "ItemHolder of " + key + " not found!"
            );
        });

    public static PersistTextParser<ItemLootTable> ITEM_LOOT_TABLE = PersistTextParser.elementBuilder(ItemLootTable.class)
        .field(TextInputField.field(PersistTextParser.KEY, Keyed::key))
        .apply(ctx ->{
            Key key = ctx.get();
            return (ItemLootTable) Objects.requireNonNull(
                CruxRegistries.ITEM_LOOT_TABLE.get(key),
                "ItemLootTable of " + key + " not found!"
            );
        });

    public static PersistTextParser<EntityPredicate> SIMPLE_ENTITY_PREDICATE = PersistTextParser.elementBuilder(EntityPredicate.class)
        .field(TextInputField.field(PersistTextParser.STRING, e ->{
            if(!(e instanceof StringListEncodeComponent all)) throw new IllegalArgumentException(
                "EntityPredicate must implement StringListEncode! " + e
            );
            return all.encodeToParser().getFirst();
        }))
        .apply(ctx ->{
            String id = ctx.get();
            boolean invert = id.startsWith("!");
            if(invert) id = id.substring(1);
            EntityPredicate p;
            if(id.startsWith("#")){
                var tag = CruxRegistries.ENTITY_TAG.get(Crux.key(id.substring(1)));
                if(tag == null){
                    Crux.log(Level.SEVERE, "Cannot find item tag of " + id + "!");
                    return null;
                }
                p = EntityPredicate.fromTag(tag);
            }else{
                p = EntityPredicate.fromType(Crux.key(id));
            }
            return invert ? EntityPredicate.fromInverted(p) : p;
        });

    public static PersistTextParser<EntityPredicate> ENTITY_PREDICATE = PersistTextParser.mapBuilder(EntityPredicate.class)
        .field("type", TextInputField.field(PersistTextParser.STRING, e ->{
            if(e instanceof EntityAllPredicate) return "all_of";
            if(e instanceof EntityAnyPredicate) return "any_of";
            return null;
        }))
        .field("terms", TextInputField.field(PersistTextParser.LIST.STRING, e ->{
            if(!(e instanceof StringListEncodeComponent c)) return null;
            return c.encodeToParser();
        }))
        .apply(ctx ->{
            if(!(ctx.get() instanceof List<?>) && !(ctx.get() instanceof Map<?,?>)){
                return SIMPLE_ENTITY_PREDICATE.decodeObject(ctx.get());
            }
            if(!(ctx.getOptional("terms") instanceof List<?> list)){
                return SIMPLE_ENTITY_PREDICATE.decodeObject(ctx.get());
            }

            Collection<EntityPredicate> parsed = new HashSet<>();
            for(Object s : list){
                var predicate = SIMPLE_ENTITY_PREDICATE.decodeObject(s);
                parsed.add(predicate);
            }
            String type = ctx.getOptional("type", "all_of");
            switch (type.toLowerCase()){
                case "all_of" ->{
                    return EntityPredicate.fromAllOf(parsed);
                }
                case "any_of" ->{
                    return EntityPredicate.fromAnyOf(parsed);
                }
            }
            return EntityPredicate.fromAllOf(parsed);
        });

    public static PersistTextParser<ItemPredicate> SIMPLE_ITEM_PREDICATE = PersistTextParser.elementBuilder(ItemPredicate.class)
        .field(TextInputField.field(PersistTextParser.STRING, e ->{
            if(!(e instanceof StringListEncodeComponent all)) throw new IllegalArgumentException(
                "ItemPredicate must implement StringListEncode! " + e
            );
            return all.encodeToParser().getFirst();
        }))
        .apply(ctx ->{
            String id = ctx.get();
            boolean invert = id.startsWith("!");
            if(invert) id = id.substring(1);
            ItemPredicate p;
            if(id.startsWith("#")){
                ItemTag tag = CruxRegistries.ITEM_TAG.get(Crux.key(id.substring(1)));
                if(tag == null){
                    Crux.log(Level.SEVERE, "Cannot find item tag of " + id + "!");
                    return null;
                }
                p = ItemPredicate.fromTag(tag);
            }else{
                p = ItemPredicate.fromType(Crux.key(id));
            }
            return invert ? ItemPredicate.fromInverted(p) : p;
        });

    public static PersistTextParser<ItemPredicate> ITEM_PREDICATE = PersistTextParser.mapBuilder(ItemPredicate.class)
        .field("type", TextInputField.field(PersistTextParser.STRING, e ->{
            if(e instanceof ItemAllPredicate) return "all_of";
            if(e instanceof ItemAnyPredicate) return "any_of";
            return null;
        }))
        .field("terms", TextInputField.field(PersistTextParser.LIST.STRING, e ->{
            if(!(e instanceof StringListEncodeComponent c)) return null;
            return c.encodeToParser();
        }))
        .apply(ctx ->{
            if(!(ctx.getOptional("terms") instanceof List<?> list)){
                return SIMPLE_ITEM_PREDICATE.decodeObject(ctx.get());
            }

            Collection<ItemPredicate> parsed = new HashSet<>();
            for(Object s : list){
                ItemPredicate predicate = SIMPLE_ITEM_PREDICATE.decodeObject(s);
                parsed.add(predicate);
            }
            String type = ctx.getOptional("type", "all_of");
            switch (type.toLowerCase()){
                case "all_of" ->{
                    return ItemPredicate.fromAllOf(parsed);
                }
                case "any_of" ->{
                    return ItemPredicate.fromAnyOf(parsed);
                }
            }
            return ItemPredicate.fromAllOf(parsed);
        });

    public static PersistTextParser<BlockPredicate> SIMPLE_BLOCK_PREDICATE = PersistTextParser.elementBuilder(BlockPredicate.class)
        .field(TextInputField.field(PersistTextParser.STRING, e ->{
            if(!(e instanceof StringListEncodeComponent all)) throw new IllegalArgumentException(
                "BlockPredicate must be a StringListEncodeComponent! " + e
            );
            return all.encodeToParser().getFirst();
        }))
        .apply(ctx ->{
            String id = ctx.get();
            boolean invert = id.startsWith("!");
            if(invert) id = id.substring(1);
            BlockPredicate p;
            if(id.startsWith("#")){
                BlockTag tag = CruxRegistries.BLOCK_TAG.get(Crux.key(id.substring(1)));
                if(tag == null) return null;
                p = BlockPredicate.fromTag(tag);
            }else p = BlockPredicate.fromType(Crux.key(id));
            return invert ? BlockPredicate.fromInverted(p) : p;
        });

    public static PersistTextParser<BlockPredicate> BLOCK_PREDICATE = PersistTextParser.elementBuilder(BlockPredicate.class)
        .field(TextInputField.field(PersistTextParser.LIST.STRING, e ->{
            if(!(e instanceof StringListEncodeComponent all)) throw new IllegalArgumentException(
                "BlockPredicate must be a BlockPredicateComponent! " + e
            );
            return all.encodeToParser();
        }))
        .apply(ctx ->{
            if(!(ctx.get() instanceof List<?>)){
                return SIMPLE_BLOCK_PREDICATE.decodeObject(ctx.get());
            }
            Collection<String> list = ctx.get();
            Collection<BlockPredicate> parsed = new HashSet<>();
            for(String s : list){
                BlockPredicate predicate = SIMPLE_BLOCK_PREDICATE.decodeObject(s);
                parsed.add(predicate);
            }
            return BlockPredicate.fromAllOf(parsed);
        });

    public static PersistTextParser<ToolComponent.Rule> TOOL_RULE = PersistTextParser.mapBuilder(ToolComponent.Rule.class)
        .field("blocks", TextInputField.field(BLOCK_PREDICATE, ToolComponent.Rule::getBlocks))
        .field("speed", TextInputField.field(PersistTextParser.FLOAT, ToolComponent.Rule::getSpeed))
        .field("correct_for_drops", TextInputField.field(PersistTextParser.BOOLEAN, ToolComponent.Rule::getCorrectForDrops))
        .apply(ctx -> new ToolComponent.Simple.Rule(ctx.getOptional("blocks"), ctx.getOptional("speed"), ctx.getOptional("correct_for_drops")));

    public static PersistTextParser<ToolComponent> TOOL = PersistTextParser.mapBuilder(ToolComponent.class)
        .field("default_mining_speed", TextInputField.field(PersistTextParser.FLOAT, ToolComponent::getDefaultMiningSpeed))
        .field("rules", TextInputField.field(new ListPersistTextParser<>(TOOL_RULE, null), ToolComponent::getRules))
        .apply(ctx -> new ToolComponent.Simple(ctx.getOptional("default_mining_speed", 1f), ctx.getOptional("rules")));

    public static PersistTextParser<Particle.DustOptions> PARTICLE_DUST_OPTIONS = PersistTextParser.mapBuilder(Particle.DustOptions.class)
        .field("color", TextInputField.field(PersistTextParser.COLOR, Particle.DustOptions::getColor))
        .field("size", TextInputField.field(PersistTextParser.FLOAT, Particle.DustOptions::getSize))
        .apply(ctx ->{
            Color color = ctx.getOptional("color", Color.WHITE);
            float size = ctx.getOptional("size", 1f);
            return new Particle.DustOptions(color, size);
        });
    public static PersistTextParser<Particle.DustTransition> PARTICLE_DUST_TRANSITION = PersistTextParser.mapBuilder(Particle.DustTransition.class)
        .field("from_color", TextInputField.field(PersistTextParser.COLOR, Particle.DustOptions::getColor))
        .field("to_color", TextInputField.field(PersistTextParser.COLOR, Particle.DustOptions::getColor))
        .field("size", TextInputField.field(PersistTextParser.FLOAT, Particle.DustOptions::getSize))
        .apply(ctx ->{
            Color fromColor = ctx.getOptional("from_color", Color.WHITE);
            Color toColor = ctx.getOptional("to_color", Color.WHITE);
            float size = ctx.getOptional("size", 1f);
            return new Particle.DustTransition(fromColor, toColor, size);
        });

    public static PersistTextParser<ParticleBuilderSupplier> PARTICLE_BUILDER_SUPPLIER = PersistTextParser.mapBuilder(ParticleBuilderSupplier.class)
        .field("particle", TextInputField.field(PARTICLE, e -> e.particle().value()))
        .field("offset", TextInputField.field(NUMBER_VECTOR, ParticleBuilderSupplier::offset))
        .field("count", TextInputField.field(NUMBER_PROVIDER, ParticleBuilderSupplier::count))
        .field("extra", TextInputField.field(NUMBER_PROVIDER, ParticleBuilderSupplier::extra))
        .field("item", TextInputField.field(PersistTextParser.KEY, e ->{
            if(!(e.openData() instanceof ItemStack i)) return null;
            return Crux.handlers().item().getType(i);
        }))
        .field("block_data", TextInputField.field(PersistTextParser.KEY, e ->{
            if(!(e.openData() instanceof BlockData i)) return null;
            return i.getMaterial().key();
        }))
        .field("dust_options", TextInputField.field(PARTICLE_DUST_OPTIONS, e ->{
            if(!(e.openData() instanceof Particle.DustOptions i)) return null;
            return i;
        }))
        .field("dust_transition", TextInputField.field(PARTICLE_DUST_TRANSITION, e ->{
            if(!(e.openData() instanceof Particle.DustTransition i)) return null;
            return i;
        }))
        .apply(ctx ->{
            Particle particle = ctx.getOptional("particle", Particle.FIREWORK);
            NumberVector offset = ctx.getOptional("offset");
            NumberProvider count = ctx.getOptional("count");
            NumberProvider extra = ctx.getOptional("extra");

            Object data = null;
            if(ctx.getOptional("item") instanceof Key k){
                data = Crux.handlers().item().getItem(k).value();
            }else if(ctx.getOptional("block_data") instanceof Key k) {
                data = Registry.MATERIAL.get(k).createBlockData();
            }else if(ctx.getOptional("dust_options") instanceof Particle.DustOptions k) {
                data = k;
            }else if(ctx.getOptional("dust_transition") instanceof Particle.DustTransition k) {
                data = k;
            }

            return ParticleBuilderSupplier.builder()
                .particle(particle)
                .offset(offset)
                .count(count)
                .extra(extra)
                .data(data)
                .build();
        });

    public static PersistTextParser<PotionEffectType> POTION_EFFECT_TYPE = PersistTextParser.elementBuilder(PotionEffectType.class)
        .field(TextInputField.field(PersistTextParser.KEY, PotionEffectType::key))
        .apply(ctx ->{
            Key key = ctx.get();
            return Registry.MOB_EFFECT.get(key);
        });

    public static PersistTextParser<EquipmentSlot> EQUIPMENT_SLOT = PersistTextParser.elementBuilder(EquipmentSlot.class)
        .field(TextInputField.field(PersistTextParser.STRING, e -> e.toString().toLowerCase()))
        .apply(ctx ->{
            String id = ctx.get();
            return EquipmentSlot.valueOf(id.toUpperCase());
        });

    public static PersistTextParser<EntityType> ENTITY_TYPE = PersistTextParser.elementBuilder(EntityType.class)
        .field(TextInputField.field(PersistTextParser.KEY, EntityType::getKey))
        .apply(ctx -> RegistryAccess.registryAccess().getRegistry(RegistryKey.ENTITY_TYPE).get((Key) ctx.get()));

    public static PersistTextParser<PotionEffect> POTION_EFFECT = PersistTextParser.mapBuilder(PotionEffect.class)
        .field("type", TextInputField.field(POTION_EFFECT_TYPE, PotionEffect::getType))
        .field("duration", TextInputField.field(PersistTextParser.INTEGER, e -> e.getDuration() == 600 ? null : e.getDuration()))
        .field("amplifier", TextInputField.field(PersistTextParser.INTEGER, e -> e.getAmplifier() == 0 ? null : e.getAmplifier()))
        .field("ambient", TextInputField.field(PersistTextParser.BOOLEAN, e -> e.isAmbient() ? e.isAmbient() : null))
        .field("particles", TextInputField.field(PersistTextParser.BOOLEAN, e -> e.hasParticles() ? null : e.hasParticles()))
        .field("icon", TextInputField.field(PersistTextParser.BOOLEAN, e -> e.hasIcon() ? null : e.hasIcon()))
        .apply(ctx ->{
            PotionEffectType type = ctx.get("type");
            int duration = ctx.getOptional("duration", 600);
            int amplifier = ctx.getOptional("amplifier", 0);
            boolean ambient = ctx.getOptional("ambient", false);
            boolean particles = ctx.getOptional("particles", true);
            boolean icon = ctx.getOptional("icon", true);
            return new PotionEffect(type, duration, amplifier, ambient, particles, icon);
        });

    public static PersistTextParser<CreateSound> CREATE_SOUND = PersistTextParser.mapBuilder(CreateSound.class)
        .field("sound_id",TextInputField.field(PersistTextParser.KEY, e -> e.getSound().name()))
        .field("pitch",TextInputField.field(PersistTextParser.FLOAT, e -> e.getSound().pitch()))
        .field("volume",TextInputField.field(PersistTextParser.FLOAT, e -> e.getSound().volume()))
        .apply(ctx ->{
            Key key = ctx.get("sound_id");
            float volume = ctx.getOptional("volume", 2f);
            float pitch = ctx.getOptional("pitch", 1f);
            return CreateSound.sound(key, Sound.Source.MASTER, volume, pitch);
        });

    public static PersistTextParser<KeyPredicate> SIMPLE_KEY_PREDICATE = PersistTextParser.elementBuilder(KeyPredicate.class)
        .field(TextInputField.field(PersistTextParser.STRING, e ->{
            if(!(e instanceof StringListEncodeComponent all)) throw new IllegalArgumentException(
                "KeyPredicate must implement StringListEncode! " + e
            );
            return all.encodeToParser().getFirst();
        }))
        .apply(ctx -> parseKeyPredicate(ctx.get()));

    private static KeyPredicate parseKeyPredicate(Object o){
        if(o instanceof List<?> list){
            Collection<KeyPredicate> parsed = new ArrayList<>();
            for(Object oo : list){
                KeyPredicate pp = SIMPLE_KEY_PREDICATE.decodeObject(oo);
                if(pp==null) continue;
                parsed.add(pp);
            }
            return KeyPredicate.fromAllOf(parsed);
        }

        String id = o.toString();
        boolean invert = id.startsWith("!");
        if(invert) id = id.substring(1);
        KeyPredicate p;
        if(id.startsWith("#")){
            KeyTag tag = CruxRegistries.KEY_TAG.get(Crux.key(id.substring(1)));
            if(tag == null){
                Crux.log(Level.SEVERE, "Cannot find key tag of " + id + "!");
                return null;
            }
            p = KeyPredicate.fromTag(tag);
        }else{
            p = KeyPredicate.fromType(Crux.key(id));
        }
        return invert ? KeyPredicate.fromInverted(p) : p;
    }

    public static PersistTextParser<KeyPredicate> KEY_PREDICATE = PersistTextParser.mapBuilder(KeyPredicate.class)
        .field("type", TextInputField.field(PersistTextParser.STRING, e ->{
            if(e instanceof ItemAllPredicate) return "all_of";
            if(e instanceof ItemAnyPredicate) return "any_of";
            return null;
        }))
        .field("terms", TextInputField.field(PersistTextParser.LIST.STRING, e ->{
            if(!(e instanceof StringListEncodeComponent c)) return null;
            return c.encodeToParser();
        }))
        .apply(ctx ->{
            if(!(ctx.getOptional("terms") instanceof List<?> list)){
                return SIMPLE_KEY_PREDICATE.decodeObject(ctx.get());
            }

            Collection<KeyPredicate> parsed = new HashSet<>();
            for(Object s : list){
                KeyPredicate predicate = SIMPLE_KEY_PREDICATE.decodeObject(s);
                parsed.add(predicate);
            }
            String type = ctx.getOptional("type", "all_of");
            switch (type.toLowerCase()){
                case "all_of" ->{
                    return KeyPredicate.fromAllOf(parsed);
                }
                case "any_of" ->{
                    return KeyPredicate.fromAnyOf(parsed);
                }
            }
            return KeyPredicate.fromAllOf(parsed);
        });


    public static PersistTextParser<AttributeModifier.Operation> ATTRIBUTE_OPERATION = PersistTextParser.elementBuilder(AttributeModifier.Operation.class)
        .field(TextInputField.field(PersistTextParser.STRING, e -> e.toString().toLowerCase()))
        .apply(ctx ->{
            String s = ctx.get();
            if(s.equalsIgnoreCase("add")) return AttributeModifier.Operation.ADD_NUMBER;
            if(s.equalsIgnoreCase("multiply")) return AttributeModifier.Operation.MULTIPLY_SCALAR_1;
            return AttributeModifier.Operation.valueOf(s.toUpperCase());
        });

    public static PersistTextParser<EquipmentSlotGroup> EQUIPMENT_SLOT_GROUP = PersistTextParser.elementBuilder(EquipmentSlotGroup.class)
        .field(TextInputField.field(PersistTextParser.STRING, e -> e.toString().toLowerCase()))
        .apply(ctx -> EquipmentSlotGroup.getByName(ctx.get()));

    public static PersistTextParser<Attribute> ATTRIBUTE = PersistTextParser.elementBuilder(Attribute.class)
        .field(TextInputField.field(PersistTextParser.KEY, org.bukkit.Keyed::key))
        .apply(ctx ->{
            Key key = ctx.get();
            return Registry.ATTRIBUTE.get(key);
        });

    public static PersistTextParser<AttributeModifier> ATTRIBUTE_MODIFIER = PersistTextParser.mapBuilder(AttributeModifier.class)
        .field("key",TextInputField.field(PersistTextParser.KEY, e -> e.getKey()))
        .field("amount",TextInputField.field(PersistTextParser.DOUBLE, e -> e.getAmount()))
        .field("operation",TextInputField.field(ATTRIBUTE_OPERATION, e -> e.getOperation()))
        .field("slot",TextInputField.field(EQUIPMENT_SLOT_GROUP, e -> e.getSlotGroup()))
        .apply(ctx ->{
            Key key = ctx.get("key");
            double amount = ctx.get("amount");
            AttributeModifier.Operation operation = ctx.getOptional("operation", AttributeModifier.Operation.ADD_NUMBER);
            EquipmentSlotGroup slot = ctx.getOptional("slot", EquipmentSlotGroup.ANY);
            return new AttributeModifier(CruxKey.key(key), amount, operation, slot);
        });

    public static final ComponentInputListParsers LIST = new ComponentInputListParsers();
    public static final ComponentInputMapParsers MAP = new ComponentInputMapParsers();
}
