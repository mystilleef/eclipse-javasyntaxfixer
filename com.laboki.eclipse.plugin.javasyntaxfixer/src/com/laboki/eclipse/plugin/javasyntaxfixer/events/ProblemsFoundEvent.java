package com.laboki.eclipse.plugin.javasyntaxfixer.events;

import org.eclipse.jdt.core.compiler.IProblem;

import com.google.common.collect.ImmutableList;

public final class ProblemsFoundEvent {

	private final ImmutableList<IProblem> problems;

	public ProblemsFoundEvent(final ImmutableList<IProblem> problems) {
		this.problems = problems;
	}

	public ImmutableList<IProblem>
	getProblems() {
		return this.problems;
	}
}
