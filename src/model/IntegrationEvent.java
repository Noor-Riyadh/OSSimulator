package model;

import java.util.List;

public class IntegrationEvent {
    public int stepNumber;
    public int faultedPage;
    public int replacedPage;        
    public int diskBlock;          
    public List<Integer> diskHeadPath; 
    public int seekDistance;
    public String pageAlgorithm;
    public String diskAlgorithm;

    public IntegrationEvent(int stepNumber, int faultedPage, int replacedPage,
                             int diskBlock, List<Integer> diskHeadPath,
                             String pageAlgorithm, String diskAlgorithm) {
        this.stepNumber    = stepNumber;
        this.faultedPage   = faultedPage;
        this.replacedPage  = replacedPage;
        this.diskBlock     = diskBlock;
        this.diskHeadPath  = diskHeadPath;
        this.pageAlgorithm = pageAlgorithm;
        this.diskAlgorithm = diskAlgorithm;

        seekDistance = 0;
        for (int i = 1; i < diskHeadPath.size(); i++) {
            seekDistance += Math.abs(diskHeadPath.get(i) - diskHeadPath.get(i - 1));
        }
    }

    public String getSummary() {
        String eviction = (replacedPage == -1)
            ? "no eviction (free frame used)"
            : "replacing page " + replacedPage + " using " + pageAlgorithm;
        return String.format("Page fault at page %d → %s → disk request for block %d → %s services it (seek: %d)",
            faultedPage, eviction, diskBlock, diskAlgorithm, seekDistance);
    }
}
