package totem.service;

import java.io.Serializable;
import java.util.Properties;

public interface TotemConfig extends Serializable {

    Properties getPersistenceProperties();

    String getErpBaseUrl();

    String getTurneroUriBase();


}
