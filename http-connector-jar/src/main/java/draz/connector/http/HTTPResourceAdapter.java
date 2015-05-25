package draz.connector.http;

import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ConfigProperty;
import javax.resource.spi.Connector;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.TransactionSupport;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Connector(reauthenticationSupport = false, transactionSupport = TransactionSupport.TransactionSupportLevel.NoTransaction)
public class HTTPResourceAdapter implements ResourceAdapter, java.io.Serializable {

    /** The serial version UID */
    private static final long serialVersionUID = 1L;

    /** The logger */
    private static Logger logger = LoggerFactory.getLogger(HTTPResourceAdapter.class);

    /**
     * Resource adapter specific settings
     */
    /** name */
    @ConfigProperty(defaultValue = "HTTP Resource Adapter")
    private String name;

    /** version */
    @ConfigProperty(defaultValue = "1.0")
    private String version;

    @Override
    public void endpointActivation(final MessageEndpointFactory endpointFactory, final ActivationSpec spec)
            throws ResourceException {
        logger.debug("endpointActivation()");

    }

    @Override
    public void endpointDeactivation(final MessageEndpointFactory endpointFactory, final ActivationSpec spec) {
        logger.debug("endpointDeactivation()");

    }

    @Override
    public XAResource[] getXAResources(final ActivationSpec[] specs) throws ResourceException {
        logger.debug("getXAResources()");
        return new XAResource[] {};

    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
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
        final HTTPResourceAdapter other = (HTTPResourceAdapter) obj;
        return equalsHTTPResourceAdapter(other);
    }

    private boolean equalsHTTPResourceAdapter(final HTTPResourceAdapter other) {
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (version == null) {
            if (other.version != null) {
                return false;
            }
        } else if (!version.equals(other.version)) {
            return false;
        }
        return true;
    }

    @Override
    public void start(BootstrapContext ctx) throws ResourceAdapterInternalException {
        logger.info("HTTP Resource Adapter start!");
    }

    @Override
    public void stop() {
        logger.info("HTTP Resource Adapter stop!");
    }
}
