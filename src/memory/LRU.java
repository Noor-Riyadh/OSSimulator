package memory;

import model.PageReplacementResult;
import model.PageReplacementResult.Step;

import java.util.*;

public class LRU {
    public static PageReplacementResult simulate(int[] referenceString, int numFrames) {
        // Steps:
        // 1. Use a Map<Integer, Integer> to track page → last used time (index)
        // 2. For each page:
        //    - If page in map → hit, update its last used time
        //    - If not in map → fault
        //      * If free frame → load directly
        //      * If full → evict the page with the MINIMUM last used time
        // 3. Record each step using result.addStep(new Step(...))
        return null; // replace this
    }
}
