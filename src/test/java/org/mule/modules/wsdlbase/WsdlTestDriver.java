package org.mule.modules.wsdlbase;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.wsdlbase.api.InvalidTokenException;
import org.mule.modules.wsdlbase.api.NoSuchEntityException;
import org.mule.modules.wsdlbase.api.SessionExpiredException;
import org.mule.modules.wsdlbase.config.WsdlConfig;

public class WsdlTestDriver {

    @Test
    public void get() throws ConnectionException, InvalidTokenException, NoSuchEntityException, SessionExpiredException {
        WsdlBaseConnector connector = new WsdlBaseConnector();
        WsdlConfig config = new WsdlConfig();
        config.setServiceAddress("http://devkit-cookbook.cloudhub.io/soap");
        config.connect("admin", "admin");
        connector.setConfig(config);

        assertNotNull(connector.get(1));
    }
}
