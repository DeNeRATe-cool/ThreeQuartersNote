package DataScrapper;

import java.util.List;

public interface ICrawlable {
    /*
      please use getInstance to get a scrapper
      please use try-finally with the instance
     */

    /**
     * initialize the executor with the given params
     * @param number BUAA student id number
     * @param password password of student
     * @param name real name of the student
     */
    void initial(String number, String password, String name);

    /**
     * login the sso system
     * @return true iff successful
     */
    boolean login();

    /**
     * get course list of this semester of the student
     * @return course list as List<String>
     */
    List<String> getCourseList();

    /**
     * crawler search the course
     * @param course chosen course name
     * @return true iff successful
     */
    boolean searchCourse(String course);

    /**
     * get teachers who teach the course
     * @return teachers list as List<String>
     */
    List<String> getTeachers();

    /**
     * crawler go to the target course page
     * @param teacher chosen teacher name
     * @return true iff successful
     */
    boolean gotoTargetTeacherCourse(String teacher);

    /**
     * get all course timetable can be reviewed
     * @return course timetable as List<String>
     */
    List<String> getCourseTimeTable();

    /**
     * crawler go to the target video page
     * @param timeTable chosen time
     * @return true iff successful
     */
    boolean gotoCourseTime(String timeTable);

    /**
     * download class video to local place
     * @return true iff successful download the video
     */
    boolean downloadCourseVideo();

    /**
     * turn off the crawler after finishing
     * MUST!!!, waste resource otherwise
     */
    void quit();
}
