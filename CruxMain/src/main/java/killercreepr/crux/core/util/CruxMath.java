package killercreepr.crux.core.util;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import killercreepr.crux.api.math.CruxPosition;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redempt.crunch.Crunch;
import redempt.crunch.functional.EvaluationEnvironment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class CruxMath {
    @Deprecated
    public static final Random RANDOM = new Random();
    public static Random random(){
        return RANDOM;
    }
    public static final CruxNumberFormat DECIMAL_FORMAT_0 = new CruxNumberFormat(0);
    public static final CruxNumberFormat DECIMAL_FORMAT_1 = new CruxNumberFormat(1);
    public static final CruxNumberFormat DECIMAL_FORMAT_2 = new CruxNumberFormat(2);
    public static final CruxNumberFormat DECIMAL_FORMAT_3 = new CruxNumberFormat(3);
    public static int calculateLevelsForExperiencePoints(int points) {
        if (points <= 352) { // Level 0-16
            return (int) Math.floor(Math.sqrt(points + 9) - 3);
        } else if (points <= 1507) { // Level 17-31
            return (int) Math.floor(8.1 + Math.sqrt(0.4 * (points - (7839.0 / 40.0))));
        } else { // 32+
            return (int) Math.floor((325.0 / 18.0) + Math.sqrt((2.0 / 9.0) * (points - (54215.0 / 72.0))));
        }
    }

    /**
     * @param skewFactor The higher this number, the more likely it will be that
     *                   this function generates a higher number. The lower it is, the more likely
     *                   it is that it will generate a lower number.
     *                   For example, a skew factor of 0.2 will make it more likely for the function
     *                   to generate a lower number.
     *                   A skew factor of 2 will make it more likely for the function to generate a higher number.
     *                   A skew factor of 1 will have equal chance (no skewed results).
     */
    public static int randomSkewed(int minValue, int maxValue, double skewFactor) {
        return randomSkewed(random(), minValue, maxValue, skewFactor);
    }
    public static long randomSkewed(long minValue, long maxValue, double skewFactor) {
        return randomSkewed(random(), minValue, maxValue, skewFactor);
    }
    public static double randomSkewed(double minValue, double maxValue, double skewFactor) {
        return randomSkewed(random(), minValue, maxValue, skewFactor);
    }
    public static float randomSkewed(float minValue, float maxValue, double skewFactor) {
        return randomSkewed(random(), minValue, maxValue, skewFactor);
    }
    public static int randomSkewed(Random random, int minValue, int maxValue, double skewFactor) {
        return (int) randomSkewed(random, (double) minValue, maxValue, skewFactor);
    }

    public static double distanceFalloff(@NotNull Entity hit, @NotNull Entity hitFrom, double maxRange, double effectiveness){
        return distanceFalloff(hit.getLocation(), hitFrom.getLocation(), maxRange, effectiveness);
    }

    public static double distanceFalloff(@NotNull Location hit, @NotNull Location hitFrom, double maxRange, double effectiveness){
        double dis = hit.distanceSquared(hitFrom);
        return distanceFalloff(dis, maxRange, effectiveness);
    }

    /**
     *
     * @param right In degrees. Negative values will rotate to left
     * @param up In degrees. Negative values will rotate downwards
     */
    @Contract(pure = true)
    public @NotNull Vector rotateDirection(@NotNull Vector dir, double right, double up) {
        // Normalize original direction
        Vector forwardVec = dir.clone().normalize();

        // Convert dir into yaw + pitch
        double yaw = Math.atan2(-forwardVec.getX(), forwardVec.getZ()); // yaw in radians
        double pitch = Math.asin(forwardVec.getY()); // pitch in radians

        // Convert your inputs (degrees) to radians
        double yawOffset = Math.toRadians(right);
        double pitchOffset = Math.toRadians(up);

        // Apply rotation
        yaw += yawOffset;
        pitch += pitchOffset;

        // Clamp pitch to avoid flipping over (optional)
        double maxPitch = Math.toRadians(89.9);
        if (pitch > maxPitch) pitch = maxPitch;
        if (pitch < -maxPitch) pitch = -maxPitch;

        // Rebuild vector from yaw + pitch
        double x = -Math.sin(yaw) * Math.cos(pitch);
        double y = Math.sin(pitch);
        double z = Math.cos(yaw) * Math.cos(pitch);

        return new Vector(x, y, z).normalize();
    }


    /**
     *
     * @param effectiveness The higher this is, the slower the result will drop off
     *                      The lower it is, the faster it will drop off
     *                      1 = linear drop off
     *                      2 = slower drop off
     *                      0.5 = faster drop off
     */
    public static double distanceFalloff(double distanceSquared, double maxRange, double effectiveness){
        return Math.max(1D-(distanceSquared/((maxRange*maxRange)*effectiveness)), 0D);
    }

    /**
     * @param skewFactor The higher this number, the more likely it will be that
     *                   this function generates a higher number. The lower it is, the more likely
     *                   it is that it will generate a lower number.
     *                   For example, a skew factor of 0.2 will make it more likely for the function
     *                   to generate a lower number.
     *                   A skew factor of 2 will make it more likely for the function to generate a higher number.
     *                   A skew factor of 1 will have equal chance (no skewed results).
     */
    public static double randomSkewed(Random random, double minValue, double maxValue, double skewFactor) {
        double randomValue = random.nextDouble();
        double skewed = Math.pow(randomValue, 1.0 / skewFactor);
        return minValue + skewed * (maxValue - minValue);
    }

    public static float randomSkewed(Random random, float minValue, float maxValue, double skewFactor) {
        return (float) randomSkewed(random, (double) minValue, maxValue, skewFactor);
    }
    public static long randomSkewed(Random random, long minValue, long maxValue, double skewFactor) {
        return (long) randomSkewed(random, (double) minValue, maxValue, skewFactor);
    }

    public static void rotateVector(Vector vector, float yaw, float pitch) {
        double yawRad = Math.toRadians(-yaw); // Negative yaw for Minecraft coordinate system
        double pitchRad = Math.toRadians(-pitch);

        // First, rotate around the Y-axis (yaw)
        double x = vector.getX();
        double z = vector.getZ();
        double newX = x * Math.cos(yawRad) - z * Math.sin(yawRad);
        double newZ = x * Math.sin(yawRad) + z * Math.cos(yawRad);

        // Then, rotate around the X-axis (pitch)
        double y = vector.getY();
        double newY = y * Math.cos(pitchRad) - newZ * Math.sin(pitchRad);  // Corrected Y rotation
        newZ = y * Math.sin(pitchRad) + newZ * Math.cos(pitchRad);  // Corrected Z rotation

        // Set the new values to the vector
        vector.setX(newX);
        vector.setY(newY);
        vector.setZ(newZ);
    }

    public static int floorBlock(double num) {
        int floor = (int)num;
        return (double)floor == num ? floor : floor - (int)(Double.doubleToRawLongBits(num) >>> 63);
    }

    public static CruxNumberFormat buildOrGetDecimalFormat(int decimalPlaces){
        return switch (decimalPlaces){
            case 0 -> DECIMAL_FORMAT_0;
            case 1 -> DECIMAL_FORMAT_1;
            case 2 -> DECIMAL_FORMAT_2;
            case 3 -> DECIMAL_FORMAT_3;
            default -> new CruxNumberFormat(decimalPlaces);
        };
    }

    public static float clampClosestYaw(float yaw){
        yaw = (yaw + 360) % 360;

        if (yaw >= 315 || yaw < 45) {
            return 0f; //north
        } else if (yaw >= 45 && yaw < 135) {
            return 90f; //BlockFace.EAST;
        } else if (yaw >= 135 && yaw < 225) {
            return 180f; //south
        } else if (yaw >= 225 && yaw < 315) {
            return -90f; //BlockFace.WEST;
        }
        return yaw;
    }

    public static float clampClosestPitch(float pitch) {
        // Normalize pitch to [-90, 90] range
        pitch = Math.max(-90, Math.min(90, pitch));

        if (pitch >= -45 && pitch <= 45) {
            return 0f;
        } else if (pitch < -45) {
            return -90f;
        }
        return 90f;
    }

    public static BlockFace getClosestYawDirection(float yaw) {
        // Normalize yaw to [0, 360)
        yaw = (yaw + 360) % 360;

        if (yaw >= 315 || yaw < 45) {
            return BlockFace.NORTH;
        } else if (yaw >= 45 && yaw < 135) {
            return BlockFace.EAST;
        } else if (yaw >= 135 && yaw < 225) {
            return BlockFace.SOUTH;
        } else if (yaw >= 225 && yaw < 315) {
            return BlockFace.WEST;
        }
        throw new IllegalStateException("Invalid yaw! " + yaw);
    }

    public static BlockFace getClosestPitchDirection(float pitch) {
        // Normalize pitch to [-90, 90] range
        pitch = Math.max(-90, Math.min(90, pitch));

        if (pitch >= -45 && pitch <= 45) {
            return BlockFace.SELF;
        } else if (pitch < -45) {
            return BlockFace.UP;
        }
        return BlockFace.DOWN;
    }

    public static @NotNull String format(@NotNull Number number){
        return DECIMAL_FORMAT_1.format(number);
    }

    /**
     * @param dir The direction the object was moving in.
     * @param hit Must be a normalized!
     * @return A reflected vector.
     */
    public static @NotNull Vector reflect(@NotNull Vector dir, @NotNull Vector hit){
        double velocityDotProduct = hit.dot(dir);
        return new Vector(dir.getX() - 2 * velocityDotProduct * hit.getX(),
            dir.getY() - 2 * velocityDotProduct * hit.getY(),
            dir.getZ() - 2 * velocityDotProduct * hit.getZ());
    }

    /**
     * Gravity of a potion = .115
     */
    public static @NotNull Vector parabolicMotion(@NotNull Vector from, @NotNull Vector to, int heightGain, double gravity) {
        // Block locations
        int endGain = to.getBlockY() - from.getBlockY();
        double horizDist = Math.sqrt(from.distanceSquared(to));

        // Height gain
        double maxGain = Math.max(heightGain, (endGain + heightGain));

        // Solve quadratic equation for velocity
        double a = -horizDist * horizDist / (4 * maxGain);
        double c = -endGain;

        double slope = -horizDist / (2 * a) - Math.sqrt(horizDist * horizDist - 4 * a * c) / (2 * a);

        // Vertical velocity
        double vy = Math.sqrt(maxGain * gravity);

        // Horizontal velocity
        double vh = vy / slope;

        // Calculate horizontal direction
        int dx = to.getBlockX() - from.getBlockX();
        int dz = to.getBlockZ() - from.getBlockZ();
        double mag = Math.sqrt(dx * dx + dz * dz);
        double dirx = dx / mag;
        double dirz = dz / mag;

        // Horizontal velocity components
        double vx = vh * dirx;
        double vz = vh * dirz;

        return new Vector(vx, vy, vz);
    }

    public static boolean hasOccurredWithin(long value, int ticks){
        return (System.currentTimeMillis() - (50L * ticks)) <= value;
    }

    public static boolean hasOccurredWithin(long value, long ticks){
        return (System.currentTimeMillis() - (50L * ticks)) <= value;
    }

    public static boolean testChance(double chance){
        return testChance(RANDOM, chance);
    }

    public static int randomSigned(int min, int max){
        return randomSigned(min, max, RANDOM);
    }

    public static int randomSigned(int min, int max, @NotNull Random random){
        int x = random(min, max, random);
        if(random.nextBoolean()) x *= -1;
        return x;
    }

    public static float randomSigned(float min, float max){
        return randomSigned(min, max, RANDOM);
    }

    public static float randomSigned(float min, float max, @NotNull Random random){
        float x = random(min, max, random);
        if(random.nextBoolean()) x *= -1f;
        return x;
    }

    public static double randomSigned(double min, double max){
        return randomSigned(min, max, RANDOM);
    }

    public static double randomSigned(double min, double max, @NotNull Random random){
        double x = random(min, max, random);
        if(random.nextBoolean()) x *= -1D;
        return x;
    }

    /**
     * @param chance The probability from 0-100.
     */
    public static boolean testChance(@NotNull Random random, double chance) {
        return random.nextDouble() * 100 < chance;
    }

    @Deprecated(forRemoval = true, since = "See CruxCollection")
    public static <T> T getRandom(@NotNull List<T> list){
        if(list.isEmpty()) return null;
        return list.get(random(0, list.size()-1));
    }
    public static double round(double value, int place, @NotNull RoundingMode roundingMode){
        if(place < 0) return value;

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(place, roundingMode);
        return bd.doubleValue();
    }

    public static double round(double value, int place){
        return round(value, place, RoundingMode.HALF_UP);
    }

    public static double round(double value){
        return round(value, 1);
    }

    public static int wrap(int value, int min, int max){
        if(value < min) value = max;
        else if (value > max) value = min;
        return value;
    }

    public static float wrap(float value, float min, float max){
        if(value < min) value = max;
        else if (value > max) value = min;
        return value;
    }

    public static double wrap(double value, double min, double max){
        /*if(value < min) value = max;
        else if (value > max) value = min;
        return value;*/

        if (min >= max) {
            throw new IllegalArgumentException("Min must be less than Max");
        }

        double range = max - min;
        if (range <= 0) {
            throw new IllegalArgumentException("Range must be positive");
        }

        // Use modulo to wrap the value
        double wrappedValue = (value - min) % range;
        if (wrappedValue < 0) {
            wrappedValue += range;
        }

        return wrappedValue + min;
    }

    public static long wrap(long value, long min, long max){
        if(value < min) value = max;
        else if (value > max) value = min;
        return value;
    }

    public static double square(double x){
        return x * x;
    }

    public static float random(float min, float max) {
        return random(min, max, RANDOM);
    }

    public static double random(double min, double max) {
        return random(min, max, RANDOM);
    }

    public static int random(int min, int max) {
        return random(min, max, RANDOM);
    }

    public static float random(float min, float max, @NotNull Random random) {
        return random.nextInt((int) ((max - min) * 10000) + 1) * 0.0001f + min;
    }

    public static double random(double min, double max, @NotNull Random random) {
        return random.nextInt((int) ((max - min) * 10000) + 1) * 0.0001D + min;
    }

    public static int random(int min, int max, @NotNull Random random) {
        return random.nextInt((max - min) + 1) + min;
    }

    public static int clamp(int value, int min, int max){
        return Math.max(min, Math.min(max, value));
    }

    public static float clamp(float value, float min, float max){
        return Math.max(min, Math.min(max, value));
    }

    public static double clamp(double value, double min, double max){
        return Math.max(min, Math.min(max, value));
    }

    public static long clamp(long value, long min, long max){
        return Math.max(min, Math.min(max, value));
    }

    public static boolean testExpression(@Nullable String eq){
        return ((int) evaluate(eq)) == 1;
    }

    public static boolean testExpression(@Nullable String eq, @NotNull EvaluationEnvironment ev){
        return ((int) evaluate(eq, ev)) == 1;
    }

    public static double evaluate(@Nullable String eq){
        if(eq == null || eq.isBlank()) return 0D;
        return Crunch.compileExpression(eq).evaluate();
    }

    public static double evaluate(@Nullable String eq, @Nullable EvaluationEnvironment ev){
        if(eq == null || eq.isBlank()) return 0D;
        if(ev==null) return evaluate(eq);
        return Crunch.compileExpression(eq, ev).evaluate();
    }

    public static String tryEvaluateEvalEx(@Nullable String eq) throws EvaluationException, ParseException {
        if(eq==null || eq.isBlank()) return "false";
        return new Expression(eq).evaluate().getStringValue();
    }

    public static String evaluateEvalEx(@Nullable String eq){
        if(eq==null || eq.isBlank()) return "false";
        try{
            return new Expression(eq).evaluate().getStringValue();
        }catch (EvaluationException | ParseException e) {
            e.printStackTrace();
            return "false";
        }
    }

    public static int unnumeral(@NotNull String number) {
        if (number.startsWith("M")) return 1000 + unnumeral(number.replaceFirst("M", ""));
        if (number.startsWith("CM")) return 900 + unnumeral(number.replaceFirst("CM", ""));
        if (number.startsWith("D")) return 500 + unnumeral(number.replaceFirst("D", ""));
        if (number.startsWith("CD")) return 400 + unnumeral(number.replaceFirst("CD", ""));
        if (number.startsWith("C")) return 100 + unnumeral(number.replaceFirst("C", ""));
        if (number.startsWith("XC")) return 90 + unnumeral(number.replaceFirst("XC", ""));
        if (number.startsWith("L")) return 50 + unnumeral(number.replaceFirst("L", ""));
        if (number.startsWith("XL")) return 40 + unnumeral(number.replaceFirst("XL", ""));
        if (number.startsWith("X")) return 10 + unnumeral(number.replaceFirst("X", ""));
        if (number.startsWith("IX")) return 9 + unnumeral(number.replaceFirst("IX", ""));
        if (number.startsWith("V")) return 5 + unnumeral(number.replaceFirst("V", ""));
        if (number.startsWith("IV")) return 4 + unnumeral(number.replaceFirst("IV", ""));
        if (number.startsWith("I")) return 1 + unnumeral(number.replaceFirst("I", ""));
        return 0;
    }

    private static @NotNull String numeralConvert(int number){
        if(number<=0) return "";
        if(number>=6000) return "";

        if (number-1000>=0) return "M" + numeralConvert(number-1000);
        if (number-900>=0) return "CM" + numeralConvert(number-900);
        if (number-500>=0) return "D" + numeralConvert(number-500);
        if (number-400>=0) return "CD" + numeralConvert(number-400);
        if (number-100>=0) return "C" + numeralConvert(number-100);
        if (number-90>=0) return "XC" + numeralConvert(number-90);
        if (number-50>=0) return "L" + numeralConvert(number-50);
        if (number-40>=0) return "XL" + numeralConvert(number-40);
        if (number-10>=0) return "X" + numeralConvert(number-10);
        if (number-9>=0) return "IX" + numeralConvert(0);
        if (number-5>=0) return "V" + numeralConvert(number-5);
        if (number-4>=0) return "IV" + numeralConvert(0);
        return "I" + numeralConvert(number-1);
    }

    public static @NotNull String numeral(int number) {
        if(number<=0) return String.valueOf(number);
        if(number>=6000) return String.valueOf(number);
        return numeralConvert(number);
    }

    public static @NotNull String padWithZeroIfSingleDigit(int x){ return (x < 10 ? "0" : "") + new DecimalFormat().format(x); }

    public static float calculateYaw(double x, double z){
        if (x == 0 && z == 0) {
            return 0f;
        }

        final double _2PI = 2 * Math.PI;
        double theta = Math.atan2(-x, z);
        return (float) Math.toDegrees((theta + _2PI) % _2PI);
    }

    public static float calculatePitch(double x, double y, double z){
        if (x == 0 && z == 0) {
            return y > 0 ? -90 : 90;
        }

        double x2 = NumberConversions.square(x);
        double z2 = NumberConversions.square(z);
        double xz = Math.sqrt(x2 + z2);
        return (float) Math.toDegrees(Math.atan(-y / xz));
    }

    public static float calculatePitch(CruxPosition dir){
        return calculatePitch(dir.x(), dir.y(), dir.z());
    }

    public static float calculatePitch(Vector dir){
        return calculatePitch(dir.getX(), dir.getY(), dir.getZ());
    }

    public static float calculateYaw(CruxPosition dir){
        return calculateYaw(dir.x(), dir.z());
    }

    public static float calculateYaw(Vector dir){
        return calculateYaw(dir.getX(), dir.getZ());
    }

    public static double distanceSquared(
        Location loc, double x2, double y2, double z2
    ){
        return distanceSquared(loc.getX(), loc.getY(), loc.getZ(), x2, y2, z2);
    }
    public static double distanceSquared(
        Vector loc, double x2, double y2, double z2
    ){
        return distanceSquared(loc.getX(), loc.getY(), loc.getZ(), x2, y2, z2);
    }
    public static double distanceSquared(
        CruxPosition loc, double x2, double y2, double z2
    ){
        return distanceSquared(loc.x(), loc.y(), loc.z(), x2, y2, z2);
    }

    public static double distanceSquared(
        double x1, double y1, double z1,
        double x2, double y2, double z2
    ) {
        return square(x1 - x2) + square(y1 - y2) + square(z1 - z2);
    }
}
