package memory;

import model.PageReplacementResult;
import model.PageReplacementResult.Step;
import java.util.*;

public class SecondChance {

    public static PageReplacementResult simulate(int[] referenceString, int numFrames) {
        PageReplacementResult result = new PageReplacementResult("Second Chance");
        int[] frames = new int[numFrames];
        boolean[] bits = new boolean[numFrames];
        Arrays.fill(frames, -1);
        int clockHand = 0;
        Set<Integer> inMemory = new HashSet<>();

        for (int i = 0; i < referenceString.length; i++) {
            int page = referenceString[i];
            boolean isPageFault;
            int replacedPage = -1;

            int frameIndex = -1;
            for (int f = 0; f < numFrames; f++) {
                if (frames[f] == page) {
                    frameIndex = f;
                    break;
                }
            }

            if (frameIndex != -1) {
                isPageFault = false;
                bits[frameIndex] = true;
            } else {
                isPageFault = true;
                if (inMemory.size() < numFrames) {
                    for (int f = 0; f < numFrames; f++) {
                        if (frames[f] == -1) {
                            frames[f] = page;
                            bits[f] = true;
                            inMemory.add(page);
                            break;
                        }
                    }
                } else {
                    while (bits[clockHand]) {
                        bits[clockHand] = false;
                        clockHand = (clockHand + 1) % numFrames;
                    }
                    replacedPage = frames[clockHand];
                    inMemory.remove(replacedPage);
                    frames[clockHand] = page;
                    bits[clockHand] = true;
                    inMemory.add(page);
                    clockHand = (clockHand + 1) % numFrames;
                }
            }
            result.addStep(new Step(i + 1, page, frames.clone(), isPageFault, replacedPage));
        }
        return result;
    }
}
