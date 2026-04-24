package disk;

import model.DiskSchedulingResult;
import java.util.*;

public class CLOOK {

    public static DiskSchedulingResult simulate(int initialHead, int[] requests) {
        DiskSchedulingResult result = new DiskSchedulingResult("CLOOK");

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

        // Go right to last request, then jump to smallest request on left
        for (int req : right) {
            order.add(req);
        }
        for (int req : left) {
            order.add(req);
        }

        result.setHeadMovementOrder(order);
        return result;
    }
}
