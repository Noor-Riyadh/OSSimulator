package disk;

import model.DiskSchedulingResult;
import java.util.*;

public class CSCAN {

    public static DiskSchedulingResult simulate(int initialHead, int[] requests, int diskSize) {
        // Like SCAN but only goes in ONE direction (right)
        // When reaching the end → jump to 0 → continue servicing
        // Steps:
        // 1. Split into left and right lists, sort both
        // 2. Service right ascending → add diskSize-1 → add 0 → service left ascending
        return null; // replace this
    }
}
