package disk;

import model.DiskSchedulingResult;
import java.util.*;

public class SCAN {

    public static DiskSchedulingResult simulate(int[] requests, int head, int diskSize, String direction) {
        DiskSchedulingResult result = new DiskSchedulingResult("SCAN");

        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        List<Integer> finalOrder = new ArrayList<>();

        for (int r : requests) {
            if (r < head) {
                left.add(r);
            } else if (r > head) {
                right.add(r);
            }
        }
        Collections.sort(left);
        Collections.sort(right);

        finalOrder.add(head);

        if ("left".equalsIgnoreCase(direction)) {
            for (int i = left.size() - 1; i >= 0; i--) {
                finalOrder.add(left.get(i));
            }
            finalOrder.add(0);
            for (int r : right) {
                finalOrder.add(r);
            }
        } else {
            for (int r : right) {
                finalOrder.add(r);
            }
            finalOrder.add(diskSize - 1);
            for (int i = left.size() - 1; i >= 0; i--) {
                finalOrder.add(left.get(i));
            }
        }

        return result;
    }
}
