package Program.Simulation.WSSSim;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class History {
    private int maxSize;
    private Queue<Integer> history = new LinkedList<>();

    public History(int setSize) {
        maxSize = setSize;
    }

    public void Record(int page) {
        history.add(page);
        if (history.size() > maxSize)
            history.remove();
    }

    public int GetWSS() {
        HashSet<Integer> set = new HashSet<>(maxSize);
        for (Integer page : history) {
            set.add(page);
        }
        return set.size();
    }

}
