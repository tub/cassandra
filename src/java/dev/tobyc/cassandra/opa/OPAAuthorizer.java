package dev.tobyc.cassandra.opa;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.apache.cassandra.auth.AuthenticatedUser;
import org.apache.cassandra.auth.IAuthorizer;
import org.apache.cassandra.auth.IResource;
import org.apache.cassandra.auth.Permission;
import org.apache.cassandra.auth.PermissionDetails;
import org.apache.cassandra.auth.RoleResource;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.cassandra.exceptions.RequestExecutionException;
import org.apache.cassandra.exceptions.RequestValidationException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

public class OPAAuthorizer implements IAuthorizer {
	private static final Logger logger = LoggerFactory.getLogger(OPAAuthorizer.class);
	private final ObjectMapper jsonMapper = new ObjectMapper();
	private String opaUrl;
	// Create an instance of HttpClient.
	HttpClient client = new HttpClient();

	@Override
	public Set<Permission> authorize(AuthenticatedUser user, IResource resource) {
		PostMethod post = new PostMethod(opaUrl);

		try {
			post.setRequestEntity(new StringRequestEntity(jsonMapper.writeValueAsString(
					new CassandraOPARequest(user.getName(), resource.getName(), DatabaseDescriptor.getClusterName())),
					"application/json", "utf-8"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			client.executeMethod(post);
			String responseJson = post.getResponseBodyAsString();
			CassandraOPAResponse opaResponse = jsonMapper.readValue(responseJson, CassandraOPAResponse.class);
			return opaResponse.getResult();
		} catch (IOException e) {
			// Fail closed
			logger.error("Errror talking to OPA Server during authorizaion, " + "assuming no permissions.", e);
			return Sets.newHashSet();
		}

	}

	@Override
	public void grant(AuthenticatedUser performer, Set<Permission> permissions, IResource resource,
			RoleResource grantee) throws RequestValidationException, RequestExecutionException {
		throw new UnsupportedOperationException("OPA Policies may not be modified via Cassandra,"
				+ " see the OPA docs for details on how to update policies.");
	}

	@Override
	public void revoke(AuthenticatedUser performer, Set<Permission> permissions, IResource resource,
			RoleResource revokee) throws RequestValidationException, RequestExecutionException {
		throw new UnsupportedOperationException("OPA Policies may not be modified via Cassandra,"
				+ " see the OPA docs for details on how to update policies.");
	}

	@Override
	public Set<PermissionDetails> list(AuthenticatedUser performer, Set<Permission> permissions, IResource resource,
			RoleResource grantee) throws RequestValidationException, RequestExecutionException {
		throw new UnsupportedOperationException("LIST PERMISSIONS operation is not supported by OPAAuthorizer");
	}

	@Override
	public void revokeAllFrom(RoleResource revokee) {
		throw new UnsupportedOperationException("OPA Policies may not be modified via Cassandra,"
				+ " see the OPA docs for details on how to update policies.");
	}

	@Override
	public void revokeAllOn(IResource droppedResource) {
		throw new UnsupportedOperationException("OPA Policies may not be modified via Cassandra,"
				+ " see the OPA docs for details on how to update policies.");
	}

	@Override
	public Set<? extends IResource> protectedResources() {
		return Collections.emptySet();
	}

	@Override
	public void validateConfiguration() throws ConfigurationException {
		opaUrl = System.getProperty("opa_url");
		if (opaUrl == null) {
			logger.info("opa_url not set, defaulting to localhost:8181");
			opaUrl = "http://localhost:8181/v1/data/cassandra/result";
		}
	}

	@Override
	public void setup() {
	}

}
