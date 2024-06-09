package killercreepr.crux.util;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.function.Function;

public class CruxGoalUtil {
    public static @NotNull Location findRandomAwayLocation(@NotNull Location center, @NotNull Location current, double range, double maxAngleDegrees) {
        Random random = new Random();

        // Calculate direction vector from center to current location
        double dx = current.getX() - center.getX();
        double dy = current.getY() - center.getY();
        double dz = current.getZ() - center.getZ();

        // Normalize the direction vector
        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
        dx /= length;
        dy /= length;
        dz /= length;

        // Convert the maximum angle to radians
        double maxAngleRadians = Math.toRadians(maxAngleDegrees);

        // Generate random angle deviations within the range [-maxAngleRadians, maxAngleRadians]
        double angleDeviationX = (random.nextDouble() * 2 - 1) * maxAngleRadians;
        double angleDeviationY = (random.nextDouble() * 2 - 1) * maxAngleRadians;
        double angleDeviationZ = (random.nextDouble() * 2 - 1) * maxAngleRadians;

        // Apply the angle deviations
        dx += Math.tan(angleDeviationX);
        dy += Math.tan(angleDeviationY);
        dz += Math.tan(angleDeviationZ);

        // Normalize again after adding angle deviations
        length = Math.sqrt(dx * dx + dy * dy + dz * dz);
        dx /= length;
        dy /= length;
        dz /= length;

        // Scale by the desired range
        dx *= range;
        dy *= range;
        dz *= range;

        // Calculate the new location
        double newX = current.getX() + dx;
        double newY = current.getY() + dy;
        double newZ = current.getZ() + dz;

        return new Location(center.getWorld(), newX, newY, newZ);
    }
    public static @NotNull Location findRandomTowardsLocation(@NotNull Location center, @NotNull Location current, double range, double maxAngleDegrees) {
        Random random = new Random();

        // Calculate direction vector from current location to center
        double dx = center.getX() - current.getX();
        double dy = center.getY() - current.getY();
        double dz = center.getZ() - current.getZ();

        // Normalize the direction vector
        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
        dx /= length;
        dy /= length;
        dz /= length;

        // Convert the maximum angle to radians
        double maxAngleRadians = Math.toRadians(maxAngleDegrees);

        // If maxAngleDegrees is greater than 0, introduce deviation
        if (maxAngleDegrees > 0) {
            // Generate random angle deviations within the range [-maxAngleRadians, maxAngleRadians]
            double angleDeviationX = (random.nextDouble() * 2 - 1) * maxAngleRadians;
            double angleDeviationY = (random.nextDouble() * 2 - 1) * maxAngleRadians;
            double angleDeviationZ = (random.nextDouble() * 2 - 1) * maxAngleRadians;

            // Apply the angle deviations
            dx += Math.tan(angleDeviationX);
            dy += Math.tan(angleDeviationY);
            dz += Math.tan(angleDeviationZ);

            // Normalize again after adding angle deviations
            length = Math.sqrt(dx * dx + dy * dy + dz * dz);
            dx /= length;
            dy /= length;
            dz /= length;
        }

        // Scale by the desired range
        dx *= range;
        dy *= range;
        dz *= range;

        // Calculate the new location
        double newX = current.getX() + dx;
        double newY = current.getY() + dy;
        double newZ = current.getZ() + dz;

        return new Location(center.getWorld(), newX, newY, newZ);
    }

    public static <T extends Goal<Mob>> @NotNull T getOrAddGoal(@NotNull Mob mob, @NotNull Class<T> clazz, @NotNull GoalKey<Mob> key, int priority,
                                                                   @NotNull Function<Mob, Goal<Mob>> notFoundFunction){
        Goal<Mob> goal = Bukkit.getMobGoals().getGoal(mob, key);
        if(goal == null){
            goal = notFoundFunction.apply(mob);
            Bukkit.getMobGoals().addGoal(mob, priority, goal);
        }
        return clazz.cast(goal);
    }

    /**
     * @param clazz This class must have a public constructor that takes in a single mob argument.
     */
    public static <T extends Goal<Mob>> @NotNull T getOrAddGoal(@NotNull Mob mob, @NotNull Class<T> clazz, @NotNull GoalKey<Mob> key, int priority){
        return getOrAddGoal(mob, clazz, key, priority, (m) ->{
            try{
                return clazz.getConstructor(Mob.class).newInstance(mob);
            }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored){
                throw new UnsupportedOperationException(clazz.getSimpleName() + " does not have a constructor that takes in 1 single mob argument!");
            }
        });
    }
    /**
     * @param clazz This class must have a public constructor that takes in a single mob argument AND
     *              a public static GoalKey KEY variable.
     */
    public static <T extends Goal<Mob>> @NotNull T getOrAddGoal(@NotNull Mob mob, @NotNull Class<T> clazz, int priority){
        try{
            Field field = clazz.getDeclaredField("KEY");
            return getOrAddGoal(mob, clazz, (GoalKey<Mob>) field.get(null), priority);
        }catch (NoSuchFieldException | IllegalAccessException ignored){
            throw new UnsupportedOperationException(clazz.getSimpleName() + " does not have a static GoalKey<Mob> KEY variable!");
        }
    }
}
