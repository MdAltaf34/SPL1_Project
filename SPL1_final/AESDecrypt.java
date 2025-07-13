
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class AESDecrypt {
    static int[][] state=new int[4][4];

     int[] w;
     AESDecrypt(int[] w){
       this.w=w;
     }

     int[] rsBox = new int[] {
            0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb,
            0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb,
            0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e,
            0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25,
            0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92,
            0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84,
            0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06,
            0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b,
            0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73,
            0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e,
            0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b,
            0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4,
            0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f,
            0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef,
            0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61,
            0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d };
    

    public byte[] dec(byte[] text) {
        byte[] out = new byte[text.length];
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[j][i] = text[i * 4 + j] & 0xff;
            }
        }

        state = decipher(state);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                out[i * 4 + j] = (byte) (state[j][i] & 0xff);
            }
        }
        return out;

    }
    public int[][] decipher(int[][] s) {
        s = addRoundKey(s, 10);
        for (int i = 9; i > 0; i--) {
            s = invShiftRows(s);
            s = invSubBytes(s);
            s = addRoundKey(s, i);
            s = invMixColumnas(s);
        }
        s = invShiftRows(s);
        s = invSubBytes(s);
        s = addRoundKey(s, 0);
        return s;
    }
    
    public int[][] addRoundKey(int[][] s, int r) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                s[j][i] = s[j][i] ^ ((w[r * 4 + i] << (r * 8)) >>> 24);
            }
        }
        return s;
    }
    
    public int invSubWord(int word) {
        int subWord = 0;
        for (int i = 24; i >= 0; i -= 8) {
            int in = word << i >>> 24;
            subWord |= rsBox[in] << (24 - i);
        }
        return subWord;
    }
    
    public int[][] invSubBytes(int[][] state) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = invSubWord(state[i][j]) & 0xFF;
            }
        }
        return state;
    }

    public int[][] invMixColumnas(int[][] state) {
        int temp0, temp1, temp2, temp3;
        for (int c = 0; c < 4; c++) {
            temp0 = mult(0x0e, state[0][c]) ^ mult(0x0b, state[1][c]) ^ mult(0x0d, state[2][c])
                    ^ mult(0x09, state[3][c]);
            temp1 = mult(0x09, state[0][c]) ^ mult(0x0e, state[1][c]) ^ mult(0x0b, state[2][c])
                    ^ mult(0x0d, state[3][c]);
            temp2 = mult(0x0d, state[0][c]) ^ mult(0x09, state[1][c]) ^ mult(0x0e, state[2][c])
                    ^ mult(0x0b, state[3][c]);
            temp3 = mult(0x0b, state[0][c]) ^ mult(0x0d, state[1][c]) ^ mult(0x09, state[2][c])
                    ^ mult(0x0e, state[3][c]);

            state[0][c] = temp0;
            state[1][c] = temp1;
            state[2][c] = temp2;
            state[3][c] = temp3;
        }
        return state;
    }
    
    public static int mult(int a, int b) {
        int sum =AESEncrypt.mult(a, b);
        return sum;
    }
    
    public static int[][] invShiftRows(int[][] state) {
        int temp1, temp2, temp3, i;

        temp1 = state[1][3];
        for (i = 3; i > 0; i--) {
            state[1][i] = state[1][(i - 1) % 4];
        }
        state[1][0] = temp1;

        temp1 = state[2][3];
        temp2 = state[2][2];
        for (i = 3; i > 1; i--) {
            state[2][i] = state[2][(i - 2) % 4];
        }
        state[2][1] = temp1;
        state[2][0] = temp2;

        temp1 = state[3][1];
        temp2 = state[3][2];
        temp3 = state[3][3];
        state[3][3] = state[3][0];

        state[3][0] = temp1;
        state[3][1] = temp2;
        state[3][2] = temp3;

        return state;
    }

    public  void decrypt() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter the file path to read encrypted data: ");
            String filePath = sc.nextLine();

            InputStream in = new FileInputStream(filePath);
            System.out.print("Enter the file path to write decrypted data: ");
            filePath = sc.nextLine();

            OutputStream out = new FileOutputStream(filePath);

            byte[] plain;
            byte[] cipher = new byte[16];
            while (in.read(cipher) != -1) {
                plain = dec(cipher);
                out.write(plain);
            }

            plain = dec(cipher);
            out.write(plain, 0, 16 - plain[15]);
            in.close();
            out.close();
            System.out.println("\nDecryption Successful\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File Not Found");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
