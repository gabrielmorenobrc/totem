package totem.daemon;

import com.sun.jna.platform.win32.WinDef;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketPrinter {
    private static final TicketPrinter INSTANCE = new TicketPrinter();
    private NPClient client;
    private String name;

    public static TicketPrinter get() {
        return INSTANCE;
    }

    public void init() {
        client = new NPClient();
        client.setCallback(new NCallbackImpl());
        String[] names = client.enumPrinters();
        for (String Pname:names) {
            if(Pname.contains("NPI")){
                name = Pname;
                break;
            }
        }
    }

    public void printTicket(PrintTicketData data) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd'/'MM'/'yyyy HH':'mm");

        String numero = magnify(data.getNumero().toUpperCase());

        String template = "1b6803\n" +
                "1b7405\n" +
                "0a\n" +
                "0a\n" +
                "1b6101\n" +
                "\"" + dateFormat.format(data.getFecha()) + "\"\n" +
                "0a\n" +
                "1b21001C21001b\"E\"01\n" +
                "\"" + data.getSede().toUpperCase() + "\n" +
                "0a\n" +
                "1b21001C21001b\"E\"01\n" +
                "\"" + data.getCategoria().toUpperCase() + "\"\n" +
                "0a\n" +
                "1b21001C21001b\"E\"01\n" +
                "\"" + data.getSubcategoria().toUpperCase() + "\"\n" +
                "0a\n" +
                "1b21001C21001b\"E\"01\n" +
                "\"UD. SERÁ LLAMADO CON EL NÚMERO\"\n" +
                "0a\n" +
                "1b240A02\n" +
                numero + "\n" +
                "0a\n" +
                "0a\n" +
                "\"" + data.getTramite().toUpperCase() + "\"\n" +
                "0a0a0a0a0a0a\n" ;


        int status = waitForStatus();
        if (status != 0) {
            throw new Exception("Error en la impresora: " + status + " - " + NPClient.statusString(status));
        }

        WinDef.DWORDByReference jobId = new WinDef.DWORDByReference();
        client.startDoc(name, jobId);

        String charset = "CP1252";

        BufferedReader reader = new BufferedReader(new StringReader(template));
        String line = reader.readLine();
        while (line != null) {
            if (!line.trim().isEmpty()) {
                byte[] bytes = new NPData(charset).writeMixed(line).toBytes();
                client.dprint(name, bytes , jobId);
                //                client.print(name, line , jobId);
            }
            line = reader.readLine();
        }
        client.endDoc(name);
    }

    private String magnify(String s) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("1c6b11");
        for (int i = 0; i < s.length(); i++) {
            String hex = String.format("%02x", (int) s.charAt(i));
            buffer.append(hex);
        }
        buffer.append("00");
        return buffer.toString();
    }

    private int waitForStatus() throws Exception {
        Integer[] status = new Integer[]{0};
        int n = 0;
        int r = client.getStatus(name, status);
        while (r != 0 || status[0] != 0) {
            n++;
            if (n == 40) {
                break;
            }
            int v = status[0];
            if ((v & 64) == 64) {
                eject();
            }
            Thread.sleep(1000);

            r = client.getStatus(name, status);
        }
        if (r != 0) {
            throw new Exception("Error de lectura impresora: " + r + " - " + NPClient.codeMessage(r));
        }
        return status[0];
    }

    private void eject() throws IOException {
        WinDef.DWORDByReference jobId = new WinDef.DWORDByReference();
        client.startDoc(name, jobId);
        byte[] bytes = new NPData("US-ASCII").writeMixed("1b7200").toBytes();
        client.dprint(name, bytes
                , jobId);
        client.endDoc(name);
    }

    public void checkStatus() throws Exception {
        int status = waitForStatus();
        if (status != 0) {
            throw new Exception(status + " - " + NPClient.statusString(status));
        }
    }
}
