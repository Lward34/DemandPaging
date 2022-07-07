/*
 * File:PhysicalFrames.java
 * Lilian Ward
 * CMSC 412
 * May 7, 2021
 * Purpose: This generic class holds the set/get values 
 * inserted by the user for the virtual/physical frame 
 */



public class PhysicalFrames{
   int values;
   int insertedV;
   int next;//next Use
   int last;//last Use
   int timesUsed;

   PhysicalFrames(int n) {
       values = n;
       insertedV = -1;
       next = -1;
       last = -1;
       timesUsed = 0;
   }
   // Method to set the number
   void setNum(int n) {
        setValues(n);
   }
   //Method to get the number
   int getNum() {
       return getValues();
   }
   //Method to set the inserted
   void setInserted(int n) {
        setInsertedV(n);
   }
   //Method to set the inserted
   int getInserted() {
       return getInsertedV();
   }
   void setNextUse(int n) {
        setNext(n);
   }
   int getNextUse() {
       return getNext();
   }
   void setLastUse(int n) {
        setLast(n);
   }
   int getLastUse() {
       return getLast();
   }
   void incrementTimesUsed() {
        setTimesUsed(getTimesUsed() + 1);
   }
   int getTimesUsed() {
       return timesUsed;
   }

    /**
     * @return the values
     */
    public int getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(int values) {
        this.values = values;
    }

    /**
     * @return the insertedV
     */
    public int getInsertedV() {
        return insertedV;
    }

    /**
     * @param insertedV the insertedV to set
     */
    public void setInsertedV(int insertedV) {
        this.insertedV = insertedV;
    }

    /**
     * @return the next
     */
    public int getNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public void setNext(int next) {
        this.next = next;
    }

    /**
     * @return the last
     */
    public int getLast() {
        return last;
    }

    /**
     * @param last the last to set
     */
    public void setLast(int last) {
        this.last = last;
    }

    /**
     * @param timesUsed the timesUsed to set
     */
    public void setTimesUsed(int timesUsed) {
        this.timesUsed = timesUsed;
    }
}