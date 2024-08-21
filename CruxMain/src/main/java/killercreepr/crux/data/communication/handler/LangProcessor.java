package killercreepr.crux.data.communication.handler;

import killercreepr.crux.Crux;
import killercreepr.crux.data.communication.TranslateMsg;
import killercreepr.crux.util.CruxReflect;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Level;

public class LangProcessor {
    public static void process(@NotNull Class<?> type){
        try{
            for (Field field : type.getDeclaredFields()) {
                process(field);
            }
        }catch (IllegalArgumentException ignored){
            ignored.printStackTrace();
        }
    }

    public static void process(@NotNull Field field){
        try{
            if(!Modifier.isStatic(field.getModifiers())) return;
            if(!field.canAccess(null)) return;

            if(!(field.get(null) instanceof TranslateMsg msg)) return;
            process(field, msg);
        }catch (IllegalArgumentException | IllegalAccessException ignored){
            ignored.printStackTrace();
        }
    }

    public static void process(@NotNull Field field, @NotNull TranslateMsg msg){
        try{
            if(msg.id() != null) return;
            Field idField = CruxReflect.getDeclaredField(msg.getClass(), "id");
            if(idField == null){
                Crux.log(Level.WARNING, field.getName() + " - " + msg + " - " + msg.getClass() + " - " + idField + " - Field, 'id' should not be null.");
                return;
            }
            boolean accessible = idField.canAccess(msg);
            idField.setAccessible(true);

            idField.set(msg, field.getName().toLowerCase());

            idField.setAccessible(accessible);
        }catch (IllegalArgumentException | IllegalAccessException ignored){
            ignored.printStackTrace();
        }
    }
}
