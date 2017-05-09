package com.example.n50.speedyaladdin;

/**
 * Created by 12124 on 5/3/2017.
 */

public final class Constant {
    public static final int DISTANCE_BOTTOM_TOP_OBSTACLE = 600;
    public static final int VISIBLE_HEIGHT_MAX = 8;
    public static final int VISIBLE_HEIGHT_MIN = 3;

    public static final float ALADDIN_VELOCITY = 0.4f;
    public static final float OBSTACLE_VELOCITY = 0.3f;

    public static final String MY_PREFS = "my_prefs";
    public static final String PREF_BEST_SCORE = "pref_best_score";


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
