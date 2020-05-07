package dataStructure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/** * @author  ���� :GiantJun 
* @date ����ʱ�䣺2019��6��27�� ����4:29:20 
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

	//����valueֵ��ɾ��map�е�Ԫ��
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
	//��дmap��put������ʹ�� ����ֵ �Ե�����Ψһ������������ֵͬ��Ӧ��ͬ���������
	public synchronized V put(K key, V value) {
		for(V val:valueSet()) {
			if(val.equals(value) && val.hashCode()==value.hashCode()) {
				throw new RuntimeException("OnlineMapʵ���в��������ظ���valueֵ��");
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
	
	public Set<K> getKeySet(){
		Set<K> result = keySet();
		return result;
	}
}
