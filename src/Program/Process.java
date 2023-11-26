package Program;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class Process {
    Iterator<Integer> it;

    ArrayList<Integer> pages = new ArrayList<>();
    public int memoryNeeded = 0;

    public Process(ArrayList<Integer> pages) {
        this.pages = pages;
        it = this.pages.iterator();

        HashSet<Integer> distinctPages = new HashSet<>();
        for (Integer page : pages) {
            if (distinctPages.add(page)) {
                memoryNeeded++;
            }
        }
    }

    public boolean hasNextPage() {
        return it.hasNext();
    }

    public int nextPage() {
        return it.next();
    }

    public Process copy() {
        return new Process(pages);
    }

    @Override
    public String toString() {
        return Arrays.toString(pages.toArray());
    }
}
