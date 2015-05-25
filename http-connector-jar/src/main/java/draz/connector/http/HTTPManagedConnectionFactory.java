package draz.connector.http;

import java.io.PrintWriter;
import java.util.Set;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionDefinition;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterAssociation;
import javax.security.auth.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import draz.api.IRequest;

@ConnectionDefinition(connectionFactory = HTTPConnectionFactory.class, connectionFactoryImpl = HTTPConnectionFactoryImpl.class, connection = IRequest.class, connectionImpl = HTTPConnectionImpl.class)
public class HTTPManagedConnectionFactory implements ManagedConnectionFactory, ResourceAdapterAssociation {

    private static final long serialVersionUID = 1572226667248082543L;

    private static Logger logger = LoggerFactory.getLogger(HTTPManagedConnectionFactory.class);

    private ResourceAdapter resourceAdapter;

    private PrintWriter logwriter;

    @Override
    public ResourceAdapter getResourceAdapter() {
        logger.debug("getResourceAdapter()");
        return resourceAdapter;
    }

    @Override
    public void setResourceAdapter(ResourceAdapter resourceAdapter) throws ResourceException {
        logger.debug("setResourceAdapter()");
        this.resourceAdapter = resourceAdapter;

    }

    @Override
    public Object createConnectionFactory(ConnectionManager cxManager) throws ResourceException {
        logger.debug("createConnectionFactory()");
        return new HTTPConnectionFactoryImpl(this, cxManager);
    }

    @Override
    public Object createConnectionFactory() throws ResourceException {
        throw new ResourceException("This resource adapter doesn't support non-managed environments");
    }

    @Override
    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo cxRequestInfo)
            throws ResourceException {
        logger.debug("createManagedConnection()");
        return new HTTPManagedConnection(this, null);
    }

    @Override
    public ManagedConnection matchManagedConnections(Set connectionSet, Subject subject,
            ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        logger.debug("matchManagedConnections()");

        for (final Object mgdConnection : connectionSet) {
            final ManagedConnection managedConnection = (ManagedConnection) mgdConnection;
            if (managedConnection instanceof HTTPManagedConnection) {
                final HTTPConnectionImpl httpConx = ((HTTPManagedConnection) managedConnection).getConnection();
                if (httpConx != null) {
                    return managedConnection;
                }
            }
        }
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws ResourceException {
        logger.debug("setLogWriter()");
        logwriter = out;
    }

    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        logger.debug("getLogWriter()");
        return logwriter;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((resourceAdapter == null) ? 0 : resourceAdapter.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HTTPManagedConnectionFactory other = (HTTPManagedConnectionFactory) obj;
        if (resourceAdapter == null) {
            if (other.resourceAdapter != null) {
                return false;
            }
        } else if (!resourceAdapter.equals(other.resourceAdapter)) {
            return false;
        }
        return true;
    }
}
