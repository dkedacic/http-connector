package draz.connector.http;

import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnectionMetaData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTTPManagedConnectionMetaData implements ManagedConnectionMetaData {
    private static Logger logger = LoggerFactory.getLogger(HTTPManagedConnectionMetaData.class);
    
    private final HTTPManagedConnection managedConnection;
    
    private static final String EIS_PRODUCT_NAME = "HTTP Rar";
    private static final String EIS_PRODUCT_VERSION = "1.0.1";
    private static final int MAX_CONNECTIONS = 20;
    
    public HTTPManagedConnectionMetaData(final HTTPManagedConnection managedConnection) {
        this.managedConnection = managedConnection;
    }

    /**
     * Returns Product name of the underlying EIS instance connected through the ManagedConnection.
     * 
     * @return Product name of the EIS instance
     * @throws ResourceException
     *             Thrown if an error occurs
     */
    @Override
    public String getEISProductName() throws ResourceException {
        logger.debug("getEISProductName()");
        return EIS_PRODUCT_NAME;
    }

    /**
     * Returns Product version of the underlying EIS instance connected through the ManagedConnection.
     * 
     * @return Product version of the EIS instance
     * @throws ResourceException
     *             Thrown if an error occurs
     */
    @Override
    public String getEISProductVersion() throws ResourceException {
        logger.debug("getEISProductVersion()");
        return EIS_PRODUCT_VERSION;
    }

    /**
     * Returns maximum limit on number of active concurrent connections
     * 
     * @return Maximum limit for number of active concurrent connections
     * @throws ResourceException
     *             Thrown if an error occurs
     */
    @Override
    public int getMaxConnections() throws ResourceException {
        logger.debug("getMaxConnections()");
        return MAX_CONNECTIONS;
    }

    /**
     * Returns name of the user associated with the ManagedConnection instance
     * 
     * @return Name of the user
     * @throws ResourceException
     *             Thrown if an error occurs
     */
    @Override
    public String getUserName() throws ResourceException {
        logger.debug("getUserName()");
        return "noUserNameUsed";
    }

}
