package com.company.jmixpm.view.document;

import com.company.jmixpm.entity.Document;

import com.company.jmixpm.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.core.LoadContext;
import io.jmix.core.UnconstrainedDataManager;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "documents", layout = MainView.class)
@ViewController("Document.list")
@ViewDescriptor("document-list-view.xml")
@LookupComponent("documentsDataGrid")
@DialogMode(width = "64em")
public class DocumentListView extends StandardListView<Document> {

    @Autowired
    private UnconstrainedDataManager unconstrainedDataManager;

    @Install(to = "documentsDl", target = Target.DATA_LOADER)
    private List<Document> documentsDlLoadDelegate(final LoadContext<Document> loadContext) {
        return unconstrainedDataManager.loadList(loadContext);
    }
}