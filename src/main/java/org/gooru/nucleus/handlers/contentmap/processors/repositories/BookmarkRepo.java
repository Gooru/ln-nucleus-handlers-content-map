package org.gooru.nucleus.handlers.contentmap.processors.repositories;

import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;

/**
 * @author ashish on 13/2/17.
 */
public interface BookmarkRepo {

    MessageResponse createBookmark();

    MessageResponse deleteBookmark();

    MessageResponse listBookmark();

}
