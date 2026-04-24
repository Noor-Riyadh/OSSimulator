package disk;

import model.DiskSchedulingResult;
import java.util.*;

public class CSCAN {

    public static DiskSchedulingResult simulate(int initialHead, int[] requests, int diskSize) {
        DiskSchedulingResult result = new DiskSchedulingResult("CSCAN");

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

        // Go right servicing all requests, reach end, jump to 0, service left side
        for (int req : right) {
            order.add(req);
        }
        order.add(diskSize - 1);  // reach right end of disk
        order.add(0);             // jump to beginning (no servicing on the way back)
        for (int req : left) {
            order.add(req);
        }

        result.setHeadMovementOrder(order);
        return result;
    }
}
