package Database;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "share")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "course")
    private String course;

    @Column(name = "writer")
    private String writer;

    @Column(name = "upload")
    private Date uploadTime;

    @Column(name = "content")
    private String content;

    @Column(name = "uuid")
    private String uuid;

    public Note() {}
    public Note(String name, String writer, Date uploadTime, String content, String uuid) {
        // constructor without COURSE
        this.course = null;
        this.name = name;
        this.writer = writer;
        this.uploadTime = uploadTime;
        this.content = content;
        this.uuid = uuid;
    }
    public Note(String name, String course, String writer, Date uploadTime, String content, String uuid) {
        this.name = name;
        this.course = course;
        this.writer = writer;
        this.uploadTime = uploadTime;
        this.content = content;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String getWriter() {
        return writer;
    }

    public String getContent() {
        return content;
    }

    public String getUuid() {
        return uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Note {" + " id = " + this.id + "," + " name = " + this.name + "," + " course = " + this.course + "," +
                " writer = " + this.writer + "," + " uploadTime = " + this.uploadTime + "," + " uuid = " + this.uuid + " }";
    }

    public String simpleToString(){
        return this.name+"/"+this.writer;
    }
}
