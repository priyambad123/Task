package com.apkglobal.cheruvu;

/**
 * Created by HellBlazer on 18-04-2018.
 */

import com.google.firebase.database.IgnoreExtraProperties;



@IgnoreExtraProperties
public class User {

    public String serial;
    public String name;
    public String age;
    public String mandal;
    public String village;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)

    public User(String serial, String name, String age, String mandal, String village) {
        this.serial = serial;
        this.name = name;
        this.age = age;
        this.mandal=mandal;
        this.village=village;
    }
    public User() {}
}
