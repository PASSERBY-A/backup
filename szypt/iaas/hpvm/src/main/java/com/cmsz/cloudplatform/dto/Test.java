package com.cmsz.cloudplatform.dto;


//import com.thoughtworks.xstream.XStream;

public class Test {
	
	class Student{
		private int id;   
		private String name;   
		private String email;  
		private String address; 
		private Birthday birthday;
		@Override
		public String toString() {
			return String
					.format("Student [id=%s, name=%s, email=%s, address=%s, birthday=%s]",
							id, name, email, address, birthday);
		}  
	};
	
	class Birthday{
		public String birthday;
		public int id;
	}
	
	public void readXML4InputStream() {   
		 try {       
			/* XStream xstream = new XStream();
			 xstream.alias("student", Student.class);
			 xstream.alias("birthday", Birthday.class);
			 xstream.useAttributeFor(Birthday.class, "id");
			 xstream.useAttributeFor(Birthday.class, "birthday");
			// xstream.addImplicitCollection(List.class, "student");
			String xmlString = "<list>    <student id=\"3\" name=\"jack\">      <id>1</id>      <email>jack@email.com</email>      <address>china</address>      <birthday id=\"4\" birthday=\"2010-11-22\"/>    </student>     <student id=\"5\" name=\"tom\">      <id>2</id>      <email>tom@125.com</email>      <address>china</address>      <birthday id=\"6\" birthday=\"2010-11-22\"/>    </student>  </list>";  
			//Student student = (Student)xstream.fromXML(xmlString);
			java.util.List list = (java.util.List) xstream.fromXML(xmlString);  
			 System.out.println(list);*/
		  }    
		 catch (Exception e) { 
			 e.printStackTrace();    
		 }
	}
	
	public static void main(String[] args) {
		Test test = new Test();
		test.readXML4InputStream();
	}
		 
}
