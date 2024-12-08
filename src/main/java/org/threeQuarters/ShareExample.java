package org.threeQuarters;

import database.DirectoryInitial;
import database.note.INote;
import database.note.Note;
import database.note.NoteAction;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ShareExample {

    public static void main(String[] args) {

        DirectoryInitial.initial();

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

//        List<Note> all = noteManager.queryAll();
//        int len = all.size();
//        for(int i = len-1;i >= len / 2;i--)
//        {
//            noteManager.delete(all.get(i));
//        }

    }

}
