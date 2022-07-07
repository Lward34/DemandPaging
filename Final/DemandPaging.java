/*
 * File:DemandPaging.java
 * Lilian Ward
 * CMSC 412
 * May 7, 2021
 * Purpose: This class contain the main method of program
 * Display the main menu of Demanding Paging 
 * and read the user choice
 */



import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class DemandPaging{
   //maximum number of virtual pages
   static final int MAX_VP = 10;
   // maximum number of physical pages
   static final int MAX_PP = 7;

   public static void main(String[] args) {
       // read in physical frame numbers
       int numbPhyFrames = readComandLine(args);
       System.out.println("Number of page frames set for "
               + numbPhyFrames + ".");

       // set up for main loop
       Scanner input = new Scanner(System.in);
       String line; // input from user
       ArrayList<Integer> refString = null;
       virtualMemorySimulator simulator;

       // begin main loop:
       while (true)
       {
           System.out.println();
           System.out.println("\t\tPlease choose from the following options: \n"+
                              "\t0 - Exit\n" +
                              "\t1 - Read reference string \n"+
                              "\t2 - Generate reference string\n"+
                              "\t3 - Display current reference string\n"+
                              "\t4 - Simulate FIFO Algorithm\n"+
                              "\t5 - Simulate OPT Algorithm\n"+
                              "\t6 - Simulate LRU Algorithm\n"+
                              "\t7 - Simulate LFU Algorithm\n");
          
           // read input
           line = input.next();
           input.nextLine();
    switch (line) {
        case "0":
            // "Exit" the program 
            System.out.println("Thank you for using this program.Goodbye.");
            System.exit(0);
            break;
        case "1":
            // Reads the Reference String
            refString = readRefString(input);
            // and Confirm
            stringConfirm(refString);
            break;
        case "2":
            // Generates the Reference String
            // get length of desired string
            System.out.println("Enter the reference string length to be generated?");
            int stringSize = getStringSize(input);
            // Generates the String
            refString = generateString(stringSize, MAX_VP);
            // Confirm
            stringConfirm(refString);
            break;
        case "3":
            // Prints the Reference String
        if (refString != null) {
            System.out.print("Enter the current reference string: ");
            int i;
        for (i = 0; i < refString.size() - 1; i++) {
             System.out.print(refString.get(i) + ", ");
             }
                System.out.print(refString.get(i));
                System.out.print(".");
            } else {
                 System.out.println("Error: no reference string entered.");
               }
             break;
        case "4":
        // Check if that refString has been set:
        // test reference String:
        if (refIsSet(refString)) {
                // then creates simulation conditions, run it, and print
                simulator = new virtualMemorySimulator(refString, numbPhyFrames, MAX_VP);
                simulator.generate("FIFO");
                simulator.printFrameInfo();
             }
              break;
        case "5":
      // Check if that refString has been set:
        if (refIsSet(refString)) {
                // then creates simulation conditions, run it, and print
                simulator = new virtualMemorySimulator(refString, numbPhyFrames, MAX_VP);
                simulator.generate("OPT");
                simulator.printFrameInfo();
             }
               break;
        case "6":
               // Checks if that reference String has been set:
        if (refIsSet(refString)) {
                // then creates simulation conditions, run it, and print
                simulator = new virtualMemorySimulator(refString, numbPhyFrames, MAX_VP);
                simulator.generate("LRU");
                simulator.printFrameInfo();
             }
               break;
        case "7":
        // Checks if that reference String has been set:
        if (refIsSet(refString)) {
                // then creates simulation conditions, run it, and print
                simulator = new virtualMemorySimulator(refString, numbPhyFrames, MAX_VP);
                simulator.generate("LFU");
                simulator.printFrameInfo();
             }
               break;
               default:
               break;
           } 
       } 
   } 

   private static int readComandLine(String[] args) {
       // Checks whether the number of arguments is correct
       if (args.length < 1) {
           System.out.println("Error: need to pass exactly 1"
                   +"command line argument for number of physical frames.");
           System.exit(-1);
       }
       if (args.length > 1) {
           System.out.println("Warning: Too many command line arguments."
                   +"Every argument after the 1st will be ignored.");
       }
       // The n will be the # of physical page frames
       int n = -1;

       // try to parse int and catch exceptions
       try {
           n = Integer.parseInt(args[0]);
       } catch(NumberFormatException e) {
           System.out.println("Error: command line argument must be an integer.");
           System.exit(-1);
       }

       // if n is between 0 and N - 1
       if (n < 1 || n > MAX_PP) {
           System.out.println("Error: this must be between 1 and "
                   + (MAX_PP) + " physical frames.");
           System.exit(-1);
       }

       // everything worked well, then returns "n".
       return n;
   }

   static ArrayList<Integer> readRefString(Scanner in) {
       System.out.println("Enter a series of numbers: ");
       //Creates RefString
       ArrayList<Integer> list = new ArrayList<>();
       do {
           // Read in a line
           String line = in.nextLine();
           // Creates a scanner to operate on that line
           Scanner srInput = new Scanner(line);
           // Extract the ints
           String temp;
           int tempInt = -1;
           boolean isInt;
           while (srInput.hasNext()) {
               temp = srInput.next();
               isInt = false;
               try {
                   tempInt = Integer.parseInt(temp);
                   isInt = true;
               } catch (NumberFormatException e) {
                   System.out.println("Warning: you entered a non-integer; \""
                           + temp + "\" ignored.");
               }
               // Ensure if the numbers entered are between 0 and 9:
               if (isInt && (tempInt < 0 || tempInt >= MAX_VP))
               {
                   System.out.println("Warning: numbers must be between 0 and "
                           + (MAX_VP - 1) + "; \"" + temp + "\" ignored.");
               } else if (isInt) {
                   list.add(tempInt);
               }
           }
           // Make sure at least 1 valid int entered:
           if (list.size() < 1) {
               System.out.println("Error: Ref.string must be at least one"
           +"integer (0 to 9). Please try again.");
           }
       } while (list.size() < 1);
       return list;//return reference string
   }

   static int getStringSize(Scanner in) {
       //read in a line; 
       //Then parse an int
       int stringSize = 0;
       while (stringSize < 1) {
           try {
               stringSize = in.nextInt();
           }
           catch (InputMismatchException e) {
               System.out.println("You must enter an integer!");
           }
           in.nextLine();
           if (stringSize < 1) {
               System.out.println("You must enter a positive integer!");
           }
       }
       // if int is out of bounds, throws an error
       return stringSize;
   }

   static ArrayList<Integer> generateString(int n, int max) {
       // NOTE: the max here is exclusive
       // validate input
       if (n < 1) {
           System.out.println("Error: cannot create a reference string shorter than 1.");
           return null;
              
       }
       Random rd = new Random();

       // Creates ArrayList for ints,
       ArrayList<Integer> arList = new ArrayList<>();
       // generates n random numbers, and add them to the list.
       for (int i = 0; i < n; i++) {
           arList.add(rd.nextInt(max));
       }

       // Uses the ArrayList to create a RefString
       ArrayList<Integer> rs = arList;
       // then return the RefString
       return rs;
   }

   static void stringConfirm(ArrayList<Integer> rs) {
       if (rs != null) {
           System.out.print("Valid reference string: ");
           int i;
           for (i = 0; i < rs.size() - 1; i++) {
               System.out.print(rs.get(i) + ", ");
           }
           System.out.print(rs.get(i));
           System.out.print(".");
       } else {
           System.out.println("Invalid reference string. Please try again.");
       }
   }

   static boolean refIsSet(ArrayList<Integer> ref) {
       if (ref != null) {
           return true;
       }
       System.out.println("Error: reference string not yet entered and generated!");
       return false;
   }
}