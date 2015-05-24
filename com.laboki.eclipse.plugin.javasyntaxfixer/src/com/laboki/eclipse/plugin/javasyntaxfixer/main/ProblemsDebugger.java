package com.laboki.eclipse.plugin.javasyntaxfixer.main;

import org.eclipse.jdt.core.compiler.IProblem;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.laboki.eclipse.plugin.javasyntaxfixer.events.FixProblemEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.instance.EventBusInstance;
import com.laboki.eclipse.plugin.javasyntaxfixer.task.Task;

public final class ProblemsDebugger extends EventBusInstance {

	private static final int DELAY = 125;

	@Subscribe
	@AllowConcurrentEvents
	public static void
	eventHandler(final FixProblemEvent event) {
		new Task() {

			@Override
			public void
			execute() {
				this.print(event.getProblem());
			}

			private void
			print(final IProblem problem) {
				this.printIdString(problem);
				this.printArguments(problem);
				this.printMessage(problem);
			}

			private void
			printIdString(final IProblem problem) {
				System.out.println("===");
				System.out.println(this.getIdString(problem));
			}

			private String
			getIdString(final IProblem problem) {
				String string;
				switch (problem.getID()) {
					case IProblem.ParsingErrorInsertToComplete:
						string = "InsertToComplete: ";
						break;
					case IProblem.ParsingErrorInsertToCompletePhrase:
						string = "InsertToCompletePhrase: ";
						break;
					case IProblem.ParsingErrorInsertToCompleteScope:
						string = "InsertToCompleteScope: ";
						break;
					case IProblem.ParsingErrorInsertTokenAfter:
						string = "InsertTokenAfter: ";
						break;
					case IProblem.ParsingErrorInsertTokenBefore:
						string = "InsertTokenBefore: ";
						break;
					case IProblem.ParsingErrorDeleteToken:
						string = "DeleteToken: ";
						break;
					default:
						string = "";
						break;
				}
				return string;
			}

			private void
			printArguments(final IProblem problem) {
				System.out.println("--");
				for (final String string : problem.getArguments())
					System.out.println(string);
				System.out.println("--");
			}

			private void
			printMessage(final IProblem problem) {
				System.out.println(problem.getMessage());
				System.out.println("===");
				System.out.println();
			}
		}.setDelay(ProblemsDebugger.DELAY)
			.setRule(Scheduler.RULE)
			.setFamily(Scheduler.FAMILY)
			.start();
	}
}
