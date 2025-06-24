package killercreepr.cruxentities;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.tags.DamageTypeTagKeys;
import io.papermc.paper.tag.PostFlattenTagRegistrar;
import killercreepr.crux.core.Crux;
import killercreepr.cruxentities.registries.CruxEntityRegistries;
import org.bukkit.damage.DamageScaling;
import org.bukkit.damage.DamageType;

import java.util.List;

public class CruxEntitiesBootstrapper implements PluginBootstrap {
    @Override
    public void bootstrap(BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(RegistryEvents.DAMAGE_TYPE.freeze().newHandler(event -> {

            event.registry().register(
                RegistryKey.DAMAGE_TYPE.typedKey(Crux.key("entity_attack_calculate_custom")),
                builder -> builder.exhaustion(0.2f)
                    .messageId("entity_attack_calculate_custom")
                    .damageScaling(DamageScaling.ALWAYS)
            );
            event.registry().register(
                RegistryKey.DAMAGE_TYPE.typedKey(Crux.key("entity_projectile_calculate_custom")),
                builder -> builder.exhaustion(0.2f)
                    .messageId("entity_projectile_calculate_custom")
                    .damageScaling(DamageScaling.ALWAYS)
            );
        }));



        context.getLifecycleManager().registerEventHandler(LifecycleEvents.TAGS.postFlatten(RegistryKey.DAMAGE_TYPE), event -> {
            final PostFlattenTagRegistrar<DamageType> registrar = event.registrar();
            registrar.addToTag(
                DamageTypeTagKeys.BYPASSES_ARMOR,
                List.of(
                    TypedKey.create(RegistryKey.DAMAGE_TYPE, Crux.key("entity_attack_calculate_custom")),
                    TypedKey.create(RegistryKey.DAMAGE_TYPE, Crux.key("entity_projectile_calculate_custom"))
                )
            );
            registrar.addToTag(
                DamageTypeTagKeys.BYPASSES_RESISTANCE,
                List.of(
                    TypedKey.create(RegistryKey.DAMAGE_TYPE, Crux.key("entity_attack_calculate_custom")),
                    TypedKey.create(RegistryKey.DAMAGE_TYPE, Crux.key("entity_projectile_calculate_custom"))
                )
            );
            registrar.addToTag(
                DamageTypeTagKeys.BYPASSES_EFFECTS,
                List.of(
                    TypedKey.create(RegistryKey.DAMAGE_TYPE, Crux.key("entity_attack_calculate_custom")),
                    TypedKey.create(RegistryKey.DAMAGE_TYPE, Crux.key("entity_projectile_calculate_custom"))
                )
            );
            registrar.addToTag(
                DamageTypeTagKeys.BYPASSES_SHIELD,
                List.of(
                    TypedKey.create(RegistryKey.DAMAGE_TYPE, Crux.key("entity_attack_calculate_custom")),
                    TypedKey.create(RegistryKey.DAMAGE_TYPE, Crux.key("entity_projectile_calculate_custom"))
                )
            );
        });
    }
}
