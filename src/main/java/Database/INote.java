package Database;

import java.util.List;

public interface INote {
    /* please check if the note has uuid first */

    /**
     * check if the note has existed by uuid
     * @param note to be checked one
     * @return true if exists
     */
    boolean exist(Note note);

    /**
     * insert a new shared resource
     * need to add an uuid first
     * @param note object Note
     * @throws IllegalArgumentException if uuid is null or empty
     */
    void insert(Note note) throws IllegalArgumentException;

    /**
     * delete note if exist
     * @param note object note
     * @throws IllegalArgumentException if Note doesn't exist
     */
    void delete(Note note) throws IllegalArgumentException;

    /**
     * update the note which has existed
     * but renew the information
     * @param note object note
     * @throws IllegalArgumentException if Note doesn't exist
     */
    void update(Note note) throws IllegalArgumentException;

    /**
     * get ALL notes
     * @return list of all notes shared
     */
    List<Note> queryAll();

    /**
     * PARTIAL MATCH
     * get Note objects through course name
     * @param course course name
     * @return a list of notes contain the given information
     * @throws IllegalArgumentException if course is null or empty
     */
    List<Note> queryByCourse(String course) throws IllegalArgumentException;

    /**
     * PARTIAL MATCH
     * get Note objects through note name
     * @param noteName name of note
     * @return a list of notes contain the given information
     * @throws IllegalArgumentException if note name is null or empty
     */
    List<Note> queryByNoteName(String noteName) throws IllegalArgumentException;

    /**
     * FULL MATCH
     * get Note objects through writer's name
     * @param writer writer of note
     * @return a list of notes contain the given information
     * @throws IllegalArgumentException if writer is null or empty
     */
    List<Note> queryByWriter(String writer) throws IllegalArgumentException;

}
