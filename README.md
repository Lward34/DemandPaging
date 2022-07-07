# DemandPaging
  This program implements a virtual memory simulator for Demand Paging. Here, the following algorithms are implemented: FIFO, OPT, LRU, and LFU. This application simulates the execution of each one of these algorithms based on N physical frames (from 0 to N-1, N <8), assuming that the only running process has a virtual memory of ten frames (0 to 9). Here, a sequence of pages will be accessed, read on the keyboard, or generated randomly.
  This program contains three classes. The first class, DemandPaging, is the main method of this program, which allows the user to select options from the displayed. A second class, virtual memory simulator, will generate the virtual and physical memory based on the reference string and supplied by the user. The third class, PhysicalFrame, a generic class that holds the set/get values inserted for the virtual and physical frame simulation.


![image](https://user-images.githubusercontent.com/79439802/177733091-d0d92f53-b5db-4cae-b7b8-ec5eec2042bf.png)
