package disk;

import model.DiskSchedulingResult;

import java.util.*;

public class FCFS {

    public static DiskSchedulingResult simulate(int initialHead, int[] requests) {
        DiskSchedulingResult result = new DiskSchedulingResult("FCFS");

        List<Integer> order = new ArrayList<>();
        order.add(initialHead);
        for (int req : requests) order.add(req);

        result.setHeadMovementOrder(order);
        return result;
    }
}
