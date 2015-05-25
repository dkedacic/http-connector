package inbound.http.server.config.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "http_connector")
public class HTTPConnectorTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @Column
    private String outboundAddress;

    
    public Long getId() {
        return id;
    }
    
    public String getOutboundAddress() {
        return outboundAddress;
    }
    
    public void setOutboundAddress(String outboundAddress) {
        this.outboundAddress = outboundAddress;
    }

}
