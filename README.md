# MemoryDistribiutionSimulation
This project simulates different strategies for distributing memory to processes. At the start of a simulation multiple simulated processes are created, each of which needs memory to operate. Memory is divided into pages and if process request a memory page when memory is full, a swap takes place. That means existing page is outed and current page is inserted into memory. When all processes are finished and they stop accessing memory, program prints how many swaps had taken place and how many steps it took.

# Strategies
  * Constant Size strategy - Memory is distributed evenly among processes and it doesn't change. For example with memory of 20 pages and 4 processes, each one gets 5 pages for its entire lifetime
  * Proportional Size strategy - Memory is distributed proportionally among processes, but each process needs to declare approximately how much memory will it need
  * PFF (Piloting Fault Frequency) - When number of swaps for a process is too high, it gets assigned additional page. When it gets too low, a page is removed from that process. If swaps go above certain treshold, process gets paused unitl there's free memory.
  * WSS - Strategy looks at process history and calculates how much memory it needs, then assigns to it adequate number of pages. Process gets stopped if it requests too much memory.

# Example output

```
Algorithm: ConstSize
Faults: 1761
Steps: 980

Algorithm: Proportional
Faults: 2100
Steps: 980

Algorithm: PFF
Faults: 968
Steps: 2364

Algorithm: WSS
Faults: 448
Steps: 3270
```
Steps can change when process gets paused
