package memory;

import model.PageReplacementResult;
import model.PageReplacementResult.Step;

import java.util.*;

public class OPT {

    public static PageReplacementResult simulate(int[] referenceString, int numFrames) {
        // Steps:
        // 1. Maintain a boolean[] referenceBits array (one per frame)
        // 2. Maintain a clockHand integer (starts at 0, wraps around)
        // 3. For each page:
        //    - If page in memory → hit, set its reference bit to true
        //    - If not → fault
        //      * If free frame → load directly, set bit to true
        //      * If full → advance clockHand:
        //           while referenceBits[clockHand] == true → set it false, advance
        //           when referenceBits[clockHand] == false → evict that frame
        return null; 
    }
}
