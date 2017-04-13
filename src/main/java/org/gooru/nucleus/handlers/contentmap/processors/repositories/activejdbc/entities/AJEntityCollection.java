package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("collection")
public class AJEntityCollection extends Model {
    public static final String SELECT_CUL_COLLECTION_TO_VALIDATE =
        "SELECT id FROM collection where id = ?::uuid AND lesson_id = ?::uuid AND unit_id = ?::uuid AND course_id = ?::uuid AND format = ?::content_container_type AND subformat = ? AND is_deleted = false";
   
    public static final String SELECT_COLLECTION_TO_VALIDATE =
        "SELECT id FROM collection where id = ?::uuid AND format = ?::content_container_type AND subformat = ? AND is_deleted = false";
   
    
    public static final String SELECT_COLLECTION = "SELECT id, title, thumbnail from collection where id = ANY(?::uuid[]) AND is_deleted = false";
}
