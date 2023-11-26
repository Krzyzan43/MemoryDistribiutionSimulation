package Tests;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import Program.Memory.MemoryDistributor;

public class DistributorTests {
    @Test
    void DistribiuteEvenlyMemoryTooSmall() {
        assertThrows(IllegalArgumentException.class, () -> {
            MemoryDistributor.DistribiuteEvenly(5, 4);
        });
    }

    @Test
    void DistribiuteEvenlyExactMatch() {
        assertArrayEquals(MemoryDistributor.DistribiuteEvenly(5, 25), new int[] { 5, 5, 5, 5, 5 });
    }

    @Test
    void DistribiuteEvenlyRounding() {
        assertArrayEquals(MemoryDistributor.DistribiuteEvenly(5, 28), new int[] { 6, 6, 6, 5, 5 });
    }

    @Test
    void DistribiuteProportionallyMemoryTooSmall() {
        assertThrows(IllegalArgumentException.class, () -> {
            MemoryDistributor.DistribiuteProportionally(new int[] { 1, 5, 4, 2 }, 3);
        });
    }

    @Test
    void DistribiuteProportionallyLargeMemory() {
        int[] res = MemoryDistributor.DistribiuteProportionally(new int[] { 2, 3, 6, 1 }, 48);
        assertArrayEquals(new int[] { 2, 3, 6, 1 }, res);
    }

    @Test
    void DistribiuteProportionallyAllOnes() {
        int[] res = MemoryDistributor.DistribiuteProportionally(new int[] { 12, 42, 1, 50, 12 }, 5);
        assertArrayEquals(new int[] { 1, 1, 1, 1, 1 }, res);
    }

    @Test
    void DistribiuteProportionallyRounding() {
        int[] res = MemoryDistributor.DistribiuteProportionally(new int[] { 3, 1, 9, 3, 6 }, 15);
        assertArrayEquals(new int[] { 2, 1, 6, 2, 4 }, res);
    }

    @Test
    void DistribiuteProportionallyTakeFromBig() {
        int[] res = MemoryDistributor.DistribiuteProportionally(new int[] { 8, 10, 1, 1, 6 }, 13);
        assertArrayEquals(new int[] { 4, 4, 1, 1, 3 }, res);
    }

    @Test
    void DistribiuteProportionallyDisabled() {
        int[] res = MemoryDistributor.DistribiuteProportionally(new int[] { 3, 1, 0, 0, 9, 3, 0, 6, 0 }, 15);
        assertArrayEquals(new int[] { 2, 1, 0, 0, 6, 2, 0, 4, 0 }, res);
    }
}
