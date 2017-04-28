package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("content")
public class AJEntityContent extends Model {
    public static final String RESOURCE_COUNT = "resource_count";
    public static final String QUESTION_COUNT = "question_count";
    public static final String OE_QUESTION_COUNT = "oe_question_count";
    public static final String CONTENT_COUNT = "content_count";
    public static final String CONTENT_FORMAT = "content_format";
    public static final String COLLECTION_ID = "collection_id";
    public static final String RESOURCE_FORMAT = "resource";
    public static final String QUESTION_FORMAT = "question";

    public static final String SELECT_CONTENT_COUNT_BY_COLLECTION =
        "SELECT count(id) as content_count, content_format, collection_id FROM content WHERE"
            + " collection_id = ANY(?::uuid[])  AND is_deleted = false GROUP BY" + " collection_id, content_format";

    public static final String SELECT_OE_QUESTION_COUNT =
        "SELECT count(id) as oe_question_count, collection_id FROM content WHERE collection_id = ANY(?::uuid[]) AND"
            + " is_deleted = false AND content_format = 'question' AND"
            + " content_subformat = 'open_ended_question' GROUP BY collection_id";

    public static final String SELECT_RESOURCE_TO_VALIDATE =
        "SELECT id FROM content where id = ?::uuid AND content_format = 'resource' AND is_deleted = false";
    
    public static final String SELECT_RESOURCE = "SELECT id, title, thumbnail from content where id = ANY(?::uuid[]) AND is_deleted = false";

}
