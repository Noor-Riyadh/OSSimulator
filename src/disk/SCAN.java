package disk;

import model.DiskSchedulingResult;
import java.util.*;

public class SCAN {

    public static DiskSchedulingResult simulate(int initialHead, int[] requests,
            int diskSize, String direction) {
        DiskSchedulingResult result = new DiskSchedulingResult("SCAN");

        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();

        for (int req : requests) {
            if (req < initialHead) {
                left.add(req);
            } else {
                right.add(req);
            }
        }

        Collections.sort(left);
        Collections.sort(right);

        List<Integer> order = new ArrayList<>();
        order.add(initialHead);

        if (direction.equalsIgnoreCase("right")) {
            for (int req : right) {
                order.add(req);
            }
            order.add(diskSize - 1);
            for (int i = left.size() - 1; i >= 0; i--) {
                order.add(left.get(i));
            }
        } else {
            for (int i = left.size() - 1; i >= 0; i--) {
                order.add(left.get(i));
            }
            order.add(0);
            for (int req : right) {
                order.add(req);
            }
        }
        result.setHeadMovementOrder(order);
        return result;
    }
}
