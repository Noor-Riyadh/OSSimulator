package model;

import java.util.List;
import java.util.ArrayList;

public class DiskSchedulingResult {

    private List<Integer> headMovementOrder = new ArrayList<>();
    private int totalSeekDistance = 0;
    private String algorithmName;

    public DiskSchedulingResult(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setHeadMovementOrder(List<Integer> order) {
        this.headMovementOrder = order;
        totalSeekDistance = 0;
        for (int i = 1; i < order.size(); i++) {
            totalSeekDistance += Math.abs(order.get(i) - order.get(i - 1));
        }
    }

    public List<Integer> getHeadMovementOrder() {
        return headMovementOrder;
    }

    public int getTotalSeekDistance() {
        return totalSeekDistance;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public double getAverageSeekTime() {
        int requests = headMovementOrder.size() - 1;
        return requests <= 0 ? 0 : (double) totalSeekDistance / requests;
    }

    public String getMovementString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < headMovementOrder.size(); i++) {
            sb.append(headMovementOrder.get(i));
            if (i < headMovementOrder.size() - 1) {
                sb.append(" \u2192 ");
            }
        }
        return sb.toString();
    }
}
