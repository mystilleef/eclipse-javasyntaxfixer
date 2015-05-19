package com.laboki.eclipse.plugin.javasyntaxfixer.listeners;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

import com.google.common.base.Optional;
import com.laboki.eclipse.plugin.javasyntaxfixer.main.EditorContext;

public final class TextInsertionListener extends BaseListener
	implements
		VerifyListener {

	private final Optional<StyledText> buffer =
		EditorContext.getBuffer(EditorContext.getEditor());

	@Override
	public void
	verifyText(final VerifyEvent e) {
		BaseListener.scheduleErrorChecking();
	}

	@Override
	protected void
	add() {
		if (!this.buffer.isPresent()) return;
		this.buffer.get().addVerifyListener(this);
	}

	@Override
	protected void
	remove() {
		if (!this.buffer.isPresent()) return;
		this.buffer.get().removeVerifyListener(this);
	}
}
