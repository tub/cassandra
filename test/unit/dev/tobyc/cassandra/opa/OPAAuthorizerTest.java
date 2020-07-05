package dev.tobyc.cassandra.opa;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.apache.cassandra.auth.AuthenticatedUser;
import org.apache.cassandra.auth.DataResource;
import org.apache.cassandra.auth.Permission;
import org.junit.Test;

public class OPAAuthorizerTest {
	

	@Test
	public void test() {
		OPAAuthorizer opaAuthorizer = new OPAAuthorizer();
		opaAuthorizer.validateConfiguration();
		
		AuthenticatedUser authenticatedUser = new AuthenticatedUser("tobyc");
		DataResource keyspace = DataResource.keyspace("test");
		
		Set<Permission> authorize = opaAuthorizer.authorize(authenticatedUser, keyspace);
		assertEquals(2, authorize.size());
	}

}
