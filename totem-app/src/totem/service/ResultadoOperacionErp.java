package totem.service;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class ResultadoOperacionErp implements Serializable {
    private final List<ErrorErp> errores = new ArrayList<>();
    private String resultado;

    public List<ErrorErp> getErrores() {
        return errores;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
