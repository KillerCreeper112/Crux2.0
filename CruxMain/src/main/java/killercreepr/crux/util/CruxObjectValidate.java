package killercreepr.crux.util;

public class CruxObjectValidate {
    public static void checkNotNull(Object... objects){
        for(Object o : objects){
            if(o==null) throw new NullPointerException();
        }
    }
}
