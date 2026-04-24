package integration;

import model.IntegrationEvent;
import model.PageReplacementResult;
import model.PageReplacementResult.Step;
import model.DiskSchedulingResult;

import disk.FCFS;
import disk.SCAN;

import java.util.*;

public class IntegrationEngine {

    private static int pageToBlock(int page) {
        return page * 10 + 50;
    }

    public static List<IntegrationEvent> simulate(PageReplacementResult pageResult,
                                                   int initialHead,
                                                   int diskSize,
                                                   String diskAlgorithm,
                                                   String scanDirection) {
        List<IntegrationEvent> events = new ArrayList<>();
        int currentHead = initialHead;

        for (Step step : pageResult.getSteps()) {
            if (!step.isPageFault) continue;  

            int diskBlock  = pageToBlock(step.page);
            int[] requests = new int[]{ diskBlock };

            DiskSchedulingResult diskResult;
            switch (diskAlgorithm.toUpperCase()) {
                case "SCAN":
                    diskResult = SCAN.simulate(currentHead, requests, diskSize, scanDirection);
                    break;
                case "FCFS":
                default:
                    diskResult = FCFS.simulate(currentHead, requests);
                    break;
            }

            List<Integer> path = diskResult.getHeadMovementOrder();

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

            currentHead = path.get(path.size() - 1);
        }

        return events;
    }
}
