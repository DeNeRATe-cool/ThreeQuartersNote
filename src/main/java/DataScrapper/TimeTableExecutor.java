package DataScrapper;

import org.openqa.selenium.WebElement;

import java.util.List;

public class TimeTableExecutor {

    private List<WebElement> courseTimeTable;

    TimeTableExecutor() {}
    TimeTableExecutor(List<WebElement> courseTimeTable) {
        this.courseTimeTable = courseTimeTable;
    }

    /**
     * get course timetable with the given class name and teacher
     * @return list of courseTimeTable object
     */
    public List<CourseTimeTable> getCourseTimeTable() {
        return null;
    }
}
