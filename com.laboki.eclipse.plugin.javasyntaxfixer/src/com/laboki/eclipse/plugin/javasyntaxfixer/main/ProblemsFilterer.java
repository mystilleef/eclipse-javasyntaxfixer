package com.laboki.eclipse.plugin.javasyntaxfixer.main;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.compiler.IProblem;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.laboki.eclipse.plugin.javasyntaxfixer.events.FixProblemEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.events.ProblemsFoundEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.instance.EventBusInstance;
import com.laboki.eclipse.plugin.javasyntaxfixer.task.Task;

public final class ProblemsFilterer extends EventBusInstance {

	private static final String SEMICOLON = ";";
	private static final List<Integer> PROBLEM_IDS =
		Arrays.asList(IProblem.ParsingErrorInsertToComplete,
			IProblem.ParsingErrorInsertTokenAfter,
			IProblem.ParsingErrorInsertToCompletePhrase,
			IProblem.ParsingErrorInsertToCompleteScope,
			IProblem.ParsingErrorInsertTokenBefore,
			IProblem.ParsingErrorDeleteToken);

	@Subscribe
	@AllowConcurrentEvents
	public static void
	eventHandler(final ProblemsFoundEvent event) {
		new Task() {

			@Override
			public void
			execute() {
				if (this.hasSemiColonProblem()) this.fix(this.getSemiColonProblem());
				else this.fix(this.getProblem());
			}

			private boolean
			hasSemiColonProblem() {
				for (final IProblem problem : event.getProblems())
					if (this.isSemiColonProblem(problem)) return true;
				return false;
			}

			private boolean
			isSemiColonProblem(final IProblem problem) {
				return this.isRelevantProblem(problem) && this.isSemiColon(problem);
			}

			private void
			fix(final IProblem problem) {
				if (problem != null) EventBus.post(new FixProblemEvent(problem));
			}

			private IProblem
			getSemiColonProblem() {
				System.out.println("get semicolon problem");
				for (final IProblem problem : event.getProblems())
					if (this.isSemiColon(problem)) return problem;
				return null;
			}

			private boolean
			isSemiColon(final IProblem problem) {
				for (final String string : problem.getArguments())
					if (this.isSemiColonCharacter(string)) return true;
				return false;
			}

			private boolean
			isSemiColonCharacter(final String string) {
				return string.trim().equals(ProblemsFilterer.SEMICOLON);
			}

			private IProblem
			getProblem() {
				for (final IProblem problem : event.getProblems())
					if (this.isRelevantProblem(problem)) return problem;
				return null;
			}

			private boolean
			isRelevantProblem(final IProblem problem) {
				return problem.isError()
					&& ProblemsFilterer.PROBLEM_IDS.contains(problem.getID());
			}
		}.setDelay(125)
			.setRule(Scheduler.RULE)
			.setFamily(Scheduler.FAMILY)
			.start();
	}
}
