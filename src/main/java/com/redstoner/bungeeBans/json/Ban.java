package com.redstoner.bungeeBans.json;

public abstract class Ban {
	private String created;
	private String source;
	private String expires;
	private String reason;

	protected Ban(String created, String source, String expires, String reason) {
		this.created = created;
		this.source = source;
		this.expires = expires;
		this.reason = reason;
	}

	public abstract String getIdentifier();

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getExpires() {
		return expires;
	}

	public void setExpires(String expires) {
		this.expires = expires;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
