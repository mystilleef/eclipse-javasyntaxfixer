package com.laboki.eclipse.plugin.javasyntaxfixer.main;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.laboki.eclipse.plugin.javasyntaxfixer.events.AssistSessionEndedEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.events.CheckErrorsEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.events.SchedulerTaskEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.instance.EventBusInstance;
import com.laboki.eclipse.plugin.javasyntaxfixer.listeners.AssistSessionStartedEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.task.BaseTask;
import com.laboki.eclipse.plugin.javasyntaxfixer.task.Task;
import com.laboki.eclipse.plugin.javasyntaxfixer.task.TaskMutexRule;

public final class Scheduler extends EventBusInstance {

	public static final int DELAY = 10;
	public static final TaskMutexRule RULE = new TaskMutexRule();
	public static final String FAMILY = "JsyntaxSchedulerTaskFamily";
	private boolean canSchedule = true;

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final AssistSessionStartedEvent event) {
		this.canSchedule = false;
		EditorContext.cancelAllTasks();
	}

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final AssistSessionEndedEvent event) {
		this.canSchedule = true;
		this.checkErrors();
	}

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final SchedulerTaskEvent event) {
		this.checkErrors();
	}

	private void
	checkErrors() {
		new Task() {

			@Override
			protected boolean
			shouldSchedule() {
				if (!Scheduler.this.canSchedule) return false;
				return BaseTask.noTaskFamilyExists(Scheduler.FAMILY);
			}

			@Override
			public void
			execute() {
				EditorContext.cancelAllTasks();
				EventBus.post(new CheckErrorsEvent());
			}
		}.setFamily(Scheduler.FAMILY)
			.setRule(Scheduler.RULE)
			.setDelay(Scheduler.DELAY)
			.start();
	}
}
