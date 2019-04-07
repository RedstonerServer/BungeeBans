package com.redstoner.bungeeBans.json;

public class PlayerBan extends Ban {
	private String uuid;
	private String name;


	public PlayerBan(String uuid, String name, String created, String source, String expires, String reason) {
		super(created, source, expires, reason);

		this.uuid = uuid;
		this.name = name;
	}

	@Override
	public String getIdentifier() {
		return uuid;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
