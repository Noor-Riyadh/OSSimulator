package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Holds the complete result of a disk scheduling simulation.
 * Shared data model used by all disk scheduling algorithms.
 */
public class DiskSchedulingResult {

    private List<Integer> headMovementOrder = new ArrayList<>();
    private int totalSeekDistance = 0;
    private String algorithmName;

    public DiskSchedulingResult(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setHeadMovementOrder(List<Integer> order) {
        this.headMovementOrder = order;
        // calculate total seek distance from the movement order
        totalSeekDistance = 0;
        for (int i = 1; i < order.size(); i++) {
            totalSeekDistance += Math.abs(order.get(i) - order.get(i - 1));
        }
    }

    public List<Integer> getHeadMovementOrder() { return headMovementOrder; }
    public int getTotalSeekDistance()            { return totalSeekDistance; }
    public String getAlgorithmName()             { return algorithmName; }

    public double getAverageSeekTime() {
        int requests = headMovementOrder.size() - 1; // exclude starting position
        return requests <= 0 ? 0 : (double) totalSeekDistance / requests;
    }

    /** Returns a formatted string like "53 → 65 → 98 → ..." */
    public String getMovementString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < headMovementOrder.size(); i++) {
            sb.append(headMovementOrder.get(i));
            if (i < headMovementOrder.size() - 1) sb.append(" → ");
        }
        return sb.toString();
    }

    public void addStep(Integer get) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setTotalSeekDistance(int totalSeek) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
