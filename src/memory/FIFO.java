package memory;

import model.PageReplacementResult;
import model.PageReplacementResult.Step;

import java.util.*;

public class FIFO {

    public static PageReplacementResult simulate(int[] referenceString, int numFrames) {
            PageReplacementResult result = new PageReplacementResult("FIFO");
 
  
        int[] frames = new int[numFrames];
        Arrays.fill(frames, -1);
  
        Queue<Integer> queue = new LinkedList<>();
  
        Set<Integer> inMemory = new HashSet<>();
 
    
        for (int i = 0; i < referenceString.length; i++) {
 
            int page = referenceString[i]; 
            boolean isPageFault;           
            int replacedPage = -1;         
            if (inMemory.contains(page)) {
                
                isPageFault = false;
            } else {
                //here page fualt increment +1
                isPageFault = true;

                if (queue.size() < numFrames) {
                    for (int f = 0; f < numFrames; f++) {
                        if (frames[f] == -1) {
                            frames[f] = page;
                            break; 
                        }
                    }
 
            
                } else {
                    replacedPage = queue.poll();
                    inMemory.remove(replacedPage);
                    for (int f = 0; f < numFrames; f++) {
                        if (frames[f] == replacedPage) {
                            frames[f] = page;
                            break;
                        }
                    }
                }
                queue.add(page);
                inMemory.add(page);
            }
            result.addStep(new Step(i + 1, page, Arrays.copyOf(frames, frames.length), isPageFault, replacedPage));
        }
        return result;
    }
}
