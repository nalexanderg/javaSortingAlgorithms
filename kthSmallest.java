import java.util.*;

public class kthSmallest {
  static int count = 0;

  public static void main(String[] args) {
    // Scanner input = new Scanner(System.in);
    //
    // System.out.print("Enter the position you would like the value of: ");
    // int kPos = input.nextInt();                                                          // user picks k
    int n;
    // n = 100;
    // test(n, kPos);
    // for (int i=0; i < 100; i++) {
      n = 10000;
      test(n, (n/2));                                                                       // call function for testing
      n = 100000;
      test(n, (n/2));
      n = 1000000;
      test(n, (n/2));
    // }
  }

  private static void test(int n, int k) {
    // if (k > n) {                                                                         // for k user input
    //   System.out.println("Error: k position is larger than n.");
    //   return;
    // }

    int[] MS = new int[n];
    int[] HP = new int[n];

    int rndm;
    for (int i=0; i < n; i++) {
      rndm = (int)(Math.random() * 1000000);                                                // identical arrays of random integers
      MS[i] = rndm;
      HP[i] = rndm;

    }

    MergeSort(MS);
    System.out.println("MergeSort:");
    System.out.println("    n = " +n);
    System.out.println("    k = " +k);
    System.out.println("    A[kPos] = " +MS[k-1]);                                          // k-1, because start at 0
    System.out.println("    Number of Comparisons = " +count);
    count = 0;                                                                              // reset comp count

    int kPrint = QuickSelect(HP, 0, n-1, k);                                                // n-1, because r is rightmost
    System.out.println("HoarePartition:");
    System.out.println("    n = " +n);
    System.out.println("    k = " +k);
    System.out.println("    A[kPos] = " +kPrint);                                           // kPrint should equal A[k-1]
    System.out.println("    Number of Comparisons = " +count);
    count = 0;                                                                              // reset comp count
  }

  private static void MergeSort(int[] A) {
    int n = A.length;
    int median = (int)(Math.floor(n/2));
    int[] B = new int[median];
    int[] C;
    if ((n % 2) == 0) {
      C = new int[median];
    } else {
      C = new int[median+1];                                                                // if odd, include A[r]
    }

    if (n > 1) {
      for (int i=0; i < B.length; i++) {                                                    // copy A[0... med-1] to B
        B[i] = A[i];
      }
      int j = 0;
      for (int w = median; j < C.length; j++, w++) {                                        // copy A[med... n-1] to C
        C[j] = A[w];
      }
      MergeSort(B);
      MergeSort(C);

      Merge(B, C, A);
    }
  }

  private static void Merge(int[] B, int[] C, int[] A) {
    int i = 0, j = 0, w = 0;
    while ((i < B.length) && (j < C.length)) {                                              // first to reach length breaks the loop
      if (comp(B[i], C[j]) == 1) {
        A[w++] = C[j++];
      } else {
        A[w++] = B[i++];
      }
    }
    if (i == B.length) {                                                                    // the other is now "dumped" into end of A
      for (int x = j, y = w; y < (B.length + C.length); x++, y++) {
        A[y] = C[x];
      }
    } else {
      for (int x = i, y = w; y < (B.length + C.length); x++, y++) {
        A[y] = B[x];
      }
    }
  }

  private static int QuickSelect(int[] A, int l, int r, int k) {
    if (l == r) {
      return A[l];
    }

    int s = HoarePartition(A, l, r);

    if (comp(s, (k-1)) == 0) {
      return A[s];
    } else if (comp(s, (k-1)) == 1) {         // HAD TO CHANGE THE COMP FROM s>(l+k-1) TO (k-1) BECAUSE IM USING ONE ARRAY!!!!
      return QuickSelect(A, l, (s-1), k);                                                   // s > k, so continue with lower partition
    } else {
      return QuickSelect(A, (s+1), r, k);     // HAD TO CHANGE PARAMETER FROM (k-1-s) TO k BECAUSE IM USING ONE ARRAY!!!!
    }                                                                                       // s < k, so continue with upper partition
  }

  private static int HoarePartition(int[] A, int l, int r) {
    int pIndex = l + (int)(Math.random() * (r-l+1));                                       // random pivot selection
    swap(A, pIndex, l);
    int pivot = A[l];
    // int pivot = medianOfThree(A, l, r);                                                 // median of three pivot selection
    int i = l, j = r+1;

    do {
      do {
        if (i>=r) {                                                                         // sentinel
          break;
        }
        i++;
      } while (comp(A[i], pivot) == -1);
      do {
        if (j<=l) {                                                                         // sentinel
          break;
        }
        j--;
      } while (comp(A[j], pivot) == 1);
      swap(A, i, j);
    } while (i < j);

    swap(A, i, j);
    swap(A, l, j);                                                                          // j is new position of pivot

    return j;
  }

  ///////////////////////////////////////////////////////////////// MEDIAN OF THREE ////////////////////////////////////////////////////////////////
  // private static int medianOfThree(int[] A, int l, int r) {
  //   int mid = (int)(Math.floor((l+r)/2));
  //
  //   if (A[mid] < A[l]) {
  //     swap(A, l, mid);
  //   }
  //   if (A[r] > A[l]) {
  //     swap(A, l, r);
  //   }
  //   if (A[r] < A[mid]) {
  //     swap(A, mid, r);
  //   }
  //   swap(A, mid, l);
  //
  //   return A[l];                                                                            // A[l] is the median of A[l], A[mid], A[r]
  // }

  public static int comp(int a, int b) {                                                    // comparison func to count number of ops
    count++;
    if (a > b) {
      return 1;                                                                             // 1 --> greater than
    } else if (a == b) {
      return 0;                                                                             // 0 --> equal
    } else {
      return -1;                                                                            // -1 --> less than
    }
  }

  public static void swap(int[] array, int p, int q) {                                      // nothing special here
    int temp = array[p];
    array[p] = array[q];
    array[q] = temp;
  }
}
