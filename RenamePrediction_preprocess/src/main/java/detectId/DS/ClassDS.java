package detectId.DS;


import java.util.Vector;

public class ClassDS {
	
	String classname;
	Vector<MethodDS> methodlist;
	Vector<IdentifierDS> fieldlist;
	Vector<MethodDS> contructorlist;
	int index;
	
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public Vector<MethodDS> getMethodlist() {
		return methodlist;
	}
	public void setMethodlist(Vector<MethodDS> methodlist) {
		this.methodlist = methodlist;
	}
	public Vector<IdentifierDS> getFieldlist() {
		return fieldlist;
	}
	public void setFieldlist(Vector<IdentifierDS> fieldlist) {
		this.fieldlist = fieldlist;
	}
	
	public Vector<MethodDS> getContructorlist() {
		return contructorlist;
	}
	public void setContructorlist(Vector<MethodDS> contructorlist) {
		this.contructorlist = contructorlist;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public ClassDS(String classname, Vector<MethodDS> methodlist, Vector<IdentifierDS> fieldlist, Vector<MethodDS> constructorlist, int index) {
		super();
		this.classname = classname;
		this.methodlist = methodlist;
		this.fieldlist = fieldlist;
		this.contructorlist=constructorlist;
		this.index=index;
	}
	

}
