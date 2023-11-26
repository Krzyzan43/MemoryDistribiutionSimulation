package Tests;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Program.Memory.ProcessMemory;

public class ProcessMemoryTests {
    @Test
    public void setSizeNegative() {
        ProcessMemory memory = createTestMemory();
        assertThrows(IllegalArgumentException.class, () -> {
            memory.setSize(-1);
        });
    }

    @Test
    public void setSizeZero() {
        ProcessMemory memory = createTestMemory();
        memory.setSize(0);
        assertContainsPages(new int[] {}, memory);
    }

    @Test
    public void reduceSize() {
        ProcessMemory memory = createTestMemory();
        memory.setSize(2);

        assertContainsPages(new int[] { 3, 4 }, memory);
    }

    @Test
    public void addSizePositive() {
        ProcessMemory memory = createTestMemory();
        memory.addSize(1);
        memory.retrieve(5);
        memory.retrieve(6);

        assertContainsPages(new int[] { 1, 2, 3, 4, 5, 6 }, memory);
    }

    @Test
    public void addSizeNegative() {
        ProcessMemory memory = createTestMemory();
        memory.addSize(-3);

        assertContainsPages(new int[] { 3, 4 }, memory);
    }

    @Test
    void addSizeBelowZero() {
        ProcessMemory memory = createTestMemory();
        assertThrows(IllegalArgumentException.class, () -> {
            memory.addSize(-6);
        });
    }

    @Test
    void getSize() {
        ProcessMemory memory = createTestMemory();
        assertEquals(memory.getSize(), 5);
    }

    @Test
    void getSizeAfterSetSize() {
        ProcessMemory memory = createTestMemory();
        memory.setSize(2);
        assertEquals(memory.getSize(), 2);
    }

    @Test
    void getSizeAfterAddSize() {
        ProcessMemory memory = createTestMemory();
        memory.addSize(-2);
        assertEquals(memory.getSize(), 3);
    }

    @Test
    void retrieveAll() {
        ProcessMemory memory = createTestMemory();

        assertEquals(memory.retrieve(0), true);
        assertEquals(memory.retrieve(1), true);
        assertEquals(memory.retrieve(2), true);
        assertEquals(memory.retrieve(3), true);
        assertEquals(memory.retrieve(4), true);
    }

    @Test
    void retrieveReplaceSingle() {
        ProcessMemory memory = createTestMemory();
        assertEquals(memory.retrieve(5), false);

        assertEquals(memory.retrieve(1), true);
        assertEquals(memory.retrieve(2), true);
        assertEquals(memory.retrieve(3), true);
        assertEquals(memory.retrieve(4), true);
        assertEquals(memory.retrieve(5), true);

        assertEquals(memory.retrieve(0), false);
        assertEquals(memory.retrieve(0), true);

        assertEquals(memory.retrieve(1), false);
    }

    @Test
    void retrieveEmptyMemory() {
        ProcessMemory memory = createTestMemory();
        memory.setSize(0);

        assertThrows(IllegalStateException.class, () -> {
            memory.retrieve(0);
        });
    }

    private static ProcessMemory createTestMemory() {
        ProcessMemory memory = new ProcessMemory(5);
        memory.retrieve(0);
        memory.retrieve(1);
        memory.retrieve(2);
        memory.retrieve(3);
        memory.retrieve(4);

        return memory;
    }

    private static void assertContainsPages(int[] pages, ProcessMemory memory) {
        int[] possiblePages = { 0, 1, 2, 3, 4 };

        for (int i : possiblePages) {
            assertEquals(memory.containsPage(i), contains(pages, i));
        }
    }

    private static boolean contains(int[] array, int x) {
        for (int i : array) {
            if (i == x)
                return true;
        }
        return false;
    }
}
