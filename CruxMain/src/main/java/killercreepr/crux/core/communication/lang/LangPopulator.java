package killercreepr.crux.core.communication.lang;

import killercreepr.crux.api.communication.lang.CreateLang;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class LangPopulator {
    public static void populate(@NotNull CreateLang lang, @NotNull Class<?> type){
        try{
            for (Field field : type.getDeclaredFields()) {
                if(!Modifier.isStatic(field.getModifiers())) continue;
                if(!field.canAccess(null)) continue;

                if(!(field.get(null) instanceof TranslateMsg msg)) continue;
                if(msg.defaultValue()==null) continue;
                lang.put(msg.id(), msg.defaultValue());
            }
        }catch (IllegalArgumentException | IllegalAccessException ignored){}
    }
}
