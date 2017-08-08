package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("original_resource")
public class AJEntityOriginalResource extends Model {

    public static final String CONTENT_SUBFORMAT = "content_subformat";
    
    public static final String SELECT_RESOURCE_TO_VALIDATE =
        "SELECT id FROM original_resource where id = ?::uuid AND is_deleted = false";

    public static final String SELECT_RESOURCE =
        "SELECT id, title, thumbnail, content_subformat  from original_resource where id = ANY(?::uuid[]) AND is_deleted = false";
}
