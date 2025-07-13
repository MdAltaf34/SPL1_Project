import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean flag=true;
        AESKey k=new AESKey();
        int[] key;
        do {
            
            System.out.print("For AES encryption enter the command enc\nFor AES decryption enter the command dec\nTo terminate enter the command exit\nEnter your command: ");
        String x=sc.nextLine();
        switch (x) {
            case "enc":
                {
             key=k.key();
            AESEncrypt enc=new AESEncrypt(key);
             enc.encrypt();
                break;
            }
        case "dec":{
              key=k.key();
            AESDecrypt dec=new AESDecrypt(key);
            dec.decrypt();
               break;
        }
        case "exit":{
       
           flag=false;
             break;
        }
               default:
            {
                System.out.println("Invalid command");
            }
                break;
        }
     
        
        } while (flag);

    }
}
