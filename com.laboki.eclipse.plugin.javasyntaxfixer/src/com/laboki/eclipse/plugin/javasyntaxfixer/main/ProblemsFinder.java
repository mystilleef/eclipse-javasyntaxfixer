package com.laboki.eclipse.plugin.javasyntaxfixer.main;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.laboki.eclipse.plugin.javasyntaxfixer.events.FindErrorsEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.events.ProblemsFoundEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.instance.EventBusInstance;
import com.laboki.eclipse.plugin.javasyntaxfixer.task.Task;

public final class ProblemsFinder extends EventBusInstance {

	private final Optional<ICompilationUnit> unit =
		ProblemsFinder.getCompilationUnit();

	private static Optional<ICompilationUnit>
	getCompilationUnit() {
		final Optional<IFile> file =
			EditorContext.getFile(EditorContext.getEditor());
		if (!file.isPresent()) return Optional.absent();
		return Optional.fromNullable(JavaCore.createCompilationUnitFrom(file.get()));
	}

	@Subscribe
	@AllowConcurrentEvents
	public void
	eventHandler(final FindErrorsEvent event) {
		new Task() {

			@Override
			public void
			execute() {
				final ImmutableList<IProblem> problems =
					ImmutableList.copyOf(this.getCompilerProblems());
				EventBus.post(new ProblemsFoundEvent(problems));
			}

			private IProblem[]
			getCompilerProblems() {
				final Optional<CompilationUnit> node =
					this.createCompilationUnitNode();
				if (!node.isPresent()) return new IProblem[0];
				return node.get().getProblems();
			}

			private Optional<CompilationUnit>
			createCompilationUnitNode() {
				final ASTParser parser = ASTParser.newParser(AST.JLS8);
				if (!ProblemsFinder.this.unit.isPresent()) return Optional.absent();
				parser.setSource(ProblemsFinder.this.unit.get());
				return Optional.fromNullable((CompilationUnit) parser.createAST(null));
			}
		}.setFamily(Scheduler.FAMILY)
			.setRule(Scheduler.RULE)
			.setDelay(125)
			.start();
	}
}
