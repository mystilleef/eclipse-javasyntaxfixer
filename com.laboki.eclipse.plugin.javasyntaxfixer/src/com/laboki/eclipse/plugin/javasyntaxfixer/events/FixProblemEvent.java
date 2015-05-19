package com.laboki.eclipse.plugin.javasyntaxfixer.events;

import org.eclipse.jdt.core.compiler.IProblem;

public final class FixProblemEvent {

	private final IProblem problem;

	public FixProblemEvent(final IProblem problem) {
		this.problem = problem;
	}

	public IProblem
	getProblem() {
		return this.problem;
	}
}
