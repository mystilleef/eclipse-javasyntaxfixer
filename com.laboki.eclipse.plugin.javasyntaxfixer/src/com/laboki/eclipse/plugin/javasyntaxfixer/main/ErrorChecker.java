package com.laboki.eclipse.plugin.javasyntaxfixer.main;

import org.eclipse.ui.IEditorPart;

import com.google.common.base.Optional;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.laboki.eclipse.plugin.javasyntaxfixer.events.CheckErrorsEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.events.FindErrorsEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.instance.EventBusInstance;
import com.laboki.eclipse.plugin.javasyntaxfixer.task.AsyncTask;

public final class ErrorChecker extends EventBusInstance {

	private final Optional<IEditorPart> editor = EditorContext.getEditor();

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final CheckErrorsEvent event) {
		new AsyncTask() {

			@Override
			public void
			execute() {
				if (this.canFindErrors()) this.findErrors();
			}

			private boolean
			canFindErrors() {
				if (EditorContext.isInEditMode(ErrorChecker.this.editor)) return false;
				return EditorContext.hasJdtErrors(ErrorChecker.this.editor);
			}

			private void
			findErrors() {
				EventBus.post(new FindErrorsEvent());
			}
		}.setRule(Scheduler.RULE)
			.setFamily(Scheduler.FAMILY)
			.setDelay(Scheduler.DELAY)
			.start();
	}
}
