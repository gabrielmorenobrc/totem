package totem.daemon;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface SL500Library extends Library {
    SL500Library INSTANCE = Native.load("MasterRD", SL500Library.class);

    /**
     * int WINAPI des_decrypt ( unsigned char *pSzOut,
     *  unsigned char *pSzIn,
     *  unsigned int inlen,
     *  unsigned char *pKey,
     *  unsigned int keylen)
     */

    int des_decrypt(byte[] pszOut, byte[] pSzIn, int inlen, byte[] pKey, int keylen);
    int des_encrypt(byte[] pszOut, byte[] pSzIn, int inlen, byte[] pKey, int keylen);

}
