package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbauth;

import org.gooru.nucleus.handlers.contentmap.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;
import org.javalite.activejdbc.Model;

/**
 * Created by ashish on 14/2/17.
 */
public interface Authorizer<T extends Model> {

    ExecutionResult<MessageResponse> authorize(T model);

}
