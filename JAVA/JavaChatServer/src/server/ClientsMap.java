package server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ClientsMap<K,V> extends HashMap<K,V> {
	
	//根据value值来删除map中的元素
	public void removeByValue(Object value) {
		for(Object key:keySet()) {
			if(get(key) == value) {
				remove(key);
				break;
			}
		}
	}
	//重写map的put方法，使得map中的元素没有重复
	public synchronized V put(K key, V value) {
		for(V val:valueSet()) {
			if(val.equals(value) && val.hashCode()==value.hashCode()) {
				throw new RuntimeException("ClientMap实例中不允许含有重复的value值！");
			}
		}
		return super.put(key, value);
	}
	//获取value的集合
	public Set<V> valueSet(){
		Set<V> result = new HashSet<>();
		for(K key : keySet()) {
			result.add(get(key));
		}
		return result;
	}
}
