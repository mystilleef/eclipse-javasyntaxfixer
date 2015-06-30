package com.laboki.eclipse.plugin.javasyntaxfixer.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import com.google.common.base.Optional;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.laboki.eclipse.plugin.javasyntaxfixer.events.RemoveTokenEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.instance.EventBusInstance;
import com.laboki.eclipse.plugin.javasyntaxfixer.task.AsyncTask;
import com.laboki.eclipse.plugin.javasyntaxfixer.task.BaseTask;
import com.laboki.eclipse.plugin.javasyntaxfixer.task.Task;

public final class Remover extends EventBusInstance {

	protected final Optional<IDocument> document =
		EditorContext.getDocument(EditorContext.getEditor());
	private static final Logger LOGGER =
		Logger.getLogger(Inserter.class.getName());

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final RemoveTokenEvent event) {
		new Task() {

			@Override
			public void
			execute() {
				new RemoverTask().setObject(event).start();
			}

			@Override
			protected boolean
			shouldSchedule() {
				return BaseTask.noTaskFamilyExists(Scheduler.FAMILY);
			}

		}.setDelay(Scheduler.DELAY)
			.setFamily(Scheduler.FAMILY)
			.setRule(Scheduler.RULE)
			.start();
	}

	private final class RemoverTask extends AsyncTask {

		public RemoverTask() {
			this.setDelay(Scheduler.DELAY);
			this.setRule(Scheduler.RULE);
			this.setFamily(Scheduler.FAMILY);
		}

		@Override
		public void
		execute() {
			if (this.isValid()) this.removeToken();
		}

		private boolean
		isValid() {
			if (!Remover.this.document.isPresent()) return false;
			return !this.isInValidToken();
		}

		private boolean
		isInValidToken() {
			if (this.getToken().isEmpty()) return true;
			return this.getToken().length() > 1;
		}

		private String
		getToken() {
			return ((RemoveTokenEvent) this.getObject()).getToken();
		}

		private void
		removeToken() {
			try {
				this.tryToRemove();
			}
			catch (final BadLocationException e) {
				Remover.LOGGER.log(Level.WARNING, e.getMessage(), e);
			}
		}

		private void
		tryToRemove() throws BadLocationException {
			Remover.this.document.get().replace(this.getLocation(), 1, "");
		}

		private int
		getLocation() {
			return ((RemoveTokenEvent) this.getObject()).getLocation();
		}
	}
}
