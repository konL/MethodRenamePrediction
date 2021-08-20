package detectId.DS;

public class IdentifierDS {
	
	
	String classpar;
	String methodpar;
	String name;
	String type;
	String defaultvalue;
	int location;
	
	public String getClasspar() {
		return classpar;
	}
	public void setClasspar(String classpar) {
		this.classpar = classpar;
	}
	public String getMethodpar() {
		return methodpar;
	}
	public void setMethodpar(String methodpar) {
		this.methodpar = methodpar;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	
	public String getDefaultvalue() {
		return defaultvalue;
	}
	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}
	public IdentifierDS(String classpar, String methodpar, String name, String type, String defaultvalue,int location) {
		super();
		this.classpar = classpar;
		this.methodpar = methodpar;
		this.name = name;
		this.type = type;
		this.defaultvalue=defaultvalue;
		this.location = location;
	}
	 
	public String toString()
	{
		if(type.contains(","))
			type=type.replace(",", "");
		if(defaultvalue.contains(","))
			defaultvalue=defaultvalue.replace(",", "");
		if(name.contains(";"))
			name=name.replace(";", "");
		if(name.contains("\n"))
			name=name.replace("\n", "");
		if(name.contains("\r"))
			name=name.replace("\r", "");
		if(defaultvalue.contains("\n"))
			defaultvalue=defaultvalue.replace("\n", " ");
		if(defaultvalue.contains("\r"))
			defaultvalue=defaultvalue.replace("\r", " ");
		defaultvalue=defaultvalue.replaceAll(" +", " ");
		
		if(classpar.isEmpty())
			classpar=" ";
		if(methodpar.isEmpty())
			methodpar=" ";
		if(name.isEmpty())
			name=" ";
		if(type.isEmpty())
			type=" ";
		if(defaultvalue.isEmpty())
			defaultvalue=" ";
		String result=classpar+","+methodpar+","+name+","+type+","+defaultvalue+","+location;
		return result;		
	}
	
	

}
