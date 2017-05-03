package com.example.n50.speedyaladdin;

/**
 * Created by 12124 on 5/3/2017.
 */

public final class Constant {
    public static final int DISTANCE_BOTTOM_TOP_OBSTACLE = 500;
    public static final int VISIBLE_HEIGHT_MAX = 10;
    public static final int VISIBLE_HEIGHT_MIN = 4;

    public static final float ALADDIN_VELOCITY = 0.4f;
    public static final float OBSTACLE_VELOCITY = 0.3f;


    public enum ObstacleType {
        TOWER(1), WAND(2);

        private final int value;
        private ObstacleType(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
}
