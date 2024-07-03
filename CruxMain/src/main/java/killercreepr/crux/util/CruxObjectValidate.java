package killercreepr.crux.util;

import org.jetbrains.annotations.Contract;

public class CruxObjectValidate {
    @Contract(pure = true)
    public static boolean checkNull(Object... objects){
        for(Object o : objects){
            if(o==null) return true;
        }
        return false;
    }

    public static void requireNotNull(Object... objects){
        for(Object o : objects){
            if(o==null) throw new NullPointerException();
        }
    }

    public static void requireNotNull(String msg, Object... objects){
        for(Object o : objects){
            if(o==null) throw new NullPointerException(msg);
        }
    }
}
