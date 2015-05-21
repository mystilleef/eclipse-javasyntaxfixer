package com.laboki.eclipse.plugin.javasyntaxfixer.main;

import org.eclipse.jdt.core.compiler.IProblem;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.laboki.eclipse.plugin.javasyntaxfixer.events.FixProblemEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.instance.EventBusInstance;
import com.laboki.eclipse.plugin.javasyntaxfixer.task.Task;

public final class ProblemsDebugger extends EventBusInstance {

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
				System.out.println("===");
				System.out.println(this.getIdString(problem) + problem.getID());
				final String[] arguments = problem.getArguments();
				System.out.println("--");
				for (final String string : arguments)
					System.out.println(string);
				System.out.println("--");
				System.out.println(problem.getMessage());
				System.out.println("===");
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
		}.setDelay(125)
			.setRule(Scheduler.RULE)
			.setFamily(Scheduler.FAMILY)
			.start();
	}
}
