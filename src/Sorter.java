/***************
* Brian Burns
* Sorter.java
* Implements a quicksort
***************/

import java.util.Random;

/**
 * Implements a quicksort to alphabetize an array
 * @author Brian Burns
 */
public class Sorter {
  /**
   * The array to sort
   */
  private String[] words;

  /**
   * A random integer used for a random quicksort pivot
   */
  private Random rand = new Random();

  /**
   * Initializes the object and stores the array to sort
   * @param toSort The array to sort
   */
  public Sorter(String[] toSort) {
    words = toSort;
  }

  /**
   * A randomized quicksort for alphabetizing an array of strings
   * @param start The start of the range to sort
   * @param end The end of the range to sort
   */
  public void alphabetize(int start, int end) {
    // If the array length is one or less
    if (end-start <= 1) return;
    // Get a random number in the range of the indices
    int random = rand.nextInt(end-start) + start;
    // Switch the last item and the random item picked
    flip(end-1, random);
    // Set the last item as the pivot
    String pivot = words[end-1];
    // Set the dividing line as the start
    int divide = start;
    // Go through the array range
    for (int i=start; i<end-1; i++) {
      // If the current item comes before the pivot in the alphabet
      if (words[i].compareToIgnoreCase(pivot) < 1) {
        // Switch it with the dividing item
        flip(i, divide);
        // Increase the dividing line
        divide++;
      }
    }
    // Switch the pivot and the dividing item
    flip(end-1, divide);
    // Recursively call alphabetize
    alphabetize(start, divide);
    alphabetize(divide+1, end);
  }

  /**
   * A function to flip two array items
   * @param a Item A of the array
   * @param b Item B of the array
   */
  private void flip(int a, int b) {
    String temp = words[a];
    words[a] = words[b];
    words[b] = temp;
  }

  /**
   * Returns the sorted array
   * @return The sorted array
   */
  public String[] getSorted() {return words;}
}