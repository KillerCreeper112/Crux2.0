package killercreepr.cruxattributes.api.attribute;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import killercreepr.crux.api.entity.CruxEntity;
import killercreepr.cruxattributes.core.component.CruxAttributeComponents;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataHolder;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public interface CruxAttributeCacheHandler {
    static CruxAttributeCacheHandler attributeCacheHandler(){
        return STANDARD;
    }
    CruxAttributeCacheHandler STANDARD = new Simple();

    <P extends PersistentDataHolder> CruxAttributeHandler getCache(P item);
    <P extends PersistentDataHolder> CruxAttributeHandler removeCache(P item);
    <P extends PersistentDataHolder> CruxAttributeHandler getOrCreateCache(P item);
    void saveAll();


    class Simple implements CruxAttributeCacheHandler{
        private final Cache<UUID, Value> CACHE = CacheBuilder.newBuilder()
            .maximumSize(3000)
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .softValues()
            .initialCapacity(50)
            .removalListener(notification -> {
                if(!(notification.getValue() instanceof Value(Reference<Entity> entity, CruxAttributeHandler handler))) return;
                var e = entity.get();
                if(e != null){
                    saveAttributes(e, handler);
                }
            })
            .build();

        public void saveAttributes(Entity e, CruxAttributeHandler handler){
            CruxEntity.entity(e).set(CruxAttributeComponents.CRUX_ATTRIBUTES,
                CruxAttributeContainer.container((Collection<CruxAttributeInstance>) handler.getInstances()));
        }

        @Override
        public <P extends PersistentDataHolder> CruxAttributeHandler getCache(P item) {
            if(!(item instanceof Entity e)) return null;
            var value = CACHE.getIfPresent(e.getUniqueId());
            if(value != null) return value.handler();
            return null;
        }

        @Override
        public <P extends PersistentDataHolder> CruxAttributeHandler removeCache(P item) {
            if(!(item instanceof Entity e)) return null;
            var got = getCache(item);
            CACHE.invalidate(e.getUniqueId());
            return got;
        }

        @Override
        public <P extends PersistentDataHolder> CruxAttributeHandler getOrCreateCache(P item) {
            if(!(item instanceof Entity e)) return null;
            try{
                var gotted = getCache(item);
                if(gotted != null) return gotted;
                if(!e.isValid()){
                    return null;
                }

                var got = CACHE.get(e.getUniqueId(), () ->
                    new Value(new WeakReference<>(e), CruxAttributeHandler.builder().addAll(CruxAttribute.getInstancesRaw(item)).build()));
                return got.handler();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        public void saveAll() {
            CACHE.asMap().values().forEach(value ->{
                Entity e = value.entity().get();
                if(e != null) saveAttributes(e, value.handler());
            });
        }

        private record Value(Reference<Entity> entity, CruxAttributeHandler handler){ }
    }
}
