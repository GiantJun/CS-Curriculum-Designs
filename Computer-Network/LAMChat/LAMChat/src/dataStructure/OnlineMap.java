package dataStructure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/** * @author  作者 :GiantJun 
* @date 创建时间：2019年6月27日 下午4:29:20 
* @version 1.0 
* @parameter  
* @return 
* @throws 
*/
public class OnlineMap <K,V> extends HashMap<K,V>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//根据value值来删除map中的元素
	public void removeByValue(Object value) {
		for(Object key:keySet()) {
			if(get(key) == value) {
				remove(key);
				break;
			}
		}
	}
	
	public K getKeyfromValue(Object value) {
		for(K key:keySet()) {
			if(get(key) == value) {
				return key;
			}
		}
		return null;
	}
	//重写map的put方法，使得 键—值 对的内容唯一（即不存在相同值对应不同键的情况）
	public synchronized V put(K key, V value) {
		for(V val:valueSet()) {
			if(val.equals(value) && val.hashCode()==value.hashCode()) {
				throw new RuntimeException("OnlineMap实例中不允许含有重复的value值！");
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
	
	public Set<K> getKeySet(){
		Set<K> result = keySet();
		return result;
	}
}
