package totem.daemon;

import java.io.*;

public class NPData {
    private ByteArrayOutputStream outputStream;
    private Writer writer;


    public NPData(String outputCharset) {
        outputStream = new ByteArrayOutputStream();
        try {
            writer = new OutputStreamWriter(outputStream, outputCharset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public NPData writeString(String value) throws IOException {
        writer.write(value);
        writer.flush();
        outputStream.flush();
        return this;
    }

    public NPData writeHex(String value) throws IOException {
        int c = Integer.parseInt(value, 16);
        outputStream.write(c);
        outputStream.flush();
        return this;
    }

    public NPData writeMixed(String value) throws IOException {
        boolean escaped = false;
        boolean string = false;
        char[] buf = new char[2];
        int pos = -1;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (escaped) {
                writer.write(c);
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '"') {
                string = !string;
            } else if (string) {
                writer.write(c);
            } else if (c == 32) {
                writer.flush();
                outputStream.write(c);
            } else if (pos == -1){
                pos++;
                buf[pos] = c;
            } else if (pos == 0) {
                pos++;
                buf[pos] = c;
                int code = Integer.parseInt(new String(buf), 16);
                writer.flush();
                outputStream.write(code);
                pos = -1;
            } else {
                System.out.println("wtf");
            }
        }
        writer.flush();
        outputStream.flush();
        return this;
    }

    public byte[] toBytes() throws IOException {
        writer.flush();
        outputStream.flush();
        return outputStream.toByteArray();
    }


    public static void main(String[] args) throws IOException {
        NPData npData = new NPData("UTF-8");
        npData.writeMixed("\"Hola\"0a\"día\"0a0a");
        System.out.write(npData.toBytes());
    }


}
