package Program.Simulation.PFFSimulation;

import java.util.LinkedList;
import java.util.Queue;

public class ProcessHistory {
    private Queue<Boolean> history;
    private int maxSize;

    public double faultPercentage = 0;

    public ProcessHistory(int size) {
        history = new LinkedList<>();
        maxSize = size;
    }

    public void Record(boolean b) {
        history.add(b);
        if (history.size() > maxSize)
            history.remove();

        int faults = 0;
        for (Boolean fault : history) {
            if (fault)
                faults++;
        }

        faultPercentage = 1.0 * faults / history.size();
    }

}
