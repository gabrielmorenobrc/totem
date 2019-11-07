package totem.service;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorErp {
    private String codError;
    private String descripError;

    public String getCodError() {
        return codError;
    }

    public void setCodError(String codError) {
        this.codError = codError;
    }

    public String getDescripError() {
        return descripError;
    }

    public void setDescripError(String descripError) {
        this.descripError = descripError;
    }


    @Override
    public String toString() {
        return "ErrorErp{" +
                "codError='" + codError + '\'' +
                ", descripError='" + descripError + '\'' +
                '}';
    }
}
