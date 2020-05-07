package server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ClientsMap<K,V> extends HashMap<K,V> {
	
	//����valueֵ��ɾ��map�е�Ԫ��
	public void removeByValue(Object value) {
		for(Object key:keySet()) {
			if(get(key) == value) {
				remove(key);
				break;
			}
		}
	}
	//��дmap��put������ʹ��map�е�Ԫ��û���ظ�
	public synchronized V put(K key, V value) {
		for(V val:valueSet()) {
			if(val.equals(value) && val.hashCode()==value.hashCode()) {
				throw new RuntimeException("ClientMapʵ���в��������ظ���valueֵ��");
			}
		}
		return super.put(key, value);
	}
	//��ȡvalue�ļ���
	public Set<V> valueSet(){
		Set<V> result = new HashSet<>();
		for(K key : keySet()) {
			result.add(get(key));
		}
		return result;
	}
}
