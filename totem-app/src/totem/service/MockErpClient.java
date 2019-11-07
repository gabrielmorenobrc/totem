package totem.service;

import javax.enterprise.inject.Alternative;

@Alternative
public class MockErpClient extends ErpClient {

    @Override
    public DatosPersona getDatosPersona(Integer tipoDocumento, String numeroDocumento) {
        DatosPersona datosPersona = new DatosPersona();
        datosPersona.setNombre("Test");
        datosPersona.setNombre("Test");
        return datosPersona;
    }
}
