package integration;

import model.IntegrationEvent;
import model.PageReplacementResult;
import model.PageReplacementResult.Step;
import model.DiskSchedulingResult;

import disk.FCFS;
import disk.SCAN;
import disk.CSCAN;
import disk.LOOK;
import disk.CLOOK;

import java.util.*;

public class IntegrationEngine {

    // Maps page number to its disk block location
    private static int pageToBlock(int page) {
        return page * 10 + 50;
    }

    public static List<IntegrationEvent> simulate(
            PageReplacementResult pageResult,
            int initialHead,
            int diskSize,
            String diskAlgorithm,
            String scanDirection) {

        // Step 1 — result list + track head
        List<IntegrationEvent> events = new ArrayList<>();
        int currentHead = initialHead;

        // Step 2 — loop through every page replacement step
        for (Step step : pageResult.getSteps()) {

            // Step 3 — skip hits
            if (!step.isPageFault) {
                continue;
            }

            // Step 4 — calculate disk block
            int diskBlock = pageToBlock(step.page);
            int[] requests = new int[]{diskBlock};

            // Step 5 — send to disk algorithm
            // Step 5 — send to disk algorithm
            DiskSchedulingResult diskResult;
            String algo = diskAlgorithm.toUpperCase().trim();

            if (algo.equals("SCAN")) {
                diskResult = SCAN.simulate(currentHead, requests, diskSize, scanDirection);
            } else if (algo.equals("CSCAN")) {
                diskResult = CSCAN.simulate(currentHead, requests, diskSize);
            } else if (algo.equals("LOOK")) {
                diskResult = LOOK.simulate(currentHead, requests, scanDirection);
            } else if (algo.equals("CLOOK")) {
                diskResult = CLOOK.simulate(currentHead, requests);
            } else {
                diskResult = FCFS.simulate(currentHead, requests);
            }
            if (diskResult == null) {
                diskResult = FCFS.simulate(currentHead, requests);
            }

            // Step 6 — get head path
            List<Integer> path = diskResult.getHeadMovementOrder();

            // Step 7 — create integration event
            IntegrationEvent event = new IntegrationEvent(
                    step.stepNumber,
                    step.page,
                    step.replacedPage,
                    diskBlock,
                    path,
                    pageResult.getAlgorithmName(),
                    diskAlgorithm
            );
            events.add(event);

            // Step 8 — update head position
            currentHead = path.get(path.size() - 1);
        }

        // Step 9 — return all events
        return events;
    }
}
