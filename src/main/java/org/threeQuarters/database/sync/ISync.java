package org.threeQuarters.database.sync;

public interface ISync {

    /**
     * back up all .md note of the given root directory
     * clean all note have backed before first
     * @param folderPath absolute directory path
     * @param username online username
     * @throws IllegalArgumentException if path error or username lacked
     */
    void backUp(String folderPath, String username) throws IllegalArgumentException;

    /**
     * sync all notes of the online user no matter if the path existed
     * @param targetPath target path to save sync notes
     * @param username online username
     * @throws IllegalArgumentException if path or username error
     */
    void sync(String targetPath, String username) throws IllegalArgumentException;
}
