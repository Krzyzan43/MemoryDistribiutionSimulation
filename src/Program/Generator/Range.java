package Program.Generator;

import java.util.Random;

// Helper class for generator
// if max == 0 it'll always output min value
public class Range {
    int min, max;

    public Range(int min, int max) {
        if (max == 0) {
            this.max = min + 1;
            this.min = min;
        } else {
            this.min = min;
            this.max = max;
        }
    }

    public int random(Random random) {
        return random.nextInt(min, max);
    }
}
