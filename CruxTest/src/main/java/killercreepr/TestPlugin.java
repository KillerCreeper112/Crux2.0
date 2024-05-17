package killercreepr;

import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.crux.valueproviders.number.UniformNumber;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.common.json.container.GenericJsonHandler;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.registry.DefaultJsonRegistry;
import killercreepr.sometests.BlockBo;
import killercreepr.sometests.JsonCfgtest;
import killercreepr.sometests.JsonT;
import killercreepr.sometests.TestCf;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class TestPlugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        super.onEnable();
        getServer().getPluginManager().registerEvents(this, this);
        //getServer().getPluginManager().registerEvents(new MenuListener(), this);

        DefaultJsonRegistry.REGISTRY.registerContainerHandler(
                new GenericJsonHandler<>("block_bo", BlockBo.class)
        );

        cfg = new TestCf(this, "test");
        cfg.setup();

        DefaultJsonRegistry.REGISTRY.CONTAINER_REGISTRY.entrySet().forEach(entyr ->{
            getLogger().log(Level.WARNING, "ayo: " + entyr.getKey());
        });

        testJson = new JsonT(this, "test_json_boi");
        testJson.reloadIfNeeded();
        List<BlockBo> list = new ArrayList<>(){{
            add(new BlockBo(1, 0, 0));
            add(new BlockBo(0, 1, 0));
            add(new BlockBo(0, 0, 1));
        }};
        getLogger().log(Level.WARNING, "ayodwjfiowjhfoiw: " + list.getClass());
        testJson.add("prop", list);
        testJson.save();

        cfgtest = new JsonCfgtest(this, "test_my_man");
        cfgtest.setup();

        CruxConfig cfg = new CruxConfig(this, "testconfigyes");
        if(cfg.getAsYamlObject("test") instanceof YamlObject map){
            map.forEach((k, v) ->{
                if(v instanceof YamlObject sub){
                    sub.forEach((kk, vv) ->{
                        getLogger().log(Level.WARNING, k + " -> " + kk + " -> " + vv);
                    });
                    return;
                }
                getLogger().log(Level.WARNING, k + " -> " + v);
            });
        }
        cfg.set("test", new UniformNumber(2, 25));

        /*cfg.set("test", new HashMap<>(){{
            put(null, 2);
            put("test.test.hey", 10);
        }});*/
        cfg.save();

        provider = cfg.deserialize(NumberProvider.class, "test");
        getLogger().log(Level.WARNING, "AND THE CONFIG HAS FOUND test IN: " + provider);
    }
    protected NumberProvider provider;
    protected TestCf cfg;
    protected JsonT testJson;
    protected JsonCfgtest cfgtest;

    @EventHandler(ignoreCancelled = true)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Bukkit.broadcastMessage(cfgtest.STRING_MAN.value() + "");
        /*cfgtest.MAP_MAN.getOrDefault(Map.of()).forEach((k, v) ->{
            Bukkit.broadcastMessage(k + " -> " + v);
        });*/
        Player p = event.getPlayer();
        double v = p.getAttribute(Attribute.GENERIC_SCALE).getValue();
        p.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(v + provider.value().doubleValue());
    }
}
