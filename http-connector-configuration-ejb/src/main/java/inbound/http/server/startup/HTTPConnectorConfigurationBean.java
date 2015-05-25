package inbound.http.server.startup;

import inbound.http.server.config.tables.HTTPConnectorTable;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
@LocalBean
public class HTTPConnectorConfigurationBean {
    Logger logger = LoggerFactory.getLogger(HTTPConnectorConfigurationBean.class);

    @PersistenceContext(unitName = "drazenJPA")
    EntityManager em;

    @PostConstruct
    public void start() {
        logger.info("reading HTTPConnectorTable from database");
    }

    public HTTPConnectorTable getHTTPConnectorConfiguration() {
        Long maxid = (Long) em.createQuery("select max(u.id) from HTTPConnectorTable u").getSingleResult();
        logger.debug("---------------->>>Najveci id: {}", maxid);

        Query query = em.createQuery("select u from HTTPConnectorTable u where u.id = :maxid");
        query.setParameter("maxid", maxid);
        HTTPConnectorTable table = (HTTPConnectorTable) query.getSingleResult();
        return table;
    }

}
