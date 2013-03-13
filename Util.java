package hobogame;

import java.util.List;
import java.util.Random;

/**
 * @author Gershom
 */
public class Util {

    public static void main(String[] args) {
        for (float i = -HALF_RADIAN; i < HALF_RADIAN; i += RADIAN * 0.01f) {
            Coord c = Coord.vector(i, 5);
            float ang = (float) Math.round(c.angle(Coord.ORIGIN) * 100) / 100;
            //System.out.println(ang);
            System.out.println(angleDifferenceRad(angleRadian(i), 0));
        }
    }

    public static final float RADIAN = (float) (Math.PI * 2);
    public static final float HALF_RADIAN = (float) (Math.PI);
    public static final float QUAR_RADIAN = (float) (Math.PI / 2);

    public static final Random rand = new Random();

    public static float cos(float n) {
        return (float) Math.cos(n);
    }

    public static float sin(float n) {
        return (float) Math.sin(n);
    }

    public static float angleStandard(float radians) {
        return radians > HALF_RADIAN ? radians - RADIAN : radians;
    }

    public static float angleRadian(float standard) {
        return standard < 0 ? RADIAN + standard : standard;
    }

    public static float angleSum(float standAng, float amount) {
        return angleStandard((angleRadian(standAng) + amount) % RADIAN);
    }

    public static float angleSumRadRad(float rad1, float rad2) {
        return angleStandard(propRadAngle(angleRadian(rad1) + angleRadian(rad2)));
    }

    public static float angleSumRad(float ang1, float ang2) {
        return propRadAngle(ang1 + ang2);
    }

    public static float propRadAngle(float f) {
        while (f < 0) {
            f += RADIAN;
        }
        while (f > RADIAN) {
            f -= RADIAN;
        }
        return f;
    }

    public static float angleDifferenceRad(float ang1, float ang2) {
        return propRadAngle(propRadAngle(ang1) - propRadAngle(ang2));
    }

    public static float angleDifference(float ang1, float ang2) {
        return angleStandard(angleDifferenceRad(ang1, ang2));
    }

    public static float randFloat(float mult) {
        return rand.nextFloat() * mult;
    }

    public static float cenRandFloat(float mult) {
        return ((rand.nextFloat() * 2) - 1) * mult;
    }

    public static int randInt(int min, int max) {
        return min + rand.nextInt(max - min);
    }

    public static <E> E randInList(List<E> list) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(rand.nextInt(list.size()));
    }
}
