package scrapper;

import java.util.List;

public interface IPPTCrawlable {
    /* please use try-finally with the instance */

    /**
     * initialize the executor with the given params
     * @param number BUAA student id number
     * @param password password of student
     * @param name real name of the student
     */
    void initial(String number, String password, String name);

    /**
     * login 智学北航
     * @return true if successfully login
     */
    boolean login();

    /**
     * get course list of this semester
     * @return list of course
     */
    List<String> getCourseList();

    /**
     * click and switch to the web and get detail timetable
     * @param course chosen course
     * @return true if successfully getting to
     */
    boolean gotoTargetCourse(String course);

    /**
     * get timetable except for special div "课后"
     * @return list of timetable
     */
    List<String> getTimeTable();

    /**
     * DEFAULT: create a note based on just one PPT
     * download all PPTs in the course time to local
     * and return the last one's path
     * @param targetTime chosen time
     * @return absolute path of downloaded PPT
     * @throws NoneResourcesException if no resources in the chosen course time
     */
    String downloadPPT(String targetTime) throws NoneResourcesException;

    /**
     * turn off the crawler after finishing
     * MUST!!!, waste resource otherwise
     */
    void quit();
}
