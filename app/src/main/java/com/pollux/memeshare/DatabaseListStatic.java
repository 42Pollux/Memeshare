package com.pollux.memeshare;

import java.util.ArrayList;

/**
 * Created by pollux on 20.04.17.
 */
class Element {
    String filepath = "";
    ArrayList<String> tags = new ArrayList<String>();
    Element next;

}
public class DatabaseListStatic {
    private Element first;
    private Element last;
    private int size;

    public static DatabaseListStatic init(){
        DatabaseListStatic l = new DatabaseListStatic();
        l.first = l.last = null;
        l.size = 0;
        return l;
    }

    public static DatabaseListStatic addElement(String _filepath, DatabaseListStatic l){
        Element e = new Element();
        e.filepath = _filepath;
        e.next = l.first;
        if(empty(l)){
            l.last = e;
        }
        l.first = e;
        l.size++;
        return l;
    }

    public static boolean empty(DatabaseListStatic l){
        return l.size==0;
    }

    public static boolean isin(String _filepath, DatabaseListStatic l){
        for(Element e = l.first; e!=null; e=e.next){
            if(e.filepath.equals(_filepath)) return true;
        }
        return false;
    }

    public static int length(DatabaseListStatic l){
        return l.size;
    }

    public static void addTag(String _filepath, String tag, DatabaseListStatic l){
        for(Element e = l.first; e!=null; e=e.next){
            if(e.filepath.equals(_filepath)) e.tags.add(tag);
        }
    }

    public static void removeTag(String _filepath, String tag, DatabaseListStatic l){
        for(Element e = l.first; e!=null; e=e.next){
            if(e.filepath.equals(_filepath)) e.tags.remove(tag);
        }
    }

    public static ArrayList<String> getTags(String _filepath, DatabaseListStatic l){
        ArrayList<String> list = new ArrayList<String>();
        for(Element e = l.first; e!=null; e=e.next){
            if(e.filepath.equals(_filepath)){
                if(e.tags.size()>0) {
                    return e.tags;
                } else {
                    break;
                }
            }
        }
        list.add("none");
        return list;
    }

    public static ArrayList<String> getLinesToWrite(DatabaseListStatic l){
        ArrayList<String> list = new ArrayList<String>();
        for(Element e = l.first; e!=null; e=e.next){
            list.add(e.filepath);
            String str = "";
            if(!e.tags.isEmpty()){
                for(String tag : e.tags){
                    str = str + tag + ";";
                }
            } else {
                str ="$notag";
            }
            list.add(str);
        }
        return list;
    }
}
