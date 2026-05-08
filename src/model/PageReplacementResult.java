package model;

import java.util.List;
import java.util.ArrayList;

public class PageReplacementResult {

    public static class Step {

        public int stepNumber;
        public int page;
        public int[] frames;       
        public boolean isPageFault;
        public int replacedPage;   

        public Step(int stepNumber, int page, int[] frames, boolean isPageFault, int replacedPage) {
            this.stepNumber = stepNumber;
            this.page = page;
            this.frames = frames.clone();
            this.isPageFault = isPageFault;
            this.replacedPage = replacedPage;
        }
    }

    private List<Step> steps = new ArrayList<>();
    private int totalPageFaults = 0;
    private int totalHits = 0;
    private String algorithmName;

    public PageReplacementResult(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void addStep(Step step) {
        steps.add(step);
        if (step.isPageFault) {
            totalPageFaults++;
        } else {
            totalHits++;
        }
    }

    public List<Step> getSteps() {
        return steps;
    }

    public int getTotalPageFaults() {
        return totalPageFaults;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public double getPageFaultRate() {
        int total = totalPageFaults + totalHits;
        return total == 0 ? 0 : (double) totalPageFaults / total * 100.0;
    }
}
