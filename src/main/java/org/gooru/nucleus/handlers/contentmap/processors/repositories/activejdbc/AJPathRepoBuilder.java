package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.PathRepo;

public final class AJPathRepoBuilder {

    private AJPathRepoBuilder() {
        throw new AssertionError();
    }

    public static PathRepo buildPathRepo(ProcessorContext context) {
        return new AJPathRepo(context);
    }
}
