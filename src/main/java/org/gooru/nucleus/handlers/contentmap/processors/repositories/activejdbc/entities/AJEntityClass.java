package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("class")
public class AJEntityClass extends Model {
    public static final String CREATOR_ID = "creator_id";
    public static final String COURSE_ID = "course_id";
    public static final String ID = "id";
    public static final String COLLABORATOR = "collaborator";
    public static final String SELECT_CLASS_TO_VALIDATE =
        "SELECT id, creator_id, collaborator, course_id FROM class where id = ?::uuid AND is_deleted = false";
}
