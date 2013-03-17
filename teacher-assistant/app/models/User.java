package models;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class User extends Model{
	
	@Required
	@Email
	public String email;
	@Required
	public String password;
	@Required
	public String fullName;
	@Required
	@OneToOne(fetch=FetchType.LAZY)
	public School school;
	public boolean isAdmin;
	@Required
	public String userType;
	
	@OneToOne(fetch=FetchType.LAZY)
	public Teacher tchr;
	
/*	@OneToOne
	public Student student;
	*/
    @ManyToMany (cascade=CascadeType.ALL)
	public Set<Module> modules;
	
	
	public User(String email,String pwd, String fullName, School school)
	{
		this.email=email;
		this.password=pwd;
		this.fullName=fullName;
		this.school=school;
		this.userType="Teacher";
		this.modules=new LinkedHashSet<Module>();
	}

	public static User login(String email, String pwd)
	{
		return find("byEmailAndPassword",email,pwd).first();
	}
	
	public void addModules(String moduleName,String url)
	{
		this.modules.add(new Module(moduleName,url));
	}

	
	
	
}
