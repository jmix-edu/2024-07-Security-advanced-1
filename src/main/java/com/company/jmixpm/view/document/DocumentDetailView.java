package com.company.jmixpm.view.document;

import com.company.jmixpm.entity.Document;

import com.company.jmixpm.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.core.SaveContext;
import io.jmix.core.UnconstrainedDataManager;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Route(value = "documents/:id", layout = MainView.class)
@ViewController("Document.detail")
@ViewDescriptor("document-detail-view.xml")
@EditedEntityContainer("documentDc")
public class DocumentDetailView extends StandardDetailView<Document> {

    @Autowired
    private UnconstrainedDataManager unconstrainedDataManager;

    @Install(target = Target.DATA_CONTEXT)
    private Set<Object> saveDelegate(final SaveContext saveContext) {
        return unconstrainedDataManager.save(saveContext);
    }
}