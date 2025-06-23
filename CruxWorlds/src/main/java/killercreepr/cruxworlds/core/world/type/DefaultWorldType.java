package killercreepr.cruxworlds.core.world.type;

import killercreepr.cruxworlds.api.world.CruxWorld;
import killercreepr.cruxworlds.api.world.manager.CruxWorldManager;
import killercreepr.cruxworlds.api.world.type.CruxWorldType;
import net.kyori.adventure.key.Key;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DefaultWorldType implements CruxWorldType {
    protected final Key key;
    protected final Key defaultWorldKey;
    protected final CruxWorldManager worldManager;
    protected final World.Environment environment;

    public DefaultWorldType(Key key, Key defaultWorldKey, CruxWorldManager worldManager, World.Environment environment) {
        this.key = key;
        this.defaultWorldKey = defaultWorldKey;
        this.worldManager = worldManager;
        this.environment = environment;
    }
    public DefaultWorldType(Key key, CruxWorldManager worldManager, World.Environment environment) {
        this(
            key,
            environment == World.Environment.NORMAL ? Key.key("overworld") :
                environment == World.Environment.NETHER ? Key.key("the_nether") :
                    environment == World.Environment.THE_END ? Key.key("the_end") :
                        Key.key("custom"),
            worldManager, environment
        );
    }

    @Override
    public @NotNull CruxWorld generate(@NotNull Key name) {
        World world = new WorldCreator(name.value()).environment(environment).createWorld();
        if(world==null) throw new IllegalStateException("Cannot generate world! " + key());

        CruxWorld crux = worldManager.getWorld(name);
        Objects.requireNonNull(crux, "Unable to generate world " + name + "!");
        return crux;
    }

    @Override
    public @NotNull Key defaultWorldKey() {
        return defaultWorldKey;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
