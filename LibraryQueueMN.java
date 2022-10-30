
/**
 * @Name    Michael Nguyen
 * @Course  COSC 2436
 * @ProgSet 2 Problem 2
 * @Description The local library allows patrons to borrow three books at a 
 *      time, one per day. When they borrow the first three books, each goes 
 *        on the next empty shelf of three that they have, one book per shelf, 
 *      starting with the uppermost empty shelf. For example, if they request 
 *      books 5, 1 and 2, they go onto the top, middle and bottom shelves, 
 *      and so the books on the shelves from top to bottom are 5 1 2. 
 *      Once a book is put on a shelf, it is kept there until they return it 
 *      to the local library.
 */

// import java.util.Scanner;
// import java.util.zip.Inflater;

import javax.lang.model.element.Element;
import javax.lang.model.util.ElementScanner14;

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
import java.util.*;
import java.io.*;


public class LibraryQueueMN {

    // Constants for position of the shelf
    static final String TOP = "top";
    static final String MIDDLE = "middle";
    static final String BOTTOM = "bottom";
    static final int MAX = 3;

    /**
     * @name QueueLL
     * @type Class
     * @info The QueueLL class is a linked list implementation of a queue
     * @members front The node that is the front of the queue
     * @members back The node that is the back of the queue
     * @members size The number of elements in the queue
     */
    private static class QueueLL {
        private QNode front;
        private QNode rear;
        private int size;
        private Boolean need_LRU;;

        /**
         * @name QueueLL()
         * @info Default Constructor that creates a new node and setting it as the front
         *       node. The rear node is then set to point to the front node. The size is
         *       then incremented.
         */
        QueueLL() {
            this.front = null;
            this.rear = null;
            this.size = 0;
            this.need_LRU = false;

        }// end constructor

        /**
         * @name getSize()
         * @type Method
         * @return size The total elements of the array.
         * @info Returns the size of the queue linkedlist
         * 
         */
        public int getSize() {
            return size;
        }// end getSize

        /**
         * @name enqueue()
         * @type Method
         * @param data The number of the book to be put into the queue
         * @return Boolean value that is false for empty linked list and true after
         *         successful insertion of node.X
         * @info Method adds a nodat the rear of teh queue. If the queue is empty,X
         *       return false. Otherwise, increment the size of the queue and returnX
         *       trueX
         */
        public Boolean enqueue(int data) {

            // tempPtr to traverse the queue
            QNode tempPtr;
            tempPtr = front;

            // check if queue is empty
            if (front == null) {
                rear = new QNode(data);
                front = rear;
                front.shelfPos = TOP;
            } else {
                // LRU queue is does not need to search for data in queue
                if (this.need_LRU == false) {

                    // Check for pre-existing data value in the queue
                    while (tempPtr != null) {
                        if (tempPtr.data == data) {
                            // data found in the queue already so exit
                            return true;
                        }
                        tempPtr = tempPtr.next;
                    }
                    
                    this.need_LRU = false;
                    // turn LRU flag back to false if turned on
                }// end need_LRU
                // this.need_LRU = false;

                // create new node with data at end and link to rear
                rear.next = new QNode(data);
                rear = rear.next;
                // Lable the Middle node only once
                if(size == 1) {
                
                    rear.shelfPos = MIDDLE;
                }
                else if(size == 2){
                    rear.shelfPos = BOTTOM;
                }


            } // end if queue is empty
            size++;

            // Remove front book if size of queue is more than MAX (3)
            if (size > MAX) {
                // Assigning top to the node behind front before it is removed
   
                front.next.next.next.shelfPos = front.shelfPos;
            
          
                // Remove the link from the front of the queue
                front = front.next;
                size--;

            }

            return true;

        } // end enqueue

        public void displayQ() {
            QNode temp = front;
            while (temp != null){
                System.out.print(temp.toString());
                temp = temp.next;
            }

        }

        
    }// end QueueLL class definition

    /**
     * @name QNode
     * @type Class
     * @info Class for nodes in the queue that contains an integer and a pointer to
     *       the next node in the queue
     */
    static class QNode {
        private int data;
        private QNode next;
        private String shelfPos = null;

        /**
         * @name QNode()
         * @type Method (default constructor)
         * @info This is the default constructor that initializes node to null
         */
        public QNode() {
            this.data = 0;
            this.next = null;
        }// end constructor

        /**
         * @name QNode()
         * @type Method (constructor)
         * @param data
         * @info This is a constructor that takes an integer as an argument and sets the
         *       data field of the object to the value of the argument.
         */
        public QNode(int data) {
            this.data = data;
            this.next = null;
        }// end constructor

        /**
         * @name setData()
         * @type Method (acceessor)
         * @param data The data to be stored in the node.
         * @info Method takes an integer as an argument and sets the data field of the
         *       object to the value of the argument
         */
        public void setData(int data) {
            this.data = data;
        }// end setData

        /**
         * @name getData()
         * @type Method (acceessor)
         * @return The data variable is being returned.
         * @info Method returns the value of the variable data
         */
        public int getData() {
            return data;
        }// end getData

        /**
         * @name toString()
         * @type Method
         * @return The data of the node.
         * @info Method overides the obj.toString() method to print the elements of the
         *       node
         */
        public String toString() {
            return this.data + " ";
        }// end toString

    }// end QNode class definition

    /***********************************************************************************
     * @name main
     * @method
     * @param args
     ***********************************************************************************/
    public static void main(String[] args) {

        QueueLL requestQ = new QueueLL();        // Retreive the data from external file

        getRequest(requestQ);

    }// end main

    /**
     * @param q
     */
    public static void getRequest(QueueLL q) {
        // Create object to read in data from file
        Scanner scanName = new Scanner(System.in);

        // Prompt user to enter the name of file that contains the sequence of book
        // requests. Input will be used to open and read in the dadta
        System.out.print("Please enter the file name: ");
        String fileName = scanName.nextLine();

        File file = new File(fileName);
        try {
            Scanner scanner = new Scanner(file);
            int numberOfSeqs = scanner.nextInt();

            // Create array list to hold the sequence of book request read in
            ArrayList<Integer>[] sequence = new ArrayList[numberOfSeqs];
            for (int i = 0; i < numberOfSeqs; i++) {
                // Populate the array list with the sequences
                sequence[i] = new ArrayList<Integer>();

            } // for loop over sequences

            // Move scanner to nextLine
            scanner.nextLine();

            // Read in the sequence as a string but split by whitespace and placed in
            // arrayList
            for (int i = 0; i < numberOfSeqs; i++) {
                // Split up the sequence and store in string
                String line[] = scanner.nextLine().split(" ");
                int num[] = new int[line.length];

                for (int j = 0; j < num.length; j++) {
                    num[j] = Integer.parseInt(line[j]);
                    sequence[i].add(num[j]);
                }

            } // end read in

            scanner.close();

            // Populate the Queue
            for (int j = 0; j < sequence[0].size(); j++) {
                int num = sequence[0].get(j);
                q.enqueue(num);

            } // end populating queue
            System.out.print("\nRequest 1:\n\nFIFO: ");
            q.displayQ();
            // System.out.print("\n" + q.front.shelfPos + " + " + q.front.next.shelfPos + " + " + q.rear.shelfPos);

            // Set Flag for LRU queue
            q.need_LRU = true;
            for (int j = 0; j < sequence[0].size(); j++) {
                int num = sequence[0].get(j);
                q.enqueue(num);

            } // end populating queue
            System.out.print("\nLRU: ");
            q.displayQ();
            // System.out.print("\n" + q.front.shelfPos + " + " + q.front.next.shelfPos + " + " + q.rear.shelfPos);
            
            q.need_LRU = false;
            // Populate the Queue
            for (int j = 0; j < sequence[1].size(); j++) {
                int num = sequence[1].get(j);
                q.enqueue(num);

            } // end populating queue
            System.out.print("\n\nRequest 2:\n\nFIFO: ");
            q.displayQ();

            // Set Flag for LRU queue
            q.need_LRU = true;
            for (int j = 0; j < sequence[1].size(); j++) {
                int num = sequence[1].get(j);
                q.enqueue(num);

            } // end populating queue   
            System.out.print("\nLRU: ");
            q.displayQ();                     

        } // end try statement

        catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } // end catch

    }// end getRequest()

}// end LibraryQueueMN
