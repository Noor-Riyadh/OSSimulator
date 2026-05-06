package memory;

import model.PageReplacementResult;
import model.PageReplacementResult.Step;

import java.util.*;

public class LRU {
    public static PageReplacementResult simulate(int[] referenceString, int numFrames) {
        
         PageReplacementResult result = new PageReplacementResult("LRU");
        int[] frames = new int[numFrames];
        Arrays.fill(frames, -1);
  
        Map<Integer, Integer> lastUsed = new LinkedHashMap<>();
        for (int i = 0; i < referenceString.length; i++) {
 
            int page = referenceString[i]; 
            boolean isPageFault;
            int replacedPage = -1;
            if (lastUsed.containsKey(page)) {
                isPageFault = false;
                lastUsed.put(page, i);
            } else {
                isPageFault = true;
                if (lastUsed.size() < numFrames) {
                    for (int f = 0; f < numFrames; f++) {
                        if (frames[f] == -1) {
                            frames[f] = page;
                            break;
                        }
                    }
                } else {
                    int lruPage = Collections.min(
                        lastUsed.entrySet(),
                        Map.Entry.comparingByValue()
                    ).getKey();
 
                    replacedPage = lruPage;
                    lastUsed.remove(lruPage);
                    for (int f = 0; f < numFrames; f++) {
                        if (frames[f] == lruPage) {
                            frames[f] = page;
                            break;
                        }
                    }
                }
  
                lastUsed.put(page, i);
            }
  
            result.addStep(new Step(i + 1, page, Arrays.copyOf(frames, frames.length), isPageFault, replacedPage));
        }
 
    
        return result  ; 
    }
}
