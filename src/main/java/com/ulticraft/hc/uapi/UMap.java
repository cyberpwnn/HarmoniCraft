package com.ulticraft.hc.uapi;

import java.util.HashMap;

public class UMap<K, V> extends HashMap<K, V>
{
	private static final long serialVersionUID = 1527847670799761130L;

	public UMap<K, V> copy()
	{
		UMap<K, V> m = new UMap<K, V>();

		for(K k : this.keySet())
		{
			m.put(k, get(k));
		}

		return m;
	}

	@Override
	public String toString()
	{
		UList<String> s = new UList<String>();

		for(K i : keySet())
		{
			s.add(i.toString() + ": " + get(i).toString());
		}

		return "[" + s.toString() + "]";
	}

	public UMap<K, V> append(UMap<K, V> umap)
	{
		for(K i : umap.keySet())
		{
			put(i, umap.get(i));
		}

		return this;
	}

	public UList<K> ukeys()
	{
		return new UList<K>(keySet());
	}

	public UList<V> uvalues()
	{
		return new UList<V>(values());
	}
	
	public void putNVD(K k, V v)
	{
		if(!containsValue(v))
		{
			put(k, v);
		}
	}

	public UList<V> get(UList<K> keys)
	{
		UList<V> ulv = new UList<V>();

		for(K i : keys)
		{
			if(get(i) != null)
			{
				ulv.add(get(i));
			}
		}

		return ulv;
	}

	public UMap<K, V> removeDuplicateKeys()
	{
		UMap<K, V> map = this.copy();
		UList<K> keys = map.ukeys().removeDuplicates();

		clear();

		for(K i : keys)
		{
			put(i, map.get(i));
		}

		return this;
	}

	public UMap<K, V> removeDuplicateValues()
	{
		UMap<K, V> map = this.copy();
		UList<K> keys = map.ukeys().removeDuplicates();

		clear();

		for(K i : keys)
		{
			put(i, map.get(i));
		}

		return this;
	}

	public void put(UList<K> k, UList<V> v)
	{
		if(k.size() != v.size())
		{
			return;
		}

		for(int i = 0; i < k.size(); i++)
		{
			put(k, v);
		}
	}
}
