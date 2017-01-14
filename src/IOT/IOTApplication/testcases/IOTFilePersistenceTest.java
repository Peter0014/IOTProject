package IOT.IOTApplication.testcases;

import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import IOT.IOTApplication.dao.IOTFilePersistenceManager;
import IOT.IOTApplication.dao.IOTPersistenceManagerInterface;

public class IOTFilePersistenceTest {

	@Test
	public void test() throws Exception {
		
			Path currentRelativePath = Paths.get("");
			System.out.println(currentRelativePath.toAbsolutePath().toString()); 
			
			IOTFilePersistenceManager<String> pm = new IOTFilePersistenceManager<>("test69");
			pm.add("hi");
			pm.add("y'all");
			
			//pm.flush();
			pm = new IOTFilePersistenceManager<>("test69");
			
			
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
