package com.laboki.eclipse.plugin.javasyntaxfixer.main;

import com.laboki.eclipse.plugin.javasyntaxfixer.listeners.AnnotationsListener;
import com.laboki.eclipse.plugin.javasyntaxfixer.services.BaseServices;

public final class PartServices extends BaseServices {

	@Override
	protected void
	startServices() {
		this.startService(new Remover());
		this.startService(new Inserter());
		this.startService(new ProblemsHelper());
		this.startService(new ProblemsFixer());
		this.startService(new ProblemsFilterer());
		this.startService(new ProblemsFinder());
		this.startService(new ErrorChecker());
		this.startService(new Scheduler());
		this.startService(new AnnotationsListener());
	}

	@Override
	protected void
	cancelTasks() {
		EditorContext.cancelAllTasks();
		super.cancelTasks();
	}
}
