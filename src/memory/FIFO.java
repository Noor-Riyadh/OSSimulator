package memory;

import model.PageReplacementResult;
import model.PageReplacementResult.Step;

import java.util.*;

public class FIFO {

    public static PageReplacementResult simulate(int[] referenceString, int numFrames) {
        // Steps:
        // 1. Use a Queue to track insertion order
        // 2. Use a Set for fast lookup of pages in memory
        // 3. For each page in referenceString:
        //    - If page is in memory → hit (no fault)
        //    - If page not in memory → fault
        //      * If free frame exists → load directly
        //      * If memory full → evict front of queue, load new page
        // 4. Record each step using result.addStep(new Step(...))
        return null; // replace this
    }
}
