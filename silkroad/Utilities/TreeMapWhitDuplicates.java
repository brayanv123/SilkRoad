package Utilities;
import java.util.*;

/**
 * Write a description of class TreeMapWhitDuplicates here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TreeMapWhitDuplicates
{
    private TreeMap<Integer, ArrayList<Object>> map;
    
    public TreeMapWhitDuplicates()
    {
        map = new TreeMap<>();
    }
    
    public void put(int key, Object value){
        if(map.containsKey(key)){
            map.get(key).add(value);
        } else {
            map.put(key, new ArrayList<>());
            map.get(key).add(value);
        }
    }
    
    public void removeFirst(int key){
        if(map.containsKey(key)){
            map.get(key).remove(0);
        }
        if(map.get(key).isEmpty()){
            map.remove(key);
        }
    }
    
    public ArrayList<Object> get(int key){
        if(map.containsKey(key)){
            return map.get(key);
        } else return new ArrayList<Object>();
    }
    
    public ArrayList<Integer> getKeys(){
        return new ArrayList<>(map.keySet());
    }
}