/***************
* Brian Burns
* Sorter.java
* Implements a quicksort
***************/

import java.util.Random;

public class Sorter {
  private String[] words;

  private Random rand = new Random();

  public Sorter(String[] toSort) {
    words = toSort;
  }

  public void alphabetize(int start, int end) {
    if (end-start <= 1) return;
    int random = rand.nextInt(end-start) + start;
    flip(end-1, random);
    String pivot = words[end-1];
    int divide = start;
    for (int i=start; i<end-1; i++) {
      if (words[i].compareToIgnoreCase(pivot) < 1) {
        flip(i, divide);
        divide++;
      }
    }
    flip(end-1, divide);
    alphabetize(start, divide);
    alphabetize(divide+1, end);
  }

  private void flip(int a, int b) {
    String temp = words[a];
    words[a] = words[b];
    words[b] = temp;
  }

  public String[] getSorted() {return words;}
}