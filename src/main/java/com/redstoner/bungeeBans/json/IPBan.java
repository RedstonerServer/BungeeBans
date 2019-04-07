package com.redstoner.bungeeBans.json;

public class IPBan extends Ban {
	private String ip;


	public IPBan(String ip, String created, String source, String expires, String reason) {
		super(created, source, expires, reason);

		this.ip = ip;
	}

	@Override
	public String getIdentifier() {
		return ip;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
