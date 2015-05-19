package com.laboki.eclipse.plugin.javasyntaxfixer.main;

import com.laboki.eclipse.plugin.javasyntaxfixer.services.BaseServices;

public final class Services extends BaseServices {

	@Override
	protected void
	startServices() {
		this.startService(new PartMonitor());
	}

	@Override
	protected void
	cancelTasks() {
		EditorContext.cancelAllTasks();
		super.cancelTasks();
	}
}
