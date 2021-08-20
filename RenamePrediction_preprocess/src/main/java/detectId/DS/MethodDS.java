package detectId.DS;

import java.util.Vector;

public class MethodDS {
	
	
	String methodname;
	Vector<IdentifierDS> parameters;
	String returntype;
	String outcomment;
	String incomment;
	int beginindex;
	int endindex;
	String body;
	public String getMethodname() {
		return methodname;
	}
	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	public Vector<IdentifierDS> getParameters() {
		return parameters;
	}
	public void setParameters(Vector<IdentifierDS> parameters) {
		this.parameters = parameters;
	}
	public String getReturntype() {
		return returntype;
	}
	public void setReturntype(String returntype) {
		this.returntype = returntype;
	}
	public String getOutcomment() {
		return outcomment;
	}
	public void setOutcomment(String outcomment) {
		this.outcomment = outcomment;
	}
	public String getIncomment() {
		return incomment;
	}
	public void setIncomment(String incomment) {
		this.incomment = incomment;
	}
	public int getBeginindex() {
		return beginindex;
	}
	public void setBeginindex(int beginindex) {
		this.beginindex = beginindex;
	}
	public int getEndindex() {
		return endindex;
	}
	public void setEndindex(int endindex) {
		this.endindex = endindex;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public MethodDS(String methodname, Vector<IdentifierDS> parameters, String returntype, String outcomment,
			String incomment, int beginindex, int endindex, String body) {
		super();
		this.methodname = methodname;
		this.parameters = parameters;
		this.returntype = returntype;
		this.outcomment = outcomment;
		this.incomment = incomment;
		this.beginindex = beginindex;
		this.endindex = endindex;
		this.body = body;
	}
	
	
	

}
