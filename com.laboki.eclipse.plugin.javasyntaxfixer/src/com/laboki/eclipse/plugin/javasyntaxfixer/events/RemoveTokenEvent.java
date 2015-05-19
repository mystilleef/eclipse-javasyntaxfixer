package com.laboki.eclipse.plugin.javasyntaxfixer.events;

public final class RemoveTokenEvent {

	private final String character;
	private final int location;

	public RemoveTokenEvent(final String character, final int location) {
		this.character = character;
		this.location = location;
	}

	public String
	getToken() {
		return this.character;
	}

	public int
	getLocation() {
		return this.location;
	}
}
