package com.laboki.eclipse.plugin.javasyntaxfixer.listeners;

import org.eclipse.jface.text.contentassist.ContentAssistEvent;
import org.eclipse.jface.text.contentassist.ICompletionListener;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.quickassist.IQuickAssistAssistant;
import org.eclipse.jface.text.source.ContentAssistantFacade;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.IEditorPart;

import com.google.common.base.Optional;
import com.laboki.eclipse.plugin.javasyntaxfixer.events.AssistSessionEndedEvent;
import com.laboki.eclipse.plugin.javasyntaxfixer.instance.EventBusInstance;
import com.laboki.eclipse.plugin.javasyntaxfixer.instance.Instance;
import com.laboki.eclipse.plugin.javasyntaxfixer.main.EditorContext;
import com.laboki.eclipse.plugin.javasyntaxfixer.main.EventBus;

public class CompletionListener extends EventBusInstance
	implements
		ICompletionListener {

	private final Optional<IEditorPart> editor = EditorContext.getEditor();
	private final Optional<ContentAssistantFacade> contentAssistant =
		this.getContentAssistant();
	private final Optional<IQuickAssistAssistant> quickAssistant =
		this.getQuickAssistant();

	@Override
	public void
	assistSessionStarted(final ContentAssistEvent event) {
		EventBus.post(new AssistSessionStartedEvent());
	}

	@Override
	public void
	assistSessionEnded(final ContentAssistEvent event) {
		EventBus.post(new AssistSessionEndedEvent());
	}

	@Override
	public void
	selectionChanged(	final ICompletionProposal proposal,
										final boolean smartToggle) {}

	@Override
	public Instance
	start() {
		this.add();
		return super.start();
	}

	private void
	add() {
		if (this.contentAssistant.isPresent()) this.contentAssistant.get()
			.addCompletionListener(this);
		if (this.quickAssistant.isPresent()) this.quickAssistant.get()
			.addCompletionListener(this);
	}

	@Override
	public Instance
	stop() {
		this.remove();
		return super.stop();
	}

	private void
	remove() {
		if (this.contentAssistant.isPresent()) this.contentAssistant.get()
			.removeCompletionListener(this);
		if (this.quickAssistant.isPresent()) this.quickAssistant.get()
			.removeCompletionListener(this);
	}

	private Optional<ContentAssistantFacade>
	getContentAssistant() {
		final Optional<SourceViewer> view = EditorContext.getView(this.editor);
		if (!view.isPresent()) return Optional.absent();
		return Optional.fromNullable(view.get().getContentAssistantFacade());
	}

	private Optional<IQuickAssistAssistant>
	getQuickAssistant() {
		final Optional<SourceViewer> view = EditorContext.getView(this.editor);
		if (!view.isPresent()) return Optional.absent();
		return Optional.fromNullable(view.get().getQuickAssistAssistant());
	}
}
