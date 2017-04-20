package com.pollux.memeshare;

import java.util.ArrayList;

/**
 * Created by pollux on 12.04.17.
 */

public class DatabaseList {
    private String _filepath = null;
    private ArrayList<String> tags = new ArrayList<String>();
    private DatabaseList _next = null;


    public DatabaseList(String filepath){
        this._filepath = filepath;
        this._next = null;
    }

    public DatabaseList addElement(String filepath){
        DatabaseList p = new DatabaseList(filepath);
        p._next = this;

        return p;
    }

    public boolean isin(String filepath){
        boolean bool = false;
        DatabaseList a = new DatabaseList(this._filepath);
        for(DatabaseList p = this; p!=null; p = p._next){
            a.addElement(p._filepath);
            if(p._filepath==filepath){
                bool = true;
            }
        }
        DatabaseList b = new DatabaseList(a._filepath);
        for(DatabaseList p = a; p!=null; p=p._next){
            b.addElement(a._filepath);
        }

        return bool;
    }

    public DatabaseList removeElement(String filepath){
        DatabaseList a = new DatabaseList(this._filepath);
        for(DatabaseList p = this; p!=null; p = p._next){
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
        DatabaseList a = new DatabaseList(this._filepath);
        for(DatabaseList p = this; p!=null; p = p._next){
            a.addElement(p._filepath);
            if(p._filepath==filepath){
                p.tags.add(tag);
            }
        }
        DatabaseList b = new DatabaseList(a._filepath);
        for(DatabaseList p = a; p!=null; p=p._next){
            b.addElement(a._filepath);
        }
    }

    public void removeTag(String filepath, String tag){
        DatabaseList a = new DatabaseList(this._filepath);
        for(DatabaseList p = this; p!=null; p = p._next){
            a.addElement(p._filepath);
            if(p._filepath==filepath){
                p.tags.remove(tag);
            }
        }
        DatabaseList b = new DatabaseList(a._filepath);
        for(DatabaseList p = a; p!=null; p=p._next){
            b.addElement(a._filepath);
        }
    }


}
