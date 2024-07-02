package com.company.jmixpm.security.specific;

import io.jmix.core.accesscontext.SpecificOperationAccessContext;

public class JmixPmProjectArchive extends SpecificOperationAccessContext {

    public static final String NAME = "jmix.pm.project.archive";

    public JmixPmProjectArchive() {
        super(NAME);
    }
}
