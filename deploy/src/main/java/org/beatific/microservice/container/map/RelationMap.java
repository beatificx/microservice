package org.beatific.microservice.container.map;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RelationMap<K,V>  {
	public int size();

	public boolean isEmpty();

	public boolean containsKey(Object key);
	
	public boolean containsValue(Object value);
	
	public boolean contains(Object key, Object value);

	public List<V> get(Object key);
	
	public void put(K key, V value);

	public List<V> remove(Object key);

	public void putAll(Map<? extends K, ? extends V> m);

	public void clear();

	public Set<K> keySet();

	public Collection<V> values();
}