package com.pollux.memeshare;

/**
 * Created by pollux on 12.04.17.
 */

public class DatabaseList {
    private String _filepath;
    private String[] _tags = new String[10];
    private DatabaseList _next;

    public DatabaseList(String filepath){
        this._filepath = filepath;
        this._next = null;
    }

    public DatabaseList addElement(String filepath){
        DatabaseList p = new DatabaseList(filepath);
        p._next = this;

        return p;
    }

    public static boolean isin(String filepath, DatabaseList list){
        return true;
    }

    public static DatabaseList removeElement(String filepath, DatabaseList list){
        DatabaseList a = new DatabaseList(list._filepath);
        for(DatabaseList p = list; p!=null; p = p._next){
            if(!(p._filepath==filepath)){
                a.addElement(p._filepath);
            }
        }
        DatabaseList b = new DatabaseList(a._filepath);
        for(DatabaseList p = a; p!=null; p=p._next){
            b.addElement(a._filepath);
        }

        return b;
    }

    public void addTag(String filepath, String tag){

    }

    public void removeTag(String filepath, String tag){

    }


}
