
import java.util.Scanner;

public class AESKey {
     int[] key;
     int[] w;
    public int[] key() {
    Scanner sc = new Scanner(System.in);
    String k;
    do {
      System.out.print("Enter the 16 character key: ");
      k = sc.nextLine();
      
      if (k.length() != 16) {
          System.out.println("Key must be exactly 16 characters.");
          continue;
      }
      break;
    } while (true);

        byte[] aeskey = k.getBytes();

      key = new int[aeskey.length];

        for (int i = 0; i < aeskey.length; i++) {
            key[i] = aeskey[i];
        }
        
        w = new int[44];
      KeyExpand Ke=new KeyExpand(key);
        w=Ke.expandKey();  
       return w;
    }
}
