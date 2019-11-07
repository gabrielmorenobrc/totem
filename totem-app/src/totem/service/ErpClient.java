package totem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Alternative
public class ErpClient implements Serializable {
    @Inject
    private TotemConfig totemConfig;

    public DatosPersona getDatosPersona(Integer tipoDocumento, String numeroDocumento) throws IOException {
        String url = totemConfig.getErpBaseUrl() + "/AltaPersona/GetDatosPersona.ashx";

        Client client = Client.create();
        WebResource resource = client.resource(url);
        GetDatosPersonaData data = new GetDatosPersonaData();
        data.setTipoDocumento(tipoDocumento);
        data.setNroDocumento(numeroDocumento);
        WebResource.Builder builder = resource.header("Content-Type", "application/json");
        String string = builder.post(String.class, data);
        ObjectMapper objectMapper = new ObjectMapper();
        GetDatosPersonaResponse response = objectMapper.readValue(string.getBytes(), GetDatosPersonaResponse.class);
        if (response.getResultadoOperacion().getResultado().equals("Error")) {
            Logger.getLogger(ErpClient.class.getName()).log(Level.WARNING, tipoDocumento + " " + numeroDocumento +
            ": " + Arrays.toString(response.getResultadoOperacion().getErrores().toArray()));
            throw new RuntimeException("Error " + response.getResultadoOperacion().getResultado());
        }
        DatosPersona datosPersona = new DatosPersona();
        datosPersona.setApellido(response.getPersona().get("apellido").toString());
        datosPersona.setNombre(response.getPersona().get("nombre").toString());
        datosPersona.setTipoDocumento(response.getPersona().get("tipodocumento").toString());
        datosPersona.setNumeroDocumento(response.getPersona().get("documento").toString());
        return datosPersona;
    }

}
