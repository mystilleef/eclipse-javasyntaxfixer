package com.laboki.eclipse.plugin.javasyntaxfixer.listeners;

import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelListener;
import org.eclipse.ui.IEditorPart;

import com.google.common.base.Optional;
import com.laboki.eclipse.plugin.javasyntaxfixer.main.EditorContext;

public final class AnnotationsListener extends BaseListener
	implements
		IAnnotationModelListener {

	private final Optional<IAnnotationModel> annotationModel =
		AnnotationsListener.getAnnotationModel();

	@Override
	public void
	modelChanged(final IAnnotationModel model) {
		BaseListener.scheduleErrorChecking();
	}

	@Override
	protected void
	add() {
		if (!this.annotationModel.isPresent()) return;
		this.annotationModel.get().addAnnotationModelListener(this);
	}

	@Override
	protected void
	remove() {
		if (!this.annotationModel.isPresent()) return;
		this.annotationModel.get().removeAnnotationModelListener(this);
	}

	private static Optional<IAnnotationModel>
	getAnnotationModel() {
		final Optional<IEditorPart> editor = EditorContext.getEditor();
		if (!editor.isPresent()) return Optional.absent();
		return EditorContext.getAnnotationModel(editor);
	}
}
