package com.laboki.eclipse.plugin.javasyntaxfixer.main;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Control;

import com.google.common.base.Optional;
import com.laboki.eclipse.plugin.javasyntaxfixer.listeners.BaseListener;

public final class KeyEventListener extends BaseListener
	implements
		KeyListener {

	private final Optional<Control> control =
		EditorContext.getControl(EditorContext.getEditor());

	@Override
	public void
	keyPressed(final KeyEvent e) {
		EditorContext.cancelAllTasks();
	}

	@Override
	public void
	keyReleased(final KeyEvent e) {
		BaseListener.scheduleErrorChecking();
	}

	@Override
	public void
	add() {
		if (!this.control.isPresent()) return;
		this.control.get().addKeyListener(this);
	}

	@Override
	public void
	remove() {
		if (!this.control.isPresent()) return;
		this.control.get().removeKeyListener(this);
	}
}
