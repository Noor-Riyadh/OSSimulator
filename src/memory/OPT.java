package memory;

import model.PageReplacementResult;
import model.PageReplacementResult.Step;
import java.util.*;

public class OPT {

    public static PageReplacementResult simulate(int[] referenceString, int numFrames) {
        PageReplacementResult result = new PageReplacementResult("OPT");
        int[] frames = new int[numFrames];
        Arrays.fill(frames, -1);
        Set<Integer> inMemory = new HashSet<>();

        for (int i = 0; i < referenceString.length; i++) {
            int page = referenceString[i];
            boolean isPageFault;
            int replacedPage = -1;

            if (inMemory.contains(page)) {
                isPageFault = false;
            } else {
                isPageFault = true;
                if (inMemory.size() < numFrames) {
                    for (int f = 0; f < numFrames; f++) {
                        if (frames[f] == -1) {
                            frames[f] = page;
                            break;
                        }
                    }
                    inMemory.add(page);
                } else {
                    int victimIndex = findOptimalVictim(frames, referenceString, i + 1);
                    replacedPage = frames[victimIndex];
                    inMemory.remove(replacedPage);
                    frames[victimIndex] = page;
                    inMemory.add(page);
                }
            }
            result.addStep(new Step(i + 1, page, frames.clone(), isPageFault, replacedPage));
        }
        return result;
    }

    private static int findOptimalVictim(int[] frames, int[] referenceString, int fromIndex) {
        int victimIndex = 0;
        int farthestUse = -1;

        for (int f = 0; f < frames.length; f++) {
            int nextUse = Integer.MAX_VALUE;
            for (int j = fromIndex; j < referenceString.length; j++) {
                if (referenceString[j] == frames[f]) {
                    nextUse = j;
                    break;
                }
            }
            if (nextUse > farthestUse) {
                farthestUse = nextUse;
                victimIndex = f;
            }
        }
        return victimIndex;
    }
}
