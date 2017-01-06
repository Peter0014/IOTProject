package IOT.IOTApplication;

import java.util.List;	

public interface IOTPersistenceManager<T extends Object> {
	
	/** returns true if element is to be selected from Datastore
	 *  
	 * @author penderiko
	 *
	 * @param <T> element
	 */
	public abstract class Predicate<T> {
		public abstract boolean where(T element);
	}
	
	List<T> findAll();	
	List<T> find(Predicate<T> predicate);
	List<T> find(T element);
	T findFirst(Predicate<T> predicate);
	T findFirst(T element);
	void add(T element);
	void delete(T element);
	void delete(Predicate<T> predicate);
}	