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
            a = a.addElement(p._filepath);
            if(p._filepath==filepath){
                bool = true;
            }
        }
        DatabaseList b = new DatabaseList(a._filepath);
        for(DatabaseList p = a; p!=null; p=p._next){
            b = b.addElement(p._filepath);
        }
        this._filepath = b._filepath;
        this._next = b._next;
        return bool;
    }

    public DatabaseList removeElement(String filepath){
        DatabaseList a = new DatabaseList(this._filepath);
        for(DatabaseList p = this; p!=null; p = p._next){
            if(!(p._filepath==filepath)){
                a = a.addElement(p._filepath);
            }
        }
        DatabaseList b = new DatabaseList(a._filepath);
        for(DatabaseList p = a; p!=null; p=p._next){
            b = b.addElement(p._filepath);
        }

        return b;
    }

    public void addTag(String filepath, String tag){
        DatabaseList a = new DatabaseList(this._filepath);
        for(DatabaseList p = this; p!=null; p = p._next){
            a = a.addElement(p._filepath);
            if(p._filepath==filepath){
                p.tags.add(tag);
            }
        }
        DatabaseList b = new DatabaseList(a._filepath);
        for(DatabaseList p = a; p!=null; p=p._next){
            b = b.addElement(p._filepath);
        }
        this._filepath = b._filepath;
        this._next = b._next;
    }

    public void removeTag(String filepath, String tag){
        DatabaseList a = new DatabaseList(this._filepath);
        for(DatabaseList p = this; p!=null; p = p._next){
            a = a.addElement(p._filepath);
            if(p._filepath==filepath){
                p.tags.remove(tag);
            }
        }
        DatabaseList b = new DatabaseList(a._filepath);
        for(DatabaseList p = a; p!=null; p=p._next){
            b = b.addElement(p._filepath);
        }
        this._filepath = b._filepath;
        this._next = b._next;
    }

    public ArrayList<String> getLinesToWrite(){
        ArrayList<String> list = new ArrayList<String>();
        DatabaseList a = new DatabaseList(this._filepath);

        for(DatabaseList p = this; p!=null; p = p._next){
            a = a.addElement(p._filepath);
            list.add(p._filepath);
            String tagString = new String("");
            if(p.tags.isEmpty()){
                tagString = "$notag";
            } else {
                for(String tag : p.tags){
                    tagString = tagString + tag + ";";
                }
            }
            list.add(tagString);

        }
        DatabaseList b = new DatabaseList(a._filepath);
        for(DatabaseList p = a; p!=null; p=p._next){
            b = b.addElement(p._filepath);
        }
        this._filepath = b._filepath;
        this._next = b._next;
        return list;
    }

    public int size(){
        int i = 0;
        DatabaseList a = new DatabaseList(this._filepath);
        for(DatabaseList p = this; p!=null; p = p._next){
            a = a.addElement(p._filepath);
            i++;
        }
        DatabaseList b = new DatabaseList(a._filepath);
        for(DatabaseList p = a; p!=null; p=p._next){
            b = b.addElement(p._filepath);
        }
        this._filepath = b._filepath;
        this._next = b._next;
        return i;
    }


}
