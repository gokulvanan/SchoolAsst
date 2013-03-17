package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.Required;
import java.util.*;

import models.*;

public class Login extends Controller {

    public static void index() {
    	String moduleId="index";
        render(moduleId);
    }
    
    public static void authenticate( @Required String email, @Required  String password){
    	String msg="";
    	if(validation.hasErrors()) {  msg="Please enter email / pasword";  render("Login/index.html", msg);    }
    	User obj = User.login(email, password);
    	if(obj == null) {  msg="Invaid Email / Password"; 	  render("Login/index.html", msg);    }
    	session.put("id", obj.id);
    	session.put("name", obj.fullName);
    	Iterator<Module> mod = obj.modules.iterator();
    	if(mod.hasNext())
    	redirect(mod.next().url);
    }
    
    public static void redirect()
    {
    	User obj = User.findById(Long.parseLong(session.get("id")));
    	Iterator<Module> mod = obj.modules.iterator();
    	if(mod.hasNext())
    	redirect(mod.next().url);
    	
    }
    public static void sendMail(String mailBody)
    {
    	String emailMsg="Mail Sent SuccessFully";
    	System.out.println("mailBody");
    	System.out.println(mailBody);
    	render("Login/index.html", emailMsg);
    }
    
    public static void logout()
    {
    	session.clear();
    	String msg="You have Been logged Out";
    	render("Login/index.html", msg);
    }
}