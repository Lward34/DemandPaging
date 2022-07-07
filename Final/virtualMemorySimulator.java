/*
 * File:virtualMemorySimulator.java
 * Lilian Ward
 * CMSC 412
 * May 7, 2021
 * Purpose: This class will called virtual/physical frame 
 * based on a ref. String and supplied information provided
 * to run through simulations
 */


import java.util.ArrayList;
import java.util.Scanner;

public class virtualMemorySimulator{

    
   ArrayList<Integer> refString; // reference string
   int[] pageCalled; // Keeps track of physical pages
   int[] pageRemoved; // Keeps track of removed pages
   boolean[] pageFaults; // keeps track of page faults
   
   int refStringLen; // The size of the reference string
   int numbPhyFrames;//The number of Physical Frames
   int numbVirtPages;//The number of Virtual Frames
   
   int[][] phyMemory;// 1st represents "time" or memory at that time.
   PhysicalFrames[] virtualFrames;//keep track of all virtual frames in Array
   String Algorithm;//keep track of which algorithm ran in the simulation 

  // Parameterized constructor to initialize instance variables(state) of object
   virtualMemorySimulator(ArrayList<Integer> refs, int p, int v){
       refString = refs;
       refStringLen = refString.size();
       pageCalled = new int[refStringLen];
       pageRemoved = new int[refStringLen];
       numbPhyFrames = p;
       numbVirtPages = v;
       phyMemory = new int[refString.size()][p];
       virtualFrames = new PhysicalFrames[v];
       pageFaults = new boolean[refStringLen];
   }

   //The "generate()" method uses the reference string and supplied information
   // about the virtual and physical memory to run through simulations.
   void generate(String alg) {
       initialize();
        setAlgorithm(alg);
       int curFrame = 0;// current Frame
       int insertFrame;
       int empty;
       int replaceFrame;
       int[] listOfFrames;
       int inMemory;
       // While loops step through each call of the simulation
       while (curFrame < getRefStringLen()) {
           insertFrame = getRefString().get(curFrame);
           if ("LRU".equals(alg)) {
                getVirtualFrames()[insertFrame].setLastUse(curFrame);
           } else if ("LFU".equals(alg)) {
                getVirtualFrames()[insertFrame].incrementTimesUsed();
           }
           empty = findIndex(getPhyMemory()[curFrame], -1);
           // if the page we need is already in physical memory...
           inMemory = findIndex(getPhyMemory()[curFrame], insertFrame);
           if (inMemory != -1) {
                getPageCalled()[curFrame] = inMemory;
               // no page fault!
                getPageFaults()[curFrame] = false;
           }
           // else if it is not in memory, but there is a space for it.
           else if (empty >= 0) {
                getPageCalled()[curFrame] = empty;
                getPhyMemory()[curFrame][empty] = insertFrame;
                getVirtualFrames()[insertFrame].setInserted(curFrame);
           }
           // not in memory and no empty space
           else {
               // find the frame to be removed depending on the algorithm
               switch (alg) {
                   case "FIFO":
                   // find the oldest frame
                   replaceFrame = findFirst(getPhyMemory()[curFrame]);
                   // update insertion time
                    getVirtualFrames()[insertFrame].setInserted(curFrame);
                   break;
                   case "OPT":
                   // calculate next uses
                   evaluatesNext(curFrame);
                   // find the least optimal page
                   replaceFrame = findLeastOptimal(getPhyMemory()[curFrame]);
                   break;
                   case "LFU":
                   // find least recently used
                   replaceFrame = findLfu(getPhyMemory()[curFrame]);
                   break;
                   case "LRU":
                   // Find least recently used
                   replaceFrame = findLru(getPhyMemory()[curFrame]);
                   // Updates the information for last use of the frame just called
                   break;
                   default:
                   System.out.println("Error: algorithm not recognized!");
                   return;
               }
               // This record removed frame
                getPageRemoved()[curFrame] = getPhyMemory()[curFrame][replaceFrame];
               // This record new frame spot
                getPageCalled()[curFrame] = replaceFrame;
               // This put the new frame in that spot
                getPhyMemory()[curFrame][replaceFrame] = insertFrame;


           }
           // Make the physical memory for the next call a copy of the physical
           // memory at the end of this call
           if ((curFrame + 1) < getRefStringLen()) {
               for (int i = 0; i < getNumbPhyFrames(); i ++) {
                    getPhyMemory()[curFrame +1][i] = getPhyMemory()[curFrame][i];
               }
           }
           curFrame += 1;
       }
   }

   // Find the first inserted Frame, given an array of Frame numbers
   int findFirst(int[] a) {
       int first = getVirtualFrames()[a[0]].getInserted();
       int firstIndex = 0;
       int checking;
       for (int i = 1; i < a.length; i++) {
           checking = getVirtualFrames()[a[i]].getInserted();
           if (checking < first) {
               first = checking;
               firstIndex = i;
           }
       }
       return firstIndex;
   }

   // Find least frequently used frame
   //by given an array containing frame numbers
   int findLfu(int[] a) {
       int lfuIndexFrame = 0;
       int lfuTimesUsed = getVirtualFrames()[a[lfuIndexFrame]].getTimesUsed();

       for (int i = 1; i < a.length; i++) {
           int temp = a[i];
           int tempTimesUsed = getVirtualFrames()[a[i]].getTimesUsed();

           if (tempTimesUsed < lfuTimesUsed) {
               lfuIndexFrame = i;
               lfuTimesUsed = tempTimesUsed;
           }
       }
       return lfuIndexFrame;
   }
   // Find least recently used frame, given an
   //array containing frame numbers
   int findLru(int[] a) {
       int lruIndexFrame = 0;
       int lruLastUsed = getVirtualFrames()[a[lruIndexFrame]].getLastUse();

       for (int i = 1; i < a.length; i++) {
           int temp = a[i];
           int tempLastUse = getVirtualFrames()[a[i]].getLastUse();

           if (tempLastUse < lruLastUsed) {
               lruIndexFrame = i;
               lruLastUsed = tempLastUse;
           }
       }
       return lruIndexFrame;
   }

   // Find "least optimal" frame
   int findLeastOptimal(int[] a) {
       int leastOptimalFrame = a[0];
       int indexOpt = 0;
       int leastNextOpt = getVirtualFrames()[leastOptimalFrame].getNextUse();
       for (int i = 1; i < a.length; i++) {
           int temp = a[i];
           int tempNextUse = getVirtualFrames()[temp].getNextUse();
           if (tempNextUse > leastNextOpt)
           {
               leastOptimalFrame = temp;
               leastNextOpt = getVirtualFrames()[leastOptimalFrame].getNextUse();
               indexOpt = i;
           }
       }
       //return least Optimal Index
       return indexOpt;
   }

  
   void evaluatesNext(int n){      
       for (int i = 0; i < getNumbVirtPages(); i++) {
            getVirtualFrames()[i].setNextUse(getRefStringLen() + 1);
       }
       // then it works backwards from the end
       for (int i = getRefStringLen() - 1; i >= n; i--) {
           int called = getRefString().get(i);
            getVirtualFrames()[called].setNextUse(i);
       }
   }

   // Initialize all the arrays used in generate()
   void initialize() {
       // Set page faults to false
       for (int i = 0; i < getPageFaults().length; i++) {
            getPageFaults()[i] = true;
       }
       // Set removed to -1s
       for (int i = 0; i < getPageRemoved().length; i++) {
            getPageRemoved()[i] = -1;
       }
       // Set pages changed to -1s
       for (int i = 0; i < getPageCalled().length; i++) {
            getPageCalled()[i] = -1;
       }  
       // Set clean array of frames:
       for (int i = 0; i < getNumbVirtPages(); i++) {
            getVirtualFrames()[i] = new PhysicalFrames(i);
       }
       // Clean array of slices
       for (int i = 0; i < getRefStringLen(); i++) {
           for (int j = 0; j < getNumbPhyFrames(); j ++) {
                getPhyMemory()[i][j] = -1;
           }
       }
      
        setAlgorithm("");
   }

   // Display the simulation results, one step at a time
   void printFrameInfo() {
       System.out.println("Memory information: "
                         +"\nAlgorithm type: " + getAlgorithm()
                         +"\nLength of reference string: " + getRefStringLen()
                         +"\nNumber of virtual pages: " + getNumbVirtPages()
                         +"\nNumber of physical pages: " + getNumbPhyFrames()
                         +"\n---"
                         +"\nPress 'Enter' to display the snapshots of physical memory"
                         +"\nOr, enter \"q\" at any time to return to the main menu.");
            
       Scanner keyboard = new Scanner(System.in);
       int steppingSlice = 0;
       String prompt;
       int frameNum;
       int removedInt;
       while (steppingSlice < getRefStringLen()) {
           prompt = keyboard.nextLine();
           if (prompt.equals("q")) {
               System.out.println("The printout has been completed.");
               break;
           }
           System.out.println("Snapshot at step " + (steppingSlice + 1) + ":");
           System.out.println("Program called virtual frame # "
                           + getRefString().get(steppingSlice));
           for (int i = 0; i < getNumbPhyFrames(); i ++) {
               System.out.print("Physical frame " + i + ":");
               frameNum = getPhyMemory()[steppingSlice][i];
               if (frameNum >= 0) {
                   if (i == getPageCalled()[steppingSlice]) {
                       System.out.println("[" + frameNum + "]");
                   } else {
                       System.out.println(" " + frameNum);
                   }
               } else {
                   System.out.println("x");
               }
           }
           removedInt = getPageRemoved()[steppingSlice];
           System.out.println("Page faults: " + (getPageFaults()[steppingSlice] ? "Yes." : "No."));
           System.out.println("Victim frames: " + (removedInt == -1 ? "None." : removedInt));
           steppingSlice += 1;
       }
       System.out.print("Simulation completed.Press enter to return to the main menu");
       keyboard.nextLine();
   }

   int findIndex(int[] a, int n) {
       for (int i = 0; i < a.length; i++) {
           if (a[i] == n) {
               return i;
           }
       }
       return -1;
   }

    /**
     * @return the refString
     */
    public ArrayList<Integer> getRefString() {
        return refString;
    }

    /**
     * @param refString the refString to set
     */
    public void setRefString(ArrayList<Integer> refString) {
        this.refString = refString;
    }

    /**
     * @return the pageCalled
     */
    public int[] getPageCalled() {
        return pageCalled;
    }

    /**
     * @param pageCalled the pageCalled to set
     */
    public void setPageCalled(int[] pageCalled) {
        this.pageCalled = pageCalled;
    }

    /**
     * @return the pageRemoved
     */
    public int[] getPageRemoved() {
        return pageRemoved;
    }

    /**
     * @param pageRemoved the pageRemoved to set
     */
    public void setPageRemoved(int[] pageRemoved) {
        this.pageRemoved = pageRemoved;
    }

    /**
     * @return the pageFaults
     */
    public boolean[] getPageFaults() {
        return pageFaults;
    }

    /**
     * @param pageFaults the pageFaults to set
     */
    public void setPageFaults(boolean[] pageFaults) {
        this.pageFaults = pageFaults;
    }

    /**
     * @return the refStringLen
     */
    public int getRefStringLen() {
        return refStringLen;
    }

    /**
     * @param refStringLen the refStringLen to set
     */
    public void setRefStringLen(int refStringLen) {
        this.refStringLen = refStringLen;
    }

    /**
     * @return the numbPhyFrames
     */
    public int getNumbPhyFrames() {
        return numbPhyFrames;
    }

    /**
     * @param numbPhyFrames the numbPhyFrames to set
     */
    public void setNumbPhyFrames(int numbPhyFrames) {
        this.numbPhyFrames = numbPhyFrames;
    }

    /**
     * @return the numbVirtPages
     */
    public int getNumbVirtPages() {
        return numbVirtPages;
    }

    /**
     * @param numbVirtPages the numbVirtPages to set
     */
    public void setNumbVirtPages(int numbVirtPages) {
        this.numbVirtPages = numbVirtPages;
    }

    /**
     * @return the phyMemory
     */
    public int[][] getPhyMemory() {
        return phyMemory;
    }

    /**
     * @param phyMemory the phyMemory to set
     */
    public void setPhyMemory(int[][] phyMemory) {
        this.phyMemory = phyMemory;
    }

    /**
     * @return the virtualFrames
     */
    public PhysicalFrames[] getVirtualFrames() {
        return virtualFrames;
    }

    /**
     * @param virtualFrames the virtualFrames to set
     */
    public void setVirtualFrames(PhysicalFrames[] virtualFrames) {
        this.virtualFrames = virtualFrames;
    }

    /**
     * @return the Algorithm
     */
    public String getAlgorithm() {
        return Algorithm;
    }

    /**
     * @param Algorithm the Algorithm to set
     */
    public void setAlgorithm(String Algorithm) {
        this.Algorithm = Algorithm;
    }
}