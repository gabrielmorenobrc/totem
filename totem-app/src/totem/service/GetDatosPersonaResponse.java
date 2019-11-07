package totem.service;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement
public class GetDatosPersonaResponse implements Serializable {
    private ResultadoOperacionErp resultadoOperacion;
    private final Map<String, Object> persona = new HashMap<>();

    public ResultadoOperacionErp getResultadoOperacion() {
        return resultadoOperacion;
    }

    public void setResultadoOperacion(ResultadoOperacionErp resultadoOperacion) {
        this.resultadoOperacion = resultadoOperacion;
    }

    public Map<String, Object> getPersona() {
        return persona;
    }
}
