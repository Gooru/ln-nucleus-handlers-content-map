package org.gooru.nucleus.handlers.contentmap.processors.repositories;

import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;

public interface CourseMapRepo {

    MessageResponse fetchCourseMapCourse();

    MessageResponse fetchCourseMapUnit();

    MessageResponse fetchCourseMapLesson();
    
}
