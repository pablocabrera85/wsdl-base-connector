package org.mule.modules.wsdlbase.config;

import javax.xml.ws.BindingProvider;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.ConnectionIdentifier;
import org.mule.api.annotations.Disconnect;
import org.mule.api.annotations.TestConnectivity;
import org.mule.api.annotations.ValidateConnection;
import org.mule.api.annotations.components.ConnectionManagement;
import org.mule.api.annotations.display.Password;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.annotations.param.Default;
import org.mule.modules.wsdlbase.api.IMuleCookBookService;
import org.mule.modules.wsdlbase.api.InvalidCredentialsException;
import org.mule.modules.wsdlbase.api.Login;

@ConnectionManagement(configElementName = "config-type", friendlyName = "Connection Managament type strategy")
public class WsdlConfig {

    /**
     * Endpoint for interface IMuleCookBookService
     * 
     */
    @Configurable
    @Default("http://devkit-cookbook.cloudhub.io/soap")
    private java.lang.String serviceAddress;
    /**
     * Interface to send requests using IMuleCookBookService
     * 
     */
    private IMuleCookBookService wsdlService;

    private String token;

    /**
     * Connect
     * 
     * @param username
     *            A username
     * @param password
     *            A password
     * @throws ConnectionException
     *             When the call fails
     */
    @Connect
    @TestConnectivity
    public void connect(@ConnectionKey java.lang.String username, @Password java.lang.String password) throws ConnectionException {
        if (getWsdlService() == null) {
            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
            factory.setServiceClass((org.mule.modules.wsdlbase.api.IMuleCookBookService.class));
            setWsdlService(((IMuleCookBookService) factory.create()));
            ((BindingProvider) getWsdlService()).getRequestContext().put((BindingProvider.ENDPOINT_ADDRESS_PROPERTY), getServiceAddress());

            Login parameters = new Login();
            parameters.setAccountId(username);
            parameters.setPassword(password);
            try {
                this.token = getWsdlService().login(parameters).getReturn();
            } catch (InvalidCredentialsException e) {
                throw new ConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS, "INVALID_CREDENTIALS", "Invalid username and password");
            }
        }
    }

    /**
     * Disconnect
     * 
     */
    @Disconnect
    public void disconnect() {
        setWsdlService(null);
    }

    /**
     * Are we connected
     * 
     */
    @ValidateConnection
    public boolean isConnected() {
        return getWsdlService() != null;
    }

    /**
     * Connection Id
     * 
     */
    @ConnectionIdentifier
    public String connectionId() {
        return "001";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public IMuleCookBookService getWsdlService() {
        return wsdlService;
    }

    public void setWsdlService(IMuleCookBookService wsdlService) {
        this.wsdlService = wsdlService;
    }

    public java.lang.String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(java.lang.String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }
}
