package draz.connector.http;

import inbound.http.server.startup.HTTPConnectorConfigurationBean;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.naming.InitialContext;
import javax.resource.ResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import draz.api.IRequest;
import draz.api.RequestFailedException;

public class HTTPConnectionImpl implements IRequest {

    private static Logger logger = LoggerFactory.getLogger(HTTPConnectionImpl.class);

    private final HTTPManagedConnection managedConnection;

    HttpURLConnection con = null;

    private HTTPManagedConnectionFactory managedConnectionFactory;
    private final String USER_AGENT = "Mozilla/5.0";

    private String ENDPOINT = "---";
    
    HTTPConnectorConfigurationBean configuration;

    private final String RESS = "<soap:Envelope " + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" "
            + "soap:encodingStyle=\"http://www.w3.org/2001/12/soap-encoding\">"
            + "<soap:Body xmlns:m=\"http://www.example.org/stock\">" + "<m:GetStockPriceResponse>"
            + "<m:Price>34.5</m:Price>" + "</m:GetStockPriceResponse>" + "</soap:Body></soap:Envelope>";

    public HTTPConnectionImpl(final HTTPManagedConnection managedConnection,
            final HTTPManagedConnectionFactory managedConnectionFactory) throws ResourceException {
        logger.debug("In HTTPConnectionImpl constructor");
        this.managedConnection = managedConnection;
        this.setMcf(managedConnectionFactory);
        
        
        try {
            InitialContext ic = new InitialContext();
            configuration = (HTTPConnectorConfigurationBean) ic
                    .lookup("java:global/http-connector-ear/http-connector-configuration-ejb-1.0.1/HTTPConnectorConfigurationBean!inbound.http.server.startup.HTTPConnectorConfigurationBean");
            ENDPOINT = configuration.getHTTPConnectorConfiguration().getOutboundAddress();
            logger.debug("Reading configuration: {}", ENDPOINT);
        } catch (Exception e) {
            logger.error("Exception while get endpoint address.", e);
            throw new ResourceException(e);
        }
    }

    public HTTPManagedConnectionFactory getMcf() {
        return managedConnectionFactory;
    }

    private void setMcf(final HTTPManagedConnectionFactory managedConnectionFactory) {
        this.managedConnectionFactory = managedConnectionFactory;
    }

    @Override
    public String executeRequest(String request) throws RequestFailedException {
        StringBuffer response = new StringBuffer();

        URL obj;
        try {
            obj = new URL(ENDPOINT);
            con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        } catch (Exception e1) {
            if (con != null) {
                closeHTTPConnection();
            }
            throw new RequestFailedException(e1);
        }

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream());) {

            String encodedData = URLEncoder.encode(request, "UTF-8");

            // Send post request
            logger.debug("Sending 'POST' request to URL: {}", obj);
            logger.debug("encodedData parameters : {}", encodedData);

            wr.write(encodedData.getBytes());
            wr.flush();
        } catch (Exception e) {
            closeHTTPConnection();
            throw new RequestFailedException(e);
        }

        int responseCode = -1;
        try {
            responseCode = con.getResponseCode();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (responseCode == 200) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            } catch (Exception e) {
                closeHTTPConnection();
                throw new RequestFailedException(e);
            }
        } else {
            logger.debug("Response Code : {}", responseCode);
        }
        logger.debug("Response: {}", response);

        close(); // / try to return it to pool
        return RESS.toString();
    }

    public String getENDPOINT() {
        return ENDPOINT;
    }

    public void closeHTTPConnection() {
        con.disconnect();
    }

    public void close() {
        logger.debug("In close method");
        managedConnection.closeHandle(this);
    }

}
