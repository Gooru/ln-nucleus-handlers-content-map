package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities;

import java.sql.Date;
import java.util.*;

import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.converters.ConverterRegistry;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.converters.FieldConverter;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.validators.FieldSelector;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.validators.FieldValidator;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.validators.ValidatorRegistry;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * @author ashish on 13/2/17.
 */
@Table("user_bookmarks")
public class AJEntityBookmark extends Model {
    private static final String CONTENT_ID = "content_id";
    private static final String USER_ID = "user_id";
    private static final String CONTENT_TYPE = "content_type";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String CREATED_AT = "created_at";
    private static final String UPDATED_AT = "updated_at";
    private static final String SEQUENCE_ID = "sequence_id";
    private static final String IS_DELETED = "is_deleted";

    private static final List<String> CONTENT_TYPE_VALUES =
        Arrays.asList("course", "unit", "lesson", "collection", "assessment", "question", "resource");
    public static final Set<String> CREATABLE_FIELDS = new HashSet<>(Arrays.asList(CONTENT_ID, CONTENT_TYPE, TITLE));

    private static final Map<String, FieldValidator> validatorRegistry;
    private static final Map<String, FieldConverter> converterRegistry;
    public static final String FETCH_UNDELETED_BOOKMARK_QUERY = "id = ?::uuid and is_deleted = ?";

    static {
        validatorRegistry = initializeValidators();
        converterRegistry = initializeConverters();
    }

    private static Map<String, FieldConverter> initializeConverters() {
        Map<String, FieldConverter> converterMap = new HashMap<>();
        converterMap.put(CONTENT_ID, (fieldValue -> FieldConverter.convertFieldToUuid((String) fieldValue)));
        converterMap.put(USER_ID, (fieldValue -> FieldConverter.convertFieldToUuid((String) fieldValue)));
        return Collections.unmodifiableMap(converterMap);
    }

    private static Map<String, FieldValidator> initializeValidators() {
        Map<String, FieldValidator> validatorMap = new HashMap<>();
        validatorMap.put(CONTENT_ID, (FieldValidator::validateUuid));
        validatorMap.put(TITLE, (value) -> FieldValidator.validateString(value, 1000));
        validatorMap.put(CONTENT_TYPE,
            (value) -> ((value != null) && (value instanceof String) && (CONTENT_TYPE_VALUES.contains(value))));
        return Collections.unmodifiableMap(validatorMap);
    }

    public static FieldSelector createFieldSelector() {
        return new FieldSelector() {
            @Override
            public Set<String> allowedFields() {
                return Collections.unmodifiableSet(CREATABLE_FIELDS);
            }

            @Override
            public Set<String> mandatoryFields() {
                return Collections.unmodifiableSet(CREATABLE_FIELDS);
            }
        };
    }

    private static final String BOOKMARK_CREATE_DML =
        "insert into user_bookmarks (content_id, user_id, content_type, title, sequence_id) values (?::uuid, ?::uuid,"
            + " ?, ?, (select coalesce(max(sequence_id), 0)+1 from user_bookmarks where user_id = ?::uuid)) returning"
            + " id";

    public static String createBookmark(String contentId, String userId, String contentType, String title) {
        return Base.firstCell(BOOKMARK_CREATE_DML, contentId, userId, contentType, title, userId).toString();
    }

    public static String createBookmark(AJEntityBookmark bookmark) {
        return createBookmark(bookmark.getContentId(), bookmark.getUserId(), bookmark.getContentType(),
            bookmark.getTitle());
    }

    public String getContentId() {
        return this.getString(CONTENT_ID);
    }

    public String getUserId() {
        return this.getString(USER_ID);
    }

    public String getContentType() {
        return this.getString(CONTENT_TYPE);
    }

    public String getTitle() {
        return this.getString(TITLE);
    }

    public String getDescription() {
        return this.getString(DESCRIPTION);
    }

    public Date getCreatedAt() {
        return this.getDate(CREATED_AT);
    }

    public Date getUpdatedAt() {
        return this.getDate(UPDATED_AT);
    }

    public int getSequenceId() {
        return this.getInteger(SEQUENCE_ID);
    }

    public boolean getIsDeleted() {
        return this.getBoolean(IS_DELETED);
    }

    public void setUserId(String userId) {
        this.setFieldUsingConverter(USER_ID, userId);
    }

    public void setDeleted() {
        this.setBoolean(IS_DELETED, true);
    }

    private void setFieldUsingConverter(String fieldName, Object fieldValue) {
        FieldConverter fc = converterRegistry.get(fieldName);
        if (fc != null) {
            this.set(fieldName, fc.convertField(fieldValue));
        } else {
            this.set(fieldName, fieldValue);
        }
    }

    public static ValidatorRegistry getValidatorRegistry() {
        return new BookmarkValidationRegistry();
    }

    public static ConverterRegistry getConverterRegistry() {
        return new BookmarkConverterRegistry();
    }

    private static class BookmarkValidationRegistry implements ValidatorRegistry {
        @Override
        public FieldValidator lookupValidator(String fieldName) {
            return validatorRegistry.get(fieldName);
        }
    }

    private static class BookmarkConverterRegistry implements ConverterRegistry {
        @Override
        public FieldConverter lookupConverter(String fieldName) {
            return converterRegistry.get(fieldName);
        }
    }

}
