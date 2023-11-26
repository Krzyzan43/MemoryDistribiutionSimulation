package Program.Memory;

// Holds processMemory for every process
// Keeps track of how much memory is left
// Memory can only be accessed by this class
public class Memory {
    private ProcessMemory[] memory;
    private int memorySize;
    private int freeMemory;

    public Memory(int count, int memorySize) {
        memory = new ProcessMemory[count];
        for (int i = 0; i < count; i++) {
            memory[i] = new ProcessMemory(0);
        }
        freeMemory = memorySize;
        this.memorySize = memorySize;
    }

    public void decrement(int index, int amount) {
        if (index < 0 || index >= memory.length)
            throw new IllegalArgumentException();
        if (amount < 0 || amount > memory[index].getSize())
            throw new IllegalArgumentException();

        memory[index].addSize(-amount);
        freeMemory += amount;
    }

    public boolean hasFreeMemory() {
        return freeMemory != 0;
    }

    public void increment(int index, int amount) {
        if (amount > freeMemory || amount < 0 || index < 0 || index >= memory.length)
            throw new IllegalArgumentException();

        memory[index].addSize(amount);
        freeMemory -= amount;
    }

    public int getFreeMemory() {
        return freeMemory;
    }

    public int getMaxMemory() {
        return memorySize;
    }

    public void distribiute(int[] distribiution) {
        if (distribiution.length != memory.length)
            throw new IllegalArgumentException();

        int memoryDistribiuted = 0;
        for (int i : distribiution) {
            memoryDistribiuted += i;
        }
        if (memoryDistribiuted > memorySize)
            throw new IllegalArgumentException("Too much memory to distribiute");

        for (int i = 0; i < memory.length; i++) {
            memory[i].setSize(distribiution[i]);
        }
        freeMemory = memorySize - memoryDistribiuted;
    }

    public void setToZero(int index) {
        freeMemory += memory[index].getSize();
        memory[index].setSize(0);
    }

    public boolean retrieve(int index, int page) {
        return memory[index].retrieve(page);
    }

    public int getSize(int i) {
        return memory[i].getSize();
    }

    public boolean isFull(int i) {
        return memory[i].isFull();
    }
}
