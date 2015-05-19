package com.laboki.eclipse.plugin.javasyntaxfixer.task;

import com.laboki.eclipse.plugin.javasyntaxfixer.main.EditorContext;

public abstract class AsyncTask extends Task {

	@Override
	protected TaskJob
	newTaskJob() {
		return new TaskJob() {

			@Override
			protected void
			runTask() {
				EditorContext.asyncExec(() -> AsyncTask.this.execute());
			}
		};
	}
}
