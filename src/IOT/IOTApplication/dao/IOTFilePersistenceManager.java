package IOT.IOTApplication.dao;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * The IOTFilePersistenceManager is responsible for creation of datastore,
 * handles adding, finding and deleting and finally persistence of data (flush)
 * @author René Eichinger
 *
 * @param <T>
 */
public class IOTFilePersistenceManager<T> implements IOTPersistenceManagerInterface<T> {

	private String filename;
	private List<T> data;		
	
	public IOTFilePersistenceManager(String filename) {
		
		Path currentRelativePath = Paths.get("");
		//this.filename = currentRelativePath.toAbsolutePath().toString() + '/' + filename;
		
		this.filename = "/var/lib/IOT/" + filename;
				
		this.data = Collections.synchronizedList(new ArrayList<T>());
		try {
			open();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void open() throws ClassNotFoundException, IOException {
		ObjectInputStream stream;
			try {
				File f = new File(filename);
				
				if (!f.exists()) {
					try {
						f.createNewFile();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println(f.getAbsolutePath());
				stream = new ObjectInputStream(new FileInputStream(f));
				while(true) {
					data.add((T)stream.readObject());
				}
			} catch(EOFException e) {
				
			} catch (FileNotFoundException e) {
				createNewDatastore();
			}
	}
	
	public void createNewDatastore(String filename) {
		this.filename = filename;
		data.clear();
		flush();
	}
	
	public void createNewDatastore() {
		createNewDatastore(filename);
		File f = new File(filename);
		System.out.println(f.getAbsolutePath());
	}
	
	@Override
	public List<T> findAll() {
		return data;
	}

	@Override
	public List<T> find(
			IOT.IOTApplication.dao.IOTPersistenceManagerInterface.Predicate<T> predicate) {
		ArrayList<T> result = new ArrayList<>();
		for (T element : data)
			if (predicate.where(element))
				result.add(element);
		return result;
	}
	
	@Override
	public List<T> find(T element) {
		ArrayList<T> result = new ArrayList<>();

		for (T cursor : data)
			if (element.equals(cursor))
				result.add(cursor);
		return result;
	};

	@Override
	public T findFirst(
			IOT.IOTApplication.dao.IOTPersistenceManagerInterface.Predicate<T> predicate) {
		for (T element : data)
			if (predicate.where(element)) {
				return element;
			}
		return null;
	}
	
	@Override
	public T findFirst(T element) {
		for (T cursor : data)
			if (element.equals(cursor)) {
				return element;
			}
		return null;
	}
	

	@Override
	public void add(T element) {
		data.add(element);
		flush();
	}

	@Override
	public void delete(T element) {
		data.remove(element);
		flush();
	}
	
	@Override
	public void delete(
			IOT.IOTApplication.dao.IOTPersistenceManagerInterface.Predicate<T> predicate) {
		for (T cursor : data)
			if (predicate.where(cursor))
				data.remove(cursor);
		flush();
	}

	/**
	 * use flush() to write data store to its file location
	 */
	private synchronized void flush() {
		File f = new File(filename);
		try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(f))) {
			for (T element : data)
				stream.writeObject(element);
			stream.flush();
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

}
