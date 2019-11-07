package totem.daemon;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;

public interface NPLibrary extends Library {
    NPLibrary INSTANCE = Native.load("NPrinterLib", NPLibrary.class);

    int NResetPrinter(String name);

    public interface NCallback extends Callback {
        void invoke(Pointer name, int callbackType, int result1, int result2);
    }

    int NEnumPrinters(char[] names, WinDef.DWORDByReference size);
    int NEnumDoc(Pointer name, char[] names);
    int NResetPrinter(Pointer name);
    boolean NAutoOpen(int flag);
    int NOpenPrinter(Pointer name, byte statusFlag, Pointer callback);
    int NClosePrinter(Pointer name);
    int NPrint(Pointer name, Pointer data, int dataLen, WinDef.DWORDByReference jobId);
    int NDPrint(Pointer name, byte[] data, int dataLen, WinDef.DWORDByReference jobId);
    int NClosePrinters();
    void NSetCallback(NCallback func);
    int NStartDoc(Pointer name, WinDef.DWORDByReference jobId);
    int NEndDoc(Pointer name);
    int NGetStatus(Pointer name, WinDef.DWORDByReference ref);
}
