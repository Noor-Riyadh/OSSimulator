package memory;

import model.PageReplacementResult;
import model.PageReplacementResult.Step;

import java.util.*;

public class SecondChance {

    public static PageReplacementResult simulate(int[] referenceString, int numFrames) {
        PageReplacementResult result = new PageReplacementResult("Second Chance");

        int[] frames   = new int[numFrames];
        boolean[] bits = new boolean[numFrames]; // reference bits
        Arrays.fill(frames, -1);

        int clockHand = 0;          // pointer into frames array
        Set<Integer> inMemory = new HashSet<>();

        for (int i = 0; i < referenceString.length; i++) {
            int page = referenceString[i];
            boolean isPageFault;
            int replacedPage = -1;

            int frameIndex = findPage(frames, page);

            if (frameIndex != -1) {
                // PAGE HIT — set reference bit
                isPageFault = false;
                bits[frameIndex] = true;
            } else {
                // PAGE FAULT
                isPageFault = true;

                if (inMemory.size() < numFrames) {
                    // Free frame — load directly
                    for (int f = 0; f < numFrames; f++) {
                        if (frames[f] == -1) {
                            frames[f] = page;
                            bits[f]   = true;
                            inMemory.add(page);
                            break;
                        }
                    }
                } else {
                    // Clock algorithm — find a victim
                    while (bits[clockHand]) {
                        bits[clockHand] = false;    // give second chance
                        clockHand = (clockHand + 1) % numFrames;
                    }
                    // clockHand now points to the victim (bit = 0)
                    replacedPage = frames[clockHand];
                    inMemory.remove(replacedPage);
                    frames[clockHand] = page;
                    bits[clockHand]   = true;
                    inMemory.add(page);
                    clockHand = (clockHand + 1) % numFrames;
                }
            }

            result.addStep(new Step(i + 1, page, frames, isPageFault, replacedPage));
        }

        return result;
    }

    private static int findPage(int[] frames, int page) {
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] == page) return i;
        }
        return -1;
    }
}
