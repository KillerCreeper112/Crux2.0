package killercreepr.crux.util;

public class InvUtil {
    public static int getInventorySize(int length){
        int quotient = (int) Math.ceil(length / 9f);
        return quotient > 5 ? 54 : quotient * 9;
    }
}
