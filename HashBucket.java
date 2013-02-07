package mwpf;

import java.util.HashMap;
import java.util.Iterator;

public class HashBucket<T1,T2,T3>
{
    HashMap<T1,HashMap<T2,T3>> map;
    public HashBucket()
    {
	map = new HashMap<T1,HashMap<T2,T3>>();
    }

    public void addBucket(T1 name)
    {
	map.put(name, new HashMap<T2,T3>());
    }

    public void addToBucket(T1 name, T2 key, T3 value)
    {
	map.get(name).put(key, value);
    }
    
    public void removeFromBucket(T1 name, T2 key)
    {
	map.get(name).remove(key);
    }
    
    public void removeBucket(T1 name, T2 key)
    {
	map.remove(key);
    }

    public HashMap<T2,T3> getBucket(T1 name)
    {
	return map.get(name);
    }

    public T3 getFromBucket(T1 name, T2 key)
    {
	return map.get(name).get(key);
    }

    public boolean bucketContains(T1 name, T3 value)
    {
	return map.get(name).containsValue(value);
    }
    
    public Iterator<HashMap<T2,T3>> bucketIterator()
    {
	return map.values().iterator();
    }
    
    public Iterator<T3> valuesIterator(T1 name)
    {
	return map.get(name).values().iterator();
    }
}