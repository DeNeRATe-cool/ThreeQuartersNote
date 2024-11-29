package org.threeQuarters.FileMaster;

public interface ILocalFile {

    public String getName();
    public String getAbsolutePath();
    public String getContent();
    public void setContent(String content);

    public boolean isShared();
    public void setShared(boolean shared);
}
