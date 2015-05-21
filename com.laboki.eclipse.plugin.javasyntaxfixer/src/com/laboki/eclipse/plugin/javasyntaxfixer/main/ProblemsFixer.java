package com.laboki.eclipse.plugin.javasyntaxfixer.main;

import org.eclipse.jdt.core.compiler.IProblem;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.laboki.eclipse.plugin.javasyntaxfixer.events.FixProblemEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.events.InsertTokenEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.events.RemoveTokenEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.instance.EventBusInstance;
import com.laboki.eclipse.plugin.javasyntaxfixer.task.Task;

public final class ProblemsFixer extends EventBusInstance {

	@Subscribe
	@AllowConcurrentEvents
	public static void
	eventHandler(final FixProblemEvent event) {
		new Task() {

			@Override
			public void
			execute() {
				this.fix(event.getProblem());
			}

			private void
			fix(final IProblem problem) {
				switch (problem.getID()) {
					case IProblem.ParsingErrorInsertToComplete:
						this.emitInsertTokenEvent(problem, true);
						break;
					case IProblem.ParsingErrorInsertToCompletePhrase:
						this.emitInsertTokenEvent(problem, true);
						break;
					case IProblem.ParsingErrorInsertToCompleteScope:
						this.emitInsertTokenEvent(problem, true);
						break;
					case IProblem.ParsingErrorInsertTokenAfter:
						this.emitInsertTokenEvent(problem, false);
						break;
					case IProblem.ParsingErrorInsertTokenBefore:
						this.emitInsertTokenEvent(problem, false);
						break;
					case IProblem.ParsingErrorDeleteToken:
						this.emitRemoveTokenEvent(problem);
						break;
					default:
						break;
				}
			}

			private void
			emitInsertTokenEvent(final IProblem problem, final boolean firstArgument) {
				final String token = this.getInsertEventToken(problem, firstArgument);
				final int location = this.getLocation(problem);
				EventBus.post(new InsertTokenEvent(token, location));
			}

			private String
			getInsertEventToken(final IProblem problem, final boolean firstArgument) {
				if (firstArgument) return this.getFirstArgument(problem);
				return this.getSecondArgument(problem);
			}

			private void
			emitRemoveTokenEvent(final IProblem problem) {
				final String token = this.getFirstArgument(problem);
				final int location = this.getLocation(problem) - 1;
				EventBus.post(new RemoveTokenEvent(token, location));
			}

			private String
			getFirstArgument(final IProblem problem) {
				return this.getPunctuation(problem.getArguments()[0].trim());
			}

			private String
			getSecondArgument(final IProblem problem) {
				return this.getPunctuation(problem.getArguments()[1].trim());
			}

			private String
			getPunctuation(final String argument) {
				if (EditorContext.isPunctuation(argument)) return argument.trim();
				return this.getPunctuationFromArgument(argument);
			}

			private String
			getPunctuationFromArgument(final String argument) {
				for (final String string : EditorContext.splitString(argument))
					if (EditorContext.isPunctuation(string)) return string.trim();
				return "";
			}

			private int
			getLocation(final IProblem problem) {
				return problem.getSourceEnd() + 1;
			}
		}.setDelay(Scheduler.DELAY)
			.setRule(Scheduler.RULE)
			.setFamily(Scheduler.FAMILY)
			.start();
	}
}
