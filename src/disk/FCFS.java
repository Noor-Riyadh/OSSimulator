package disk;

import model.DiskSchedulingResult;
import java.util.ArrayList;
import java.util.List;

public class FCFS {

    public static DiskSchedulingResult simulate(int initialHead, int[] requests) {

        DiskSchedulingResult result = new DiskSchedulingResult("FCFS");

        List<Integer> movement = new ArrayList<>();

        movement.add(initialHead);

        for (int i = 0; i < requests.length; i++) {
            movement.add(requests[i]);
        }

        result.setHeadMovementOrder(movement);

        return result;
    }

    public static void main(String[] args) {

        int[] requests = {98, 183, 37, 122, 14, 124, 65, 67};

        DiskSchedulingResult result = simulate(53, requests);

        System.out.println("FCFS");
        System.out.println("Movement: " + result.getMovementString());
        System.out.println("Total Seek Distance: " + result.getTotalSeekDistance());
    }
}