import org.junit.*;
import java.util.*;

import junit.framework.Test;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

	@Before
    public void setUp() {
       Fixtures.deleteAll();
   }


 /*   @Test
    public void CheckUser()
    {
    	// create Modules
    	
    	// create User
    	User gokul =new User("gokulvanan@gmail.com", "10FEB@bglr", "Gokulvanan","Chinmaya");
    	gokul.isAdmin=true;
//    	Module m =new Module("Time Table Generation").save();
    	gokul.addModules("Time Table Generation");
    	gokul.addModules("Report Card Management");
    	gokul.addModules("Student Profile Management");
    	gokul.save();
    	

    	
    	// fetchUser by Email
    	User test = User.find("byEmail", "gokulvanan@gmail.com").first();
    	assertEquals("Gokulvanan", test.fullName);
    	assertEquals(true, test.isAdmin);
    	// fetchUserList by access To Modules
    	Set<Module> m = test.modules;
    	for(Module me : m)
    		System.out.println(me.name);
    	assertEquals(Module.count("name", "Time Table Generation"),1);
    }*/
}
