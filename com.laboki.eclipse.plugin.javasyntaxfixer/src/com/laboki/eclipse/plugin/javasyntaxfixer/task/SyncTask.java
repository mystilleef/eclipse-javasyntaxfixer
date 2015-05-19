package com.laboki.eclipse.plugin.javasyntaxfixer.task;

import com.laboki.eclipse.plugin.javasyntaxfixer.main.EditorContext;

public abstract class SyncTask extends Task {

	@Override
	protected TaskJob
	newTaskJob() {
		return new TaskJob() {

			@Override
			protected void
			runTask() {
				EditorContext.syncExec(() -> SyncTask.this.execute());
			}
		};
	}
}
