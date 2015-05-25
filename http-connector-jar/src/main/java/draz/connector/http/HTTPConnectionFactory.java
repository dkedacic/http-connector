package draz.connector.http;

import java.io.Serializable;

import javax.resource.Referenceable;

import draz.api.IRequest;



public interface HTTPConnectionFactory extends Serializable, Referenceable {


	public IRequest getConnection() throws Exception;

}
