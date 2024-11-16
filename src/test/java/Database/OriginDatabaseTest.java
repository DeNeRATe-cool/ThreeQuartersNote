package Database;

import java.util.Date;
import java.util.UUID;

public class OriginDatabaseTest {

    private static final NoteAction action = new NoteAction();

    public static void TestInsert() {
        Note note_1 = new Note("我爱Java", "面向对象程序设计（Java）", "庄耿雄", new Date(), "Java请对我好点~~~", UUID.randomUUID().toString());
        Note note_2 = new Note("我爱计组", "计算机硬件基础（软件专业）", "孙健钧", new Date(), "计组球球了!!!", UUID.randomUUID().toString());
        action.insert(note_1);
        action.insert(note_2);
    }

    public static void TestQueryAll() {
        System.out.println(action.queryAll());
    }

    public static void TestExist() {
        Note note = new Note("我爱算法", "庄耿雄", new Date(), "如果是宋老师的话，也不是不行~", "bb640128-e5d3-4918-b297-7f1e7150c6d8");
        if(action.exist(note)) {
            System.out.println("existed!!!");
        } else {
            System.out.println("not existed!!!");
//            action.insert(note);
        }
    }

    public static void TestQueryByCourse() {
        String course = "马克思";
        System.out.println(action.queryByCourse(course));
    }

    public static void TestQueryByNoteName() {
        String noteName = "我";
        System.out.println(action.queryByNoteName(noteName));
    }

    public static void TestQueryByWriter() {
        String writer = "杨佳宇轩";
        System.out.println(action.queryByWriter(writer));
    }

    public static void TestDelete() {
        Note note = new Note("", "", new Date(), "", "bb640128-e5d3-4918-b297-7f1e7150c6d8");
        action.delete(note);
        System.out.println(action.queryAll());
    }

    public static void TestUpdate() {
        Note note = new Note("我爱程设", "程序设计基础", "庄耿雄", new Date(), "程设爱你哟~", "e5e3b96d-3f67-46eb-b399-aca307d4fa82");
        action.update(note);
        System.out.println(action.queryAll());
    }

    public static void main(String[] args) {
//        TestInsert();
//        TestQueryAll();
//        TestExist();
//        TestQueryByCourse();
//        TestQueryByNoteName();
//        TestQueryByWriter();
//        TestDelete();
//        TestUpdate();
    }
}
