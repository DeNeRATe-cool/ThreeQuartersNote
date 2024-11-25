package scrapper;

import org.openqa.selenium.WebElement;

import java.util.*;


public class TeacherExecutor {

    private List<String> teachers;

    TeacherExecutor() {}
    TeacherExecutor(List<WebElement> courseElements) {
        LinkedHashSet<String> teacherHashSet = new LinkedHashSet<>();
        for (WebElement element : courseElements) {
            LinkedList<String> params = new LinkedList<>(List.of(element.getText().split("\n")));
            teacherHashSet.add(params.getLast());
        }
        teachers = new LinkedList<>(teacherHashSet);
    }

    /**
     * get teachers teaching the chosen course
     * [牛建伟, 邓莹莹, 刘子鹏, 李辉勇]
     * @return teachers can be selected
     */
    public List<String> getTeachers() {
        return new ArrayList<>(teachers);
    }
}
