package disk;

import model.DiskSchedulingResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CLOOK {

    public static DiskSchedulingResult simulate(int initialHead, int[] requests) {

        DiskSchedulingResult result = new DiskSchedulingResult("CLOOK");

        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        List<Integer> movement = new ArrayList<>();

        for (int i = 0; i < requests.length; i++) {

            if (requests[i] < initialHead) {
                left.add(requests[i]);
            } else {
                right.add(requests[i]);
            }
        }

        Collections.sort(left);
        Collections.sort(right);

        movement.add(initialHead);

        for (int i = 0; i < right.size(); i++) {
            movement.add(right.get(i));
        }

        for (int i = 0; i < left.size(); i++) {
            movement.add(left.get(i));
        }

        result.setHeadMovementOrder(movement);

        return result;
    }

    public static void main(String[] args) {

        int[] requests = {98, 183, 37, 122, 14, 124, 65, 67};

        DiskSchedulingResult result = simulate(53, requests);

        System.out.println("CLOOK");
        System.out.println("Movement: " + result.getMovementString());
        System.out.println("Total Seek Distance: " + result.getTotalSeekDistance());
    }
}