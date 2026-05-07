package disk;

import model.DiskSchedulingResult;
import java.util.*;

public class LOOK {

    public static DiskSchedulingResult simulate(int initialHead, int[] requests, String direction) {
      
        DiskSchedulingResult result = new DiskSchedulingResult("LOOK");
 
        List<Integer> left = new ArrayList<>();
  
        List<Integer> right = new ArrayList<>();
 
        for (int req : requests) {
            if (req < initialHead) {
                left.add(req);    
            } else {
                right.add(req);  
            }
        }
         //make requests in order 
        Collections.sort(left);
 
        Collections.sort(right);
 
        List<Integer> order = new ArrayList<>();
        order.add(initialHead);
 
        if (direction.equalsIgnoreCase("left")) {
 
            for (int i = left.size() - 1; i >= 0; i--) {
                order.add(left.get(i));
            }
      
            for (int req : right) {
                order.add(req);
            }
 
        } else {
    
            for (int req : right) {
                order.add(req);
            }
         
            for (int i = left.size() - 1; i >= 0; i--) {
                order.add(left.get(i));
            }
        }
        //calculate total-distance
          int totalSeek = 0;
        int current = initialHead;
        for (int pos : order) {
            //make distance positive 
            totalSeek += Math.abs(pos - current);
            current = pos;
        }

        
        result.setHeadMovementOrder(order);
        result.setTotalSeekDistance(totalSeek);
        
        return result; 
    }
}
