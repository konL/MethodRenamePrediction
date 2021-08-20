package detectId.ParseInfo;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import detectId.DS.ClassDS;
import detectId.DS.IdentifierDS;
import detectId.DS.MethodDS;

import java.util.List;
import java.util.Optional;
import java.util.Vector;


public class ClassCollector extends VoidVisitorAdapter<Vector<ClassDS>> {
   
	@Override
    public void visit(ClassOrInterfaceDeclaration cORid, Vector<ClassDS> classNameSet) 
    {
//        classNameSet.add(cORid.getNameAsString()); //������
        
        Vector<MethodDS> methodlist=new Vector<MethodDS>();
        Vector<MethodDS> constructorlist=new Vector<MethodDS>();
        List<MethodDeclaration> methods=cORid.getMethods();
        
        
        
        List<ConstructorDeclaration> constructors=cORid.getConstructors();
        for(ConstructorDeclaration method:constructors)
        {
           Vector<IdentifierDS> paralist=new Vector<IdentifierDS>();
        	
        	String location=method.getBegin().toString();
        	int beginindex=Integer.parseInt(location.substring(location.indexOf("line")+4, location.indexOf(",")).trim());
        	
        	List<Parameter> paras=method.getParameters();
        	if(!paras.isEmpty())
        	{
	        	for(Parameter para:paras)
	        	{
//	        		System.out.println(para.getNameAsString()+" "+para.getType()); // �������ƺ�����
	        		IdentifierDS onepara=new IdentifierDS(cORid.getNameAsString(),method.getNameAsString(),para.getNameAsString(),para.getType().toString(),"",beginindex);
	        		paralist.add(onepara);
	        	}
        	}
        	  
        	 
        	String outcomment=method.getComment().toString();
        	String incomment=method.getAllContainedComments().toString();
        	
        	location=method.getEnd().toString();
        	int endindex=Integer.parseInt(location.substring(location.indexOf("line")+4, location.indexOf(",")).trim());
        	String body=method.getBody().toString();
        	
        	MethodDS newmethod=new MethodDS(method.getNameAsString(),paralist,"",outcomment,incomment,beginindex,endindex,body);
        	
        	constructorlist.add(newmethod);
        }
        
       
        for(MethodDeclaration method:methods)
        {
//        	System.out.println(method.getNameAsString());  //��������
        	
        	Vector<IdentifierDS> paralist=new Vector<IdentifierDS>();
        	
        	String location=method.getBegin().toString();
        	int beginindex=Integer.parseInt(location.substring(location.indexOf("line")+4, location.indexOf(",")).trim());
        	
        	List<Parameter> paras=method.getParameters();
        	if(!paras.isEmpty())
        	{
	        	for(Parameter para:paras)
	        	{
//	        		System.out.println(para.getNameAsString()+" "+para.getType()); // �������ƺ�����
	        		IdentifierDS onepara=new IdentifierDS(cORid.getNameAsString(),method.getNameAsString(),para.getNameAsString(),para.getType().toString(),"",beginindex);
	        		paralist.add(onepara);
	        	}
        	}
        	 
        	String returntype=method.getType().toString();
        	String outcomment=method.getComment().toString();
        	String incomment=method.getAllContainedComments().toString();
        	
        	location=method.getEnd().toString();
        	int endindex=Integer.parseInt(location.substring(location.indexOf("line")+4, location.indexOf(",")).trim());
        	String body=method.getBody().toString();
        	
        	MethodDS newmethod=new MethodDS(method.getNameAsString(),paralist,returntype,outcomment,incomment,beginindex,endindex,body);
        	
        	methodlist.add(newmethod);
        	
//        	System.err.println(method.getType()); //������������        	
//        	System.err.println(method.getComment().toString());  //�������е�ǰ��ע��        	 
//        	System.err.println(method.getAllContainedComments().toString()); //����������Ƕ��ע��        	
//        	System.err.println(method.getRange()+"  "+method.getBegin()+"  "+method.getEnd());   //�����ķ�Χ       	
//        	System.err.println(method.getBody().toString());                    
        }
       
        
        Vector<IdentifierDS> fieldlist=new Vector<IdentifierDS>();
        List<FieldDeclaration> fields=cORid.getFields();
        for(FieldDeclaration field:fields)
        {
        	
        	
        	String location=field.getBegin().toString();
        	int beginindex=Integer.parseInt(location.substring(location.indexOf("line")+4, location.indexOf(",")).trim());
        	
        	
        	String type=field.getElementType().toString();
        	
        	NodeList<VariableDeclarator> var=field.getVariables();
        	for(int i=0;i<var.size();i++)
        	{
        		
        		VariableDeclarator onevar=var.get(i);
        		String name=onevar.getName().toString();
        		
        		String defaulted="";
        		Optional<Expression> defaultvalue=onevar.getInitializer();
        		if(defaultvalue.isPresent())
        		{
        			defaulted=defaultvalue.get().toString();
        			
        		}
        		
        		IdentifierDS oneidentifier=new IdentifierDS(cORid.getNameAsString(),"",name,type,defaulted,beginindex);
    			fieldlist.add(oneidentifier);
//    			System.out.println(oneidentifier.toString());
    			
        	}
    			
    			
    			//���������Լ�д����ȡ����
    			
////        	System.err.println(field.getTokenRange().toString());  //�����ֶ���Ϣ
//        	String location=field.getBegin().toString();
//        	
//        	int beginindex=Integer.parseInt(location.substring(location.indexOf("line")+4, location.indexOf(",")).trim());
//        	
//        	
//        	String onefield=field.getTokenRange().toString();
////        	
//        	onefield=onefield.substring(onefield.indexOf("[")+1, onefield.lastIndexOf("]"));
//        	if(onefield.startsWith("@"))
//        	{
//        		onefield=onefield.substring(onefield.lastIndexOf("@")+1, onefield.length());
//        		continue;
//            }
////        	if(onefield.contains("private "))
////        		onefield=onefield.substring(onefield.lastIndexOf("private "), onefield.length()).trim();
////        	if(onefield.contains("protected "))
////        		onefield=onefield.substring(onefield.lastIndexOf("protected "), onefield.length()).trim();
////        	if(onefield.contains("public "))
////        		onefield=onefield.substring(onefield.lastIndexOf("public "), onefield.length()).trim();
////        	
//        	onefield=onefield.replaceAll("\t"," ");
//        		
//        	
////        	System.out.println(onefield);
//        	
//        	if(onefield.contains("="))
//        	{
//        		String front=onefield.substring(0,onefield.indexOf("=")).trim();
//        		String end=onefield.substring(onefield.indexOf("=")+1, onefield.length()).trim();
//        		if(front.contains("<")&&front.contains(">"))
//        		{
//        			front=front.substring(0, front.indexOf("<"))+" "+front.substring(front.lastIndexOf(">")+1,front.length());
//        		}
//        		if(end.contains("<")&&end.contains(">"))
//        		{
//        			end=end.substring(0, end.indexOf("<"))+" "+end.substring(end.lastIndexOf(">")+1,end.length());
//        		}
//        		
//        		onefield=front+" = "+end;
//        		
//        	}
//        	else
//        	{
//        		if(onefield.contains("<")&&onefield.contains(">"))
//        		{
//        			onefield=onefield.substring(1, onefield.indexOf("<"))+" "+onefield.substring(onefield.lastIndexOf(">")+1,onefield.length());
//        		}
//        	}
//        	
////        	while(onefield.contains("<")&&onefield.contains(">"))
////        	{
////        		onefield=onefield.substring(0, onefield.indexOf("<"))+onefield.substring(onefield.indexOf(">")+1,onefield.length());
//////        	    System.out.println(onefield);
////        	}
//        	
//        	if(onefield.contains(","))
//        	{
//        		String[] separatefield=onefield.split(",");
//        		String type="";
//        		String front=separatefield[0];
//        		if(front.contains("="))
//        		{
//        			String frontfront=front.substring(0, front.indexOf("=")).trim();
//        			String frontend=front.substring(front.indexOf("=")+1, front.length()).trim();
//        			String onename=frontfront.substring(frontfront.lastIndexOf(" "), frontfront.length()).trim();
//        			frontfront=frontfront.substring(0,frontfront.lastIndexOf(" "));
//        			if(frontfront.contains(" "))
//        			{
//        				type=frontfront.substring(frontfront.lastIndexOf(" "), frontfront.length()).trim();
//        			}
//        			else
//        			{
//        				type=frontfront;
//        			}
//        			
//        			IdentifierDS oneidentifier=new IdentifierDS(cORid.getNameAsString(),"",onename.trim(),type,frontend,beginindex);
//        			fieldlist.add(oneidentifier);
//        			
//        		}
//        		else
//        		{
//        			String onename=front.substring(front.lastIndexOf(" "), front.length()).trim();
//        			front=front.substring(0, front.lastIndexOf(" "));
//        			 
//        			if(front.contains(" "))
//        			{
//        				type=front.substring(front.lastIndexOf(" "), front.length()).trim();
//        			}
//        			else
//        			{
//        				type=front;
//        			}
//        			
//        			IdentifierDS oneidentifier=new IdentifierDS(cORid.getNameAsString(),"",onename.trim(),type,"",beginindex);
//        			fieldlist.add(oneidentifier);
//        		}
//        		for(int i=1;i<separatefield.length;i++)
//        		{
//        			String last=separatefield[i];
//        			
//        			if(last.contains("="))
//        			{
//        				String onename=last.substring(0, last.indexOf("="));
//        				String defaultv=last.substring(last.indexOf("=")+1,last.length()).trim();
//        				IdentifierDS oneidentifier=new IdentifierDS(cORid.getNameAsString(),"",onename.trim(),type,defaultv,beginindex);
//            			fieldlist.add(oneidentifier);
//        			}
//        			else
//        			{
//        				String onename=last;
//        				IdentifierDS oneidentifier=new IdentifierDS(cORid.getNameAsString(),"",onename.trim(),type,"",beginindex);
//            			fieldlist.add(oneidentifier);
//        			}
//        		}
//        	}
//        	else
//        	{
//        		String type="";
//        		if(onefield.contains("="))
//        		{
//        			String backend=onefield.substring(onefield.indexOf("=")+1,onefield.length()).trim();
//        			onefield=onefield.substring(0, onefield.indexOf("="));
//        			
//        			String onename=onefield.substring(onefield.lastIndexOf(" "), onefield.length()).trim();
//        			onefield=onefield.substring(0, onefield.lastIndexOf(" "));
//        			if(onefield.contains(" "))
//        			{
//        				type=onefield.substring(onefield.lastIndexOf(" "), onefield.length()).trim();
//        			}
//        			else
//        			{
//        				type=onefield;
//        			}
//        			
//        			IdentifierDS oneidentifier=new IdentifierDS(cORid.getNameAsString(),"",onename.trim(),type,backend,beginindex);
//        			fieldlist.add(oneidentifier);
//        		}
//        		else
//        		{
//        			
//        			String onename=onefield.substring(onefield.lastIndexOf(" "),onefield.length()).trim();
////        			System.out.println("errrr");
//        			onefield=onefield.substring(0, onefield.lastIndexOf(" ")).trim();
//        			if(onefield.contains(" "))
//        				type=onefield.substring(onefield.lastIndexOf(" "),onefield.length()).trim();
//        			else
//        				type=onefield;
//        			
//        			IdentifierDS oneidentifier=new IdentifierDS(cORid.getNameAsString(),"",onename.trim(),type,"",beginindex);
//        			fieldlist.add(oneidentifier);
//        		}
//        	}
        }
        
        
        
        String location=cORid.getBegin().toString();
    	int index=Integer.parseInt(location.substring(location.indexOf("line")+4, location.indexOf(",")).trim());
//        ClassDS oneclass=new ClassDS(cORid.getNameAsString(),methodlist,fieldlist,constructorlist,index);
    	
        
        
    	String endlocation=cORid.getEnd().toString();
    	int endindex=Integer.parseInt(endlocation.substring(endlocation.indexOf("line")+4, endlocation.indexOf(",")).trim());
    	int finalindex=index*100000+endindex;
        ClassDS oneclass=new ClassDS(cORid.getNameAsString(),methodlist,fieldlist,constructorlist,finalindex);

    	
    	
        classNameSet.add(oneclass);
        super.visit(cORid, classNameSet);
    }
 
}
