package org.threeQuarters.DataScrapper;

import org.threeQuarters.options.Options;

import java.util.ArrayList;
import java.util.List;

public class Crawler {

    public static Crawler instance = null;
    private String number;
    private String password;

    private Crawler(String number,String password)
    {
        this.number = number;
        this.password = password;
        Options.setBuaaID(number);
        Options.setBuaaPassword(password);
    }

    public static Crawler getInstance() {
        if (instance == null) {
            instance = new Crawler(Options.getBuaaID(),Options.getBuaaPassword());
            return instance;
        }
        else return instance;
    }

    public void ChangeUser()
    {
        if(!Options.getBuaaID().equals(number) || !Options.getBuaaPassword().equals(password))
        {
            instance = new Crawler(Options.getBuaaID(),Options.getBuaaPassword());
        }
    }

    private boolean Online;

    private List<String> Courses;
    private List<String> Teachers;
    private List<String> TimeTable;

    public static boolean userAccept(String number,String password)
    {
        ICrawlable crawler = ClassRoomExecutor.getInstance();

        crawler.initial(number,password,"");
        return crawler.login();
    }

    public void setOnline(boolean Online)
    {
        this.Online = Online;
    }

    public boolean getOnline(boolean Online)
    {
        return Online;
    }


    public static void main(String[] args)
    {
        System.out.println(Crawler.userAccept("22374271","123456"));
    }

}
