package draz.connector.http;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.resource.spi.ConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import draz.api.IRequest;

public class HTTPConnectionFactoryImpl implements HTTPConnectionFactory {
    private static final long serialVersionUID = 1L;

    private static Logger logger = LoggerFactory.getLogger(HTTPConnectionFactoryImpl.class);

    private Reference reference;

    private HTTPManagedConnectionFactory mcf;

    private ConnectionManager connectionManager;

    public HTTPConnectionFactoryImpl() {

    }

    public HTTPConnectionFactoryImpl(final HTTPManagedConnectionFactory mcf, final ConnectionManager cxManager) {
        logger.debug("HTTPConnectionFactoryImpl.HTTPConnectionFactoryImpl()");
        this.mcf = mcf;
        this.connectionManager = cxManager;
    }

    @Override
    public IRequest getConnection() throws Exception {
        logger.debug("getConnection()");
        return (IRequest) connectionManager.allocateConnection(mcf, null);
    }

    @Override
    public void setReference(final Reference reference) {
        this.reference = reference;

    }

    @Override
    public Reference getReference() throws NamingException {
        return this.reference;
    }
}