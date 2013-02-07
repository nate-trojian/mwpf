package mwpf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class HashList<T1, T2>
{
	private HashMap<T1, T2> map = new HashMap<T1, T2>();
	private ArrayList<T1> list = new ArrayList<T1>();
	
	public HashList()
	{
	}
	
	public void add(T1 key, T2 val)
	{
		list.add(key);
		map.put(key, val);
	}
	
	public void add(T1 key, T2 val, int index)
	{
		list.add(index, key);
		map.put(key, val);
	}
	
	public void remove(T1 key)
	{
		list.remove(key);
		map.remove(key);
	}
	
	public void remove(int index)
	{
		T1 e = list.get(index);
		list.remove(index);
		map.remove(e);
	}
	
	public T2 get(T1 key)
	{
		return map.get(key);
	}
	
	public T2 get(int index)
	{
		return map.get(list.get(index));
	}
	
	public T1 getKeyAt(int index)
	{
		return list.get(index);
	}
	
	public int indexOf(T1 key)
	{
		return list.indexOf(key);
	}
	
	public boolean containsKey(T1 key)
	{
		return map.containsKey(key);
	}
	
	public Iterator<T1> iterator()
	{
		return list.iterator();
	}
	
	public int size()
	{
		return list.size();
	}
	
	public Set<T1> keySet()
	{
		return map.keySet();
	}
	
	public Collection<T2> values()
	{
		return map.values();
	}
}
