package draz.connector.http;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTTPManagedConnection implements ManagedConnection {

    private static Logger logger = LoggerFactory.getLogger(HTTPManagedConnection.class);

    private static final String CONN_LISTENER_IS_NULL = "Connection listener is null";

    private PrintWriter logwriter;

    private final HTTPManagedConnectionFactory mcf;

    private final List<ConnectionEventListener> listeners;

    private HTTPConnectionImpl connection;

    private HTTPManagedConnectionMetaData metaData;

    public HTTPManagedConnection(final HTTPManagedConnectionFactory mcf, final String user) {
        this.mcf = mcf;
        this.logwriter = null;
        this.listeners = Collections.synchronizedList(new ArrayList<ConnectionEventListener>(1));
        this.connection = null;
    }

    @Override
    public Object getConnection(final Subject subject, final ConnectionRequestInfo cxRequestInfo)
            throws ResourceException {
        logger.debug("getConnection()");
        connection = new HTTPConnectionImpl(this, mcf);
        return connection;
    }

    public HTTPConnectionImpl getConnection() {
        return connection;
    }

    @Override
    public void associateConnection(final Object connection) throws ResourceException {
        logger.debug("associateConnection()");

        if (connection == null) {
            throw new ResourceException("Null connection handle");
        }

        if (!(connection instanceof HTTPConnectionImpl)) {
            throw new ResourceException("Wrong connection handle");
        }

        this.connection = (HTTPConnectionImpl) connection;
    }

    @Override
    public void cleanup() throws ResourceException {
        logger.debug("cleanup() method called");
    }

    @Override
    public void destroy() throws ResourceException {
        logger.debug("destroy() method called");
        if (connection != null) {
            connection.closeHTTPConnection();
        }
        this.connection = null;
        this.metaData = null;
    }

    void closeHandle(final HTTPConnectionImpl handle) {
        final ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.CONNECTION_CLOSED);
        logger.debug("closeHandle has been called...");
        event.setConnectionHandle(handle);
        for (final ConnectionEventListener cel : listeners) {
            logger.debug("cel.connectionClosed(event)");
            cel.connectionClosed(event);
        }
        logger.debug("closeHandle method complete");
    }

    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        logger.debug("getLogWriter()");
        return logwriter;
    }

    @Override
    public void setLogWriter(final PrintWriter out) throws ResourceException {
        logger.debug("setLogWriter()");
        logwriter = out;
    }

    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        throw new NotSupportedException("GetXAResource not supported");
    }

    /**
     * Returns an <code>javax.transaction.xa.XAresource</code> instance.
     * 
     * @return XAResource instance
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public XAResource getXAResource() throws ResourceException {
        throw new NotSupportedException("GetXAResource not supported");
    }

    /**
     * Gets the metadata information for this connection's underlying EIS resource manager instance.
     * 
     * @return ManagedConnectionMetaData instance
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        logger.debug("getMetaData()");
        this.metaData = new HTTPManagedConnectionMetaData(this);
        return this.metaData;
    }

    @Override
    public void addConnectionEventListener(final javax.resource.spi.ConnectionEventListener listener) {
        logger.debug("addConnectionEventListener()");
        if (listener == null) {
            throw new IllegalArgumentException(CONN_LISTENER_IS_NULL);
        }
        this.listeners.add(listener);
    }

    @Override
    public void removeConnectionEventListener(final javax.resource.spi.ConnectionEventListener listener) {
        logger.debug("removeConnectionEventListener()");
        if (listener == null) {
            throw new IllegalArgumentException(CONN_LISTENER_IS_NULL);
        }
        this.listeners.remove(listener);
    }

}
