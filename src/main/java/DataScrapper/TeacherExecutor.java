package DataScrapper;

import org.openqa.selenium.WebElement;

import java.util.List;


public class TeacherExecutor {

    private List<WebElement> courseElements;

    TeacherExecutor() {}
    TeacherExecutor(List<WebElement> courseElements) {
        this.courseElements = courseElements;
    }

    /**
     * get teachers teaching the chosen course
     * @return teachers can be selected
     */
    public List<String> getTeachers() {
        return null;
    }
}
