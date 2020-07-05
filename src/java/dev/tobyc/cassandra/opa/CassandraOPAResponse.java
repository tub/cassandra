package dev.tobyc.cassandra.opa;

import java.util.Set;

import org.apache.cassandra.auth.Permission;

public class CassandraOPAResponse {
	
	private Set<Permission> result;

	public Set<Permission> getResult() {
		return result;
	}

	public void setResult(Set<Permission> result) {
		this.result = result;
	}
	
}