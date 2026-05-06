package disk;

import model.DiskSchedulingResult;

import java.util.*;

public class SCAN {

    public static DiskSchedulingResult simulate(int initialHead, int[] requests, int diskSize, String direction) {
        // Steps:
        // 1. Split requests into two lists: left (< initialHead) and right (>= initialHead)
        // 2. Sort both lists
        // 3. If direction is "right":
        //    - Service right list ascending → go to diskSize-1 → service left list descending
        // 4. If direction is "left":
        //    - Service left list descending → go to 0 → service right list ascending
        // 5. Build the order list and call result.setHeadMovementOrder(order)
        return null; // replace this
    }
}
