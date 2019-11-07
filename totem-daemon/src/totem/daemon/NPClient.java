package totem.daemon;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class NPClient {
    private static final Map<Integer, String> CODEMAP = loadCodeMap();

    private static Map<Integer, String> loadCodeMap() {
        Map<Integer, String> map = new HashMap<>();
        try {
            try (InputStream stream = NPClient.class.getResourceAsStream("npicodes.txt")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line = reader.readLine();
                while (line != null) {
                    String[] words = line.split(" ");
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 2; i < words.length; i++) {
                        if (i > 2) {
                            stringBuilder.append(" ");
                        }
                        stringBuilder.append(words[i]);
                    }
                    map.put(Integer.parseInt(words[1]), stringBuilder.toString());
                    line = reader.readLine();
                }
            }
            return map;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String[] enumPrinters() {
        WinDef.DWORDByReference ref = new WinDef.DWORDByReference();
        int r = NPLibrary.INSTANCE.NEnumPrinters(null, ref);
        checkResult(r);
        char[] names = new char[ref.getValue().intValue()];
        r = NPLibrary.INSTANCE.NEnumPrinters(names, ref);
        checkResult(r);
        String string = new String(names);
        String[] parts = string.split(",");
        return parts;
    }

    public String[] enumDocs(String name) {
        WinDef.DWORDByReference ref = new WinDef.DWORDByReference();
        int r = NPLibrary.INSTANCE.NEnumDoc(toPWChar(name), null);
        if (r > 0) {
            char[] names = new char[r + 1];
            r = NPLibrary.INSTANCE.NEnumDoc(toPWChar(name), names);
            checkResult(r);
            String string = new String(names);
            String[] parts = string.split(",");
            return parts;
        } else {
            return null;
        }

    }

    public void enableAutoOpen() {
        if (!NPLibrary.INSTANCE.NAutoOpen(1)) {
            throw new RuntimeException("Auto open remains false");
        }
    }

    public int resetPrinter(String name) {
        int r = NPLibrary.INSTANCE.NResetPrinter(toPWChar(name));
        checkResult(r);
        return r;
    }

    public int print(final String name, String data, WinDef.DWORDByReference jobId) throws UnsupportedEncodingException {

        Pointer pwchar = toPWChar(data);
        int r = NPLibrary.INSTANCE.NPrint(toPWChar(name), pwchar, data.toCharArray().length * 2 + 1, null);
        checkResult(r);
        return r;
    }

    public int dprint(final String name, byte[] data, WinDef.DWORDByReference jobId) throws UnsupportedEncodingException {
        int r = NPLibrary.INSTANCE.NDPrint(toPWChar(name), data, data.length, jobId);
        checkResult(r);
        return r;
    }

    private void checkResult(int result) {
        if (result < 0) {
            throw new RuntimeException(result + ": " + CODEMAP.get(result));
        } else if (result > 0) {
            System.out.println(result + ": " + CODEMAP.get(result));
        }
    }

    public int openPrinter(final String name) throws UnsupportedEncodingException {
        Pointer pointer = toPWChar(name);
        int r = NPLibrary.INSTANCE.NOpenPrinter(pointer, (byte) 0, null);
        checkResult(r);
        return r;
    }

    private Pointer toPWChar(String string) {
        try {
            char[] data = string.toCharArray();
            int l = data.length;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-16LE");
            writer.write(string);
            writer.flush();
            os.flush();
            byte[] bytes = os.toByteArray();
            l = bytes.length;
            Pointer pointer = new Memory(l + 1);
            pointer.write(0, bytes, 0, l);
            pointer.setByte(l, (byte) 0);
            return pointer;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int closePrinter(final String name) {
        int r = NPLibrary.INSTANCE.NClosePrinter(toPWChar(name));
        checkResult(r);
        return r;
    }

    public int closePrinters() {
        return NPLibrary.INSTANCE.NClosePrinters();
    }

    public int startDoc(final String name, WinDef.DWORDByReference jobId) {
        WinDef.DWORDByReference ref = new WinDef.DWORDByReference();
        int r = NPLibrary.INSTANCE.NStartDoc(toPWChar(name), ref);
        if (r == -72) {
            Logger.getLogger("Document already opened. Closing.");
            NPLibrary.INSTANCE.NEndDoc(toPWChar(name));
            r = NPLibrary.INSTANCE.NStartDoc(toPWChar(name), ref);
        }
        checkResult(r);
        jobId.setValue(jobId.getValue());
        return r;
    }


    public int endDoc(final String name) {
        int r = NPLibrary.INSTANCE.NEndDoc(toPWChar(name));
        checkResult(r);
        return r;
    }

    public int getStatus(final String name, Integer[] status) {
        WinDef.DWORDByReference ref = new WinDef.DWORDByReference();
        int r = NPLibrary.INSTANCE.NGetStatus(toPWChar(name), ref);
        if (r == 0) {
            status[0] = ref.getValue().intValue();
        }
        return r;
    }

    public void setCallback(NPLibrary.NCallback nCallback) {
        NPLibrary.INSTANCE.NSetCallback(nCallback);
    }

    public static String codeMessage(int code) {
        return CODEMAP.get(code);
    }

    public static String statusString(int status) {
        if (status == 0) {
            return "Ready";
        }
        StringBuilder stringBuilder = new StringBuilder();
        if ((status & 1) == 1) {
            stringBuilder.append("Paper near end");
        }
        if ((status & 2) == 2) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("Head open");
        }
        if ((status & 4) == 4) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("Paper end");
        }
        if ((status & 8) == 8) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("Head temp error");
        }
        if ((status & 16) == 16) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("Bit 4");
        }
        if ((status & 32) == 32) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("Bit 5");
        }
        if ((status & 64) == 64) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("Ticket sin eyectar");
        }
        if ((status & 128) == 128) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("Print start");
        }
        return stringBuilder.toString();
    }


}
