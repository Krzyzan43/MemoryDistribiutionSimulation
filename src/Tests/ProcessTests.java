package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import Program.Process;

public class ProcessTests {
    @Test
    public void emptyProcess() {
        Process process = new Process(new ArrayList<>());
        assertEquals(process.hasNextPage(), false);
        assertThrows(NoSuchElementException.class, () -> {
            process.nextPage();
        });
    }

    @Test
    public void fullProcess() {
        Process process = createTestProcess();
        assertEquals(process.hasNextPage(), true);
        assertEquals(process.nextPage(), 7);
        assertEquals(process.hasNextPage(), true);
        assertEquals(process.nextPage(), 8);
        assertEquals(process.hasNextPage(), true);
        assertEquals(process.nextPage(), 3);
        assertEquals(process.hasNextPage(), true);
        assertEquals(process.nextPage(), 2);
        assertEquals(process.hasNextPage(), true);
        assertEquals(process.nextPage(), 4);

        assertEquals(process.hasNextPage(), false);
    }

    @Test
    public void copy() {
        Process process = createTestProcess();
        process.nextPage();
        process.nextPage();

        Process p2 = process.copy();
        assertEquals(p2.hasNextPage(), true);
        assertEquals(p2.nextPage(), 7);
        assertEquals(p2.hasNextPage(), true);
        assertEquals(p2.nextPage(), 8);
        assertEquals(p2.hasNextPage(), true);
        assertEquals(p2.nextPage(), 3);
        assertEquals(p2.hasNextPage(), true);
        assertEquals(p2.nextPage(), 2);
        assertEquals(p2.hasNextPage(), true);
        assertEquals(p2.nextPage(), 4);

        assertEquals(p2.hasNextPage(), false);
    }

    private static Process createTestProcess() {
        ArrayList<Integer> pages = new ArrayList<>() {
            {
                add(7);
                add(8);
                add(3);
                add(2);
                add(4);
            }
        };
        return new Process(pages);
    }
}
