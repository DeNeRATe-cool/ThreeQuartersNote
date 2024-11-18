package DataScrapper;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class TimeTableExecutor {

    private List<String> TimeTable;

    TimeTableExecutor() {}
    TimeTableExecutor(List<WebElement> courseTimeTable) {
        TimeTable = new ArrayList<>();
        for(WebElement element : courseTimeTable) {
            List<String> params = List.of(element.getText().split("\n"));
            if(params.get(3).equals("停课") || params.get(3).equals("未开始"))
                continue;
            StringBuilder sb = new StringBuilder();
            sb.append(params.get(1));
            TimeTable.add(sb.toString());
        }
    }

    /**
     * get course timetable with the given class name and teacher
     * @return list of courseTimeTable object
     */
    public List<String> getCourseTimeTable() {
        return TimeTable;
    }
}
