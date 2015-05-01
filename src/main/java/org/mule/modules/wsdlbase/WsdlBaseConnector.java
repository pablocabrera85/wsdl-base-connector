/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.wsdlbase;

import org.mule.api.annotations.ConnectionStrategy;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.ReconnectOn;
import org.mule.api.annotations.param.Default;
import org.mule.modules.wsdlbase.api.CookBookEntity;
import org.mule.modules.wsdlbase.api.Get;
import org.mule.modules.wsdlbase.api.InvalidTokenException;
import org.mule.modules.wsdlbase.api.NoSuchEntityException;
import org.mule.modules.wsdlbase.api.SessionExpiredException;
import org.mule.modules.wsdlbase.config.WsdlConfig;

/**
 * Anypoint Wsdl Base Connector.
 * 
 * @author MuleSoft, Inc.
 * 
 */
@Connector(name = "wsdl-base", friendlyName = "WsdlBase", schemaVersion = "1.0")
public class WsdlBaseConnector {

    @ConnectionStrategy
    private WsdlConfig config;

    /**
     * Retrieves an entity by Id
     * 
     * {@sample.xml ../../../doc/wsdl-base-connector.xml.sample wsdl-base:get}
     * 
     * @param id Entity identifier
     *
     * @return the Entity matching the provided id.
     * @throws SessionExpiredException then the session is no longer valid
     * @throws InvalidTokenException when the token provided is not valid (this should never happen as the token is provided by the API)
     * @throws NoSuchEntityException When the provided ID doesn't exists.
     */
    @Processor
    @ReconnectOn(exceptions = { SessionExpiredException.class })
    public CookBookEntity get(@Default("1") Integer id) throws InvalidTokenException, NoSuchEntityException, SessionExpiredException {
        Get get = new Get();
        get.setId(id);
        return config.getWsdlService().get(get, config.getToken()).getReturn();
    }

    public WsdlConfig getConfig() {
        return config;
    }

    public void setConfig(WsdlConfig config) {
        this.config = config;
    }

}
