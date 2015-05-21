package com.laboki.eclipse.plugin.javasyntaxfixer.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import com.google.common.base.Optional;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.laboki.eclipse.plugin.javasyntaxfixer.events.InsertTokenEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.instance.EventBusInstance;
import com.laboki.eclipse.plugin.javasyntaxfixer.task.AsyncTask;
import com.laboki.eclipse.plugin.javasyntaxfixer.task.Task;

public final class Inserter extends EventBusInstance {

	protected final Optional<IDocument> document =
		EditorContext.getDocument(EditorContext.getEditor());
	private static final Logger LOGGER =
		Logger.getLogger(Inserter.class.getName());

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final InsertTokenEvent event) {
		new Task() {

			@Override
			public void
			execute() {
				new InserterTask().setObject(event).start();
			}
		}.setFamily(Scheduler.FAMILY)
			.setRule(Scheduler.RULE)
			.setDelay(Scheduler.DELAY)
			.start();
	}

	private final class InserterTask extends AsyncTask {

		public InserterTask() {
			this.setDelay(Scheduler.DELAY);
			this.setFamily(Scheduler.FAMILY);
			this.setRule(Scheduler.RULE);
		}

		@Override
		public void
		execute() {
			if (this.isValid()) this.insertToken();
		}

		private boolean
		isValid() {
			if (!Inserter.this.document.isPresent()) return false;
			return !this.isInValidToken();
		}

		private boolean
		isInValidToken() {
			if (this.getToken().isEmpty()) return true;
			return this.getToken().length() > 1;
		}

		private void
		insertToken() {
			try {
				this.tryToInsert();
			}
			catch (final BadLocationException e) {
				Inserter.LOGGER.log(Level.WARNING, e.getMessage(), e);
			}
		}

		private void
		tryToInsert() throws BadLocationException {
			Inserter.this.document.get().replace(this.getLocation(),
				0,
				this.getToken());
		}

		private String
		getToken() {
			return ((InsertTokenEvent) this.getObject()).getToken();
		}

		private int
		getLocation() {
			return ((InsertTokenEvent) this.getObject()).getLocation();
		}
	}
}
