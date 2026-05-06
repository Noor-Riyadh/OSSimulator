package disk;

import model.DiskSchedulingResult;
import java.util.*;

public class SCAN {
    public static DiskSchedulingResult simulate(int[] requests, int head, int diskSize, String direction) {
        DiskSchedulingResult result = new DiskSchedulingResult("SCAN");
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        if (direction.equalsIgnoreCase("right")) {
            right.add(diskSize - 1);
        } else {
            left.add(0);
        }

        for (int r : requests) {
            if (r < head) left.add(r);
            else if (r > head) right.add(r);
        }

        Collections.sort(left);
        Collections.sort(right);

        int run = 2;
        while (run-- > 0) {
            if (direction.equalsIgnoreCase("right")) {
                for (int r : right) {
                    result.addStep(r);
                }
                direction = "left";
            } else if (direction.equalsIgnoreCase("left")) {
                for (int i = left.size() - 1; i >= 0; i--) {
                    result.addStep(left.get(i));
                }
                direction = "right";
            }
        }
        return result;
    }
}