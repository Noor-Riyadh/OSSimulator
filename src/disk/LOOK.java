package disk;

import model.DiskSchedulingResult;
import java.util.*;

public class LOOK {

    public static DiskSchedulingResult simulate(int initialHead, int[] requests, String direction) {
        // Like SCAN but does NOT go to disk end — only goes as far as the last request
        // Steps:
        // 1. Split into left and right, sort both
        // 2. If direction "right": service right ascending, then left descending
        // 3. If direction "left": service left descending, then right ascending
        // Key difference from SCAN: NO diskSize-1 or 0 added to order
        return null; // replace this
    }
}
