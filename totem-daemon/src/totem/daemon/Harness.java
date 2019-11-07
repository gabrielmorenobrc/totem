package totem.daemon;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Harness {

    /**
     * 3D99017EDB08040001FEE2EE7AFD011D
     *
     * @param args
     */

    public static void main(String[] args) throws Exception {
        String rfid = "3D99017EDB08040001FEE2EE7AFD011D";
        byte[] source = new byte[16];
        for (int i = 0; i < 16; i++) {
            String s = rfid.substring(i * 2, i * 2 + 2);
            byte n = (byte) Integer.parseInt(s, 16);
            source[i] = n;
        }

        byte[] filtered = new byte[16];

        int ind = 0;
        for (int i = 0; i < source.length; i++) {
             byte b = source[i];
            filtered[ind] = b;
            ind++;
        }
        System.out.println(Arrays.toString(filtered));
        printChars(filtered);

        byte[] result = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        String key = "FF078069";
        int r = SL500Library.INSTANCE.des_decrypt(result, source, 16, key.getBytes(), key.length());
        if (r != 0) {
            throw new Exception(String.valueOf(r));
        }
        System.out.println(new String(result));
        printChars(result);
        r = SL500Library.INSTANCE.des_encrypt(result, rfid.getBytes(), 16, key.getBytes(), key.length());
        System.out.println(r);
        printChars(result);

    }

    private static void printChars(byte[] buffer) {
        System.out.println(Arrays.toString(buffer));
        for (int i = 0; i < buffer.length; i++) {
            System.out.print((char) buffer[i]);
        }
        System.out.println();
    }


    public static byte[] toDigitsBytes(String theHex) {
        byte[] bytes = new byte[theHex.length() / 2 + (((theHex.length() % 2) > 0) ? 1 : 0)];
        for (int i = 0; i < bytes.length; i++) {
            char lowbits = theHex.charAt(i * 2);
            char highbits;

            if ((i * 2 + 1) < theHex.length())
                highbits = theHex.charAt(i * 2 + 1);
            else
                highbits = '0';

            int a = (int) GetHexBitsValue((byte) lowbits);
            int b = (int) GetHexBitsValue((byte) highbits);
            bytes[i] = (byte) ((a << 4) + b);
        }

        return bytes;
    }

    public static byte GetHexBitsValue(byte ch) {
        byte sz = 0;
        if (ch <= '9' && ch >= '0')
            sz = (byte) (ch - 0x30);
        if (ch <= 'F' && ch >= 'A')
            sz = (byte) (ch - 0x37);
        if (ch <= 'f' && ch >= 'a')
            sz = (byte) (ch - 0x57);

        return sz;
    }

    public static String toHexString(byte[] bytes) {
        String hexString = "";
        for (int i = 0; i < bytes.length; i++)
            hexString += byteHEX(bytes[i]);

        return hexString;
    }

    public static String byteHEX(Byte ib) {
        String _str = "";
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F'};
        char[] ob = new char[2];
        ob[0] = Digit[(ib >> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        _str = new String(ob);

        return _str;

    }

}
