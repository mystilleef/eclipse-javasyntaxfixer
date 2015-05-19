package com.laboki.eclipse.plugin.javasyntaxfixer.listeners;

import com.laboki.eclipse.plugin.javasyntaxfixer.events.SchedulerTaskEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.instance.EventBusInstance;
import com.laboki.eclipse.plugin.javasyntaxfixer.instance.Instance;
import com.laboki.eclipse.plugin.javasyntaxfixer.main.EditorContext;
import com.laboki.eclipse.plugin.javasyntaxfixer.main.EventBus;
import com.laboki.eclipse.plugin.javasyntaxfixer.task.BaseTask;
import com.laboki.eclipse.plugin.javasyntaxfixer.task.Task;
import com.laboki.eclipse.plugin.javasyntaxfixer.task.TaskMutexRule;

public abstract class BaseListener extends EventBusInstance {

	private static final int ONE_SECOND = 1000;
	private static final TaskMutexRule RULE = new TaskMutexRule();
	public static final String FAMILY = "JsyntaxAbstractListenerTaskFamily";

	@Override
	public Instance
	start() {
		this.add();
		return super.start();
	}

	protected abstract void
	add();

	@Override
	public Instance
	stop() {
		this.remove();
		return super.stop();
	}

	protected abstract void
	remove();

	protected final static void
	scheduleErrorChecking() {
		EditorContext.cancelAllTasks();
		BaseListener.scheduleTask();
	}

	private static void
	scheduleTask() {
		new Task() {

			@Override
			public boolean
			shouldSchedule() {
				return BaseTask.noTaskFamilyExists(BaseListener.FAMILY);
			}

			@Override
			public void
			execute() {
				EventBus.post(new SchedulerTaskEvent());
			}
		}.setRule(BaseListener.RULE)
			.setFamily(BaseListener.FAMILY)
			.setDelay(BaseListener.ONE_SECOND)
			.start();
	}
}
