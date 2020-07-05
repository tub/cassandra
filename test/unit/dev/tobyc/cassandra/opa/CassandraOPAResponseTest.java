package dev.tobyc.cassandra.opa;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.cassandra.auth.Permission;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import junit.framework.Assert;

public class CassandraOPAResponseTest {

	@Test
	public void test() throws JsonParseException, JsonMappingException, IOException {
		String responseJson = "{\"result\": [\"SELECT\"]}";
		CassandraOPAResponse opaResponse = new ObjectMapper().readValue(responseJson, CassandraOPAResponse.class);
		Assert.assertEquals(1, opaResponse.getResult().size());
		Assert.assertEquals(true, opaResponse.getResult().contains(Permission.SELECT));


	}

}
