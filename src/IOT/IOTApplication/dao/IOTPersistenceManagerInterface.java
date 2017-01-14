package IOT.IOTApplication.dao;

import java.util.List;	
/**
 * This interface defines methods to add, find and delete objects in its datastore
 * @author René Eichinger
 *
 * @param <T>
 */
public interface IOTPersistenceManagerInterface<T extends Object> {
	
	/** returns true if element is to be selected from datastore
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
	boolean exists(T element);
	void add(T element);
	void delete(T element);
	void delete(Predicate<T> predicate);
}	