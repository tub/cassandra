package dev.tobyc.cassandra.opa;

public class CassandraOPARequest {

	private String user;
	private String resource;
	private String cluster;

	public CassandraOPARequest(String user, String resource, String cluster) {
		this.setUser(user);
		this.setResource(resource);
		this.setCluster(cluster);
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

}
