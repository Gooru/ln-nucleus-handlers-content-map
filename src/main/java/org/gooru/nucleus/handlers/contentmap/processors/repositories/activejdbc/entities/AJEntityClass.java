package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("class")
public class AJEntityClass extends Model {
    public static final String SELECT_CLASS_TO_VALIDATE =
        "SELECT id FROM class where id = ?::uuid AND is_deleted = false";
}
