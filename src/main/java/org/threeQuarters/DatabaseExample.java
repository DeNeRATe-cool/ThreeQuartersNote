package org.threeQuarters;

import Database.INote;
import Database.Note;
import Database.NoteAction;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DatabaseExample {

    public static void main(String[] args) {
        INote noteManager = new NoteAction();
        Note note = new Note("这是一个样例笔记", "数学分析", "杨佳宇轩", new Date(), "芜湖", UUID.randomUUID().toString());

        List<Note> allLIst = noteManager.queryAll();
        System.out.println(allLIst);

        System.out.println(noteManager.exist(note));
        noteManager.insert(note);
        System.out.println(noteManager.exist(note));

        String prompt = "我爱";
        List<Note> queryWithPrompt = noteManager.queryByNoteName(prompt);
        System.out.println(queryWithPrompt);

    }

}
