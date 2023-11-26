package Program.Memory;

import java.util.LinkedList;
import java.util.Queue;

// Each process gets one
// Uses LRU algorithm to remove pages
// Retrieve method as well as any size modification can remove pages
// Retrieve throws exception if memorysize = 0
public class ProcessMemory {
    private int maxSize = 0;
    private Queue<Integer> memory = new LinkedList<>();

    public ProcessMemory(int size) {
        maxSize = size;
    }

    public boolean retrieve(int page) {
        if (maxSize == 0)
            throw new IllegalStateException("Cant put anything into memory, it is empty");

        if (memory.contains(page)) {
            memory.remove(page);
            memory.add(page);
            return true;
        } else {
            memory.add(page);
            if (memory.size() > maxSize)
                memory.remove();
            return false;
        }
    }

    public boolean containsPage(int page) {
        return memory.contains(page);
    }

    public void setSize(int size) {
        if (size < 0)
            throw new IllegalArgumentException("Size must be positive or 0");

        maxSize = size;
        while (memory.size() > maxSize) {
            memory.remove();
        }
    }

    public void addSize(int i) {
        setSize(maxSize + i);
    }

    public int getSize() {
        return maxSize;
    }

    public boolean isFull() {
        return memory.size() == maxSize;
    }
}
