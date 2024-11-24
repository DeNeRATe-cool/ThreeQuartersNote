package Database;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "sync")
public class SyncNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @Column(name = "course")
    private String course;

    @Column(name = "upload")
    private Date uploadTime;

    @Column(name = "content")
    private String content;

    @Column(name = "uuid")
    private String uuid;

    public SyncNote() {}
    public SyncNote(String username, String name, String path, String course, Date uploadTime, String content, String uuid) {
        this.username = username;
        this.name = name;
        this.path = path;
        this.course = course;
        this.uploadTime = uploadTime;
        this.content = content;
        this.uuid = uuid;
    }
    public SyncNote(String username, String name, String path, String course, Date uploadTime, String content) {
        // constructor without uuid
        this.username = username;
        this.name = name;
        this.path = path;
        this.course = course;
        this.uploadTime = uploadTime;
        this.content = content;
        this.uuid = null;
    }
    public SyncNote(String username, String name, String path, Date uploadTime, String content) {
        // constructor without course and uuid
        this.username = username;
        this.name = name;
        this.path = path;
        this.course = null;
        this.uploadTime = uploadTime;
        this.content = content;
        this.uuid = null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "SyncNote {" + " id = " + this.id + "," + " username = " + this.username + "," + " name = " + this.name + "," +
                " path = " + this.path + "," + " course = " + this.course + "," + " uploadTime = " + this.uploadTime + "," + " uuid = " + this.uuid + " }";
    }
}
