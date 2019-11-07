package totem.daemon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Date;

public class TicketHarness {

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        WebResource resource = client.resource("http://192.168.20.212:5987/ticket");
        PrintTicketData data = new PrintTicketData();
        data.setCategoria("TRÁNSITO Y TRANSPORTE");
        data.setSubcategoria("ESTACIONAMIENTO");
        data.setTramite("PERMISO");
        data.setSede("BILLINGHURST");
        data.setFecha(new Date());
        data.setNumero("CER1234");
        PrintTicketResult result = resource.type(MediaType.APPLICATION_JSON_TYPE).post(PrintTicketResult.class, data);
        new ObjectMapper().writeValue(System.out, result);
        System.out.println();
    }
}
