package com.bbs.security;

import java.util.Set;

public class Authority {
	private String uri;
	private Set<String> auth;
	public Authority(String uri,Set<String> auth){
		this.uri=uri;
		this.auth=auth;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public Set<String> getAuth() {
		return auth;
	}
	public void setAuth(Set<String> auth) {
		this.auth = auth;
	}

}
