package IOT.IOTApplication;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class IOTFilePersistenceManager<T> implements IOTPersistenceManager<T> {

	private String filename;
	private ArrayList<T> data;		
	
	public IOTFilePersistenceManager(String filename) {
		this.filename = filename;
		this.data = new ArrayList<T>();
	}
	
	@SuppressWarnings("unchecked")
	public void open() throws ClassNotFoundException, FileNotFoundException, IOException {
		File f = new File(filename);
		try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f))) {
			try {
				while(true) {
					data.add((T)stream.readObject());
				}
			} catch(EOFException e) {
				// EOF
			}
		}		
	}
	
	public void createNewDatastore(String filename) {
		this.filename = filename;
		data.clear();
		flush();
	}
	
	public void createNewDatastore() {
		createNewDatastore(filename);
	}
	
	@Override
	public List<T> findAll() {
		return data;
	}

	@Override
	public List<T> find(
			IOT.IOTApplication.IOTPersistenceManager.Predicate<T> predicate) {
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
			IOT.IOTApplication.IOTPersistenceManager.Predicate<T> predicate) {
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
	}

	@Override
	public void delete(T element) {
		data.remove(element);		
	}
	
	@Override
	public void delete(
			IOT.IOTApplication.IOTPersistenceManager.Predicate<T> predicate) {
		for (T cursor : data)
			if (predicate.where(cursor))
				data.remove(cursor);
	}
	
	public void flush() {
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
	
	@Override
	protected void finalize() throws Throwable {
		flush();
		super.finalize();
	}

}
