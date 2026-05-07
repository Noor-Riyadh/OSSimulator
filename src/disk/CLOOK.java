package disk;

import model.DiskSchedulingResult;
import java.util.*;

public class CLOOK {

    public static DiskSchedulingResult simulate(int initialHead, int[] requests) {
        // Like CSCAN but jumps to the SMALLEST request (not cylinder 0)
        // Steps:
        // 1. Split into left and right, sort both
        // 2. Service right ascending → then jump to start of left → service left ascending
        // Key difference from CSCAN: NO diskSize-1 or 0 added
        return null; 
    }
}
