package IOT.IOTApplication.testcases;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import IOT.IOTApplication.dao.IOTFilePersistenceManager;
import IOT.IOTApplication.dao.IOTPersistenceManagerInterface;

public class IOTFilePersistenceTest {

	@Test
	public void test() throws Exception {
		
			IOTFilePersistenceManager<String> pm = new IOTFilePersistenceManager<>("test");
			pm.add("hi");
			pm.add("y'all");
			
			//pm.flush();
			pm = new IOTFilePersistenceManager<>("test");
			
			
			List<String> res = pm.find(new IOTPersistenceManagerInterface.Predicate<String>() {
				@Override
				public boolean where(String element) {
					return element.equals("hi");
				}
			});
			System.out.println(res.toString());
			assertTrue(res.toString(), res.get(0).equals("hi"));
		 
	}

}
