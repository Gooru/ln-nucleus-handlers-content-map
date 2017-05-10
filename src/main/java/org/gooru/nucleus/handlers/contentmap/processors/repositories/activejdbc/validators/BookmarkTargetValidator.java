package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.validators;

import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.AJEntityBookmark;
import org.javalite.activejdbc.Base;

/**
 * @author ashish on 10/5/17.
 */
public final class BookmarkTargetValidator {

    private BookmarkTargetValidator() {
        throw new AssertionError();
    }

    public static boolean validateBookmarkTarget(AJEntityBookmark bookmark) {
        return TypeBasedValidator.validateContent(bookmark.getContentId(), bookmark.getContentType());
    }

    private static class TypeBasedValidator {
        private static final String VALIDATE_RESOURCE =
            "select count(*) from original_resource where id = ?::uuid and " + " is_deleted = false";
        private static final String VALIDATE_QUESTION = "select count(*) from content where id = ?::uuid and "
            + "is_deleted = false and content_format = 'question'::content_format_type ";
        private static final String VALIDATE_COLLECTION = "select count(*) from collection where id = ?::uuid and "
            + "is_deleted = false and format = 'collection'::content_container_type ";
        private static final String VALIDATE_ASSESSMENT = "select count(*) from collection where id = ?::uuid and "
            + "is_deleted = false and format = 'assessment'::content_container_type ";
        private static final String VALIDATE_LESSON =
            "select count(*) from lesson where lesson_id = ?::uuid and " + "is_deleted = false ";
        private static final String VALIDATE_UNIT =
            "select count(*) from unit where unit_id = ?::uuid and " + "is_deleted = false ";
        private static final String VALIDATE_COURSE =
            "select count(*) from course where id = ?::uuid and " + "is_deleted = false ";

        static boolean validateContent(String contentId, String type) {
            switch (type) {
            case "resource":
                return validate(VALIDATE_RESOURCE, contentId);
            case "question":
                return validate(VALIDATE_QUESTION, contentId);
            case "collection":
                return validate(VALIDATE_COLLECTION, contentId);
            case "assessment":
                return validate(VALIDATE_ASSESSMENT, contentId);
            case "lesson":
                return validate(VALIDATE_LESSON, contentId);
            case "unit":
                return validate(VALIDATE_UNIT, contentId);
            case "course":
                return validate(VALIDATE_COURSE, contentId);
            default:
                throw new IllegalStateException("Invalid type of bookmark " + type);
            }
        }

        static boolean validate(String query, String contentId) {
            Object countObject = Base.firstCell(query, contentId);
            return ((Long) countObject > 0);
        }

    }
}
