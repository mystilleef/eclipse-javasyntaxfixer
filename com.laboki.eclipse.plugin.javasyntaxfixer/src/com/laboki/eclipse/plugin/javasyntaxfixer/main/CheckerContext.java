package com.laboki.eclipse.plugin.javasyntaxfixer.main;

import com.laboki.eclipse.plugin.javasyntaxfixer.task.TaskMutexRule;

public enum CheckerContext {
	INSTANCE;

	public static final String FAMILY = "JsyntaxCheckerContextTaskFamily";
	public static final TaskMutexRule RULE = new TaskMutexRule();
}
