package org.threeQuarters.LocalUserInfo;

import javafx.scene.image.Image;

import java.io.Serializable;

public class User implements Serializable {

    private String ID;
    private String PassWord;
    private Image head;
    private boolean savePwd;

    public User(String ID,String passWord) {
        this.ID = ID;
        this.PassWord = passWord;
    }

    public User(String ID, String PassWord, Image head) {
        this.ID = ID;
        this.PassWord = PassWord;
        this.head = head;
    }

    public boolean getSavePassword() {
        return savePwd;
    }
    public void setSavePassword(boolean savePwd) {
        this.savePwd = savePwd;
    }

    public User(){}

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public String getPassWord() {
        return PassWord;
    }
    public void setPassWord(String PassWord) {
        this.PassWord = PassWord;
    }
    public Image getHead() {
        return head;
    }
    public void setHead(Image head) {
        this.head = head;
    }

    public String toString(){
        return ID+" "+PassWord;
    }

}
