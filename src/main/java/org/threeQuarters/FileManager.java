package org.threeQuarters;


import java.util.ArrayList;

public class FileManager {

    private ArrayList<FileEditor> fileEditors;

    private static FileManager instance;

    private FileManager(){}

    public static synchronized FileManager getInstance(){
        if(instance == null){
            instance = new FileManager();
        }
        return instance;
    }

    public void initialized()
    {
        fileEditors = new ArrayList<>();
    }



}
