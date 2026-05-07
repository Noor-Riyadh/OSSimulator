package memory;

import model.PageReplacementResult;
import model.PageReplacementResult.Step;

import java.util.*;

public class LRU {
    public static PageReplacementResult simulate(int[] referenceString, int numFrames) {
        
         PageReplacementResult result = new PageReplacementResult("LRU");
        int[] frames = new int[numFrames];
        //empty array all cells =-1 to know is empty 
        Arrays.fill(frames, -1);
  
        Map<Integer, Integer> lastUsed = new LinkedHashMap<>();
        for (int i = 0; i < referenceString.length; i++) {
 
            int page = referenceString[i]; 
            boolean isPageFault;
            int replacedPage = -1;
            // here meaning page in memory
            if (lastUsed.containsKey(page)) {
                isPageFault = false;
                lastUsed.put(page, i);
            } else {
                //page not in memory = fualt +1
                isPageFault = true;
                //add empty space 
                if (lastUsed.size() < numFrames) {
                    for (int f = 0; f < numFrames; f++) {
                        if (frames[f] == -1) {
                            frames[f] = page;
                            break;
                        }
                    }
                } else {
                    //
                    int lruPage = Collections.min(
                        lastUsed.entrySet(),
                        Map.Entry.comparingByValue()
                    ).getKey();
 
                    replacedPage = lruPage;
                    lastUsed.remove(lruPage);
                    //replace old page we use it  with new page 
                    for (int f = 0; f < numFrames; f++) {
                        if (frames[f] == lruPage) {
                            frames[f] = page;
                            break;
                        }
                    }
                }
                
               //make new page its last used time 
                lastUsed.put(page, i);
            }
   
            result.addStep(new Step(i + 1, page, Arrays.copyOf(frames, frames.length), isPageFault, replacedPage));
        }
 
    
        return result  ; 
    }
}
