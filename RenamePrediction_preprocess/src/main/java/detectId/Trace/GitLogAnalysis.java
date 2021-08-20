package detectId.Trace;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import detectId.DS.ClassDS;
import detectId.DS.IdentifierDS;
import detectId.DS.MethodDS;
import detectId.ParseInfo.ClassCollector;
import detectId.ParseInfo.VariableCollector;
import detectId.utility.SimilarityCal;

import java.io.*;
import java.util.*;


public class GitLogAnalysis {

	public static void main(String args[]) throws Exception
	{
		ProjectCommit("darks-learning");
		AllProjectAnalysis();
		String spl[]="test1<-sdfs<-".split("<-");
		System.out.println(spl.length);
		for(String s:spl)
			System.out.println(s);
	}
	
	public static void ParseProjectResult(String TraceHistory,String project) throws Exception
	{
		File f=new File(TraceHistory);
		if(f.exists())
		{
			BufferedReader br=new BufferedReader(new FileReader(TraceHistory));
			String line="";
			while((line=br.readLine())!=null)
			{
				if(line.contains(","))
				{
					String split[]=line.split(",");
					if(split.length>3)
					{
						String trans=split[3];
						if(trans.contains("<-"))
						{
							String onetrans[]=trans.split("<-");
							
							for(int i=1;i<onetrans.length;i++)
							{
								if(!onetrans[i].equals(onetrans[i-1]))
								{
									//it means that identifier has changed
									BufferedWriter bw=new BufferedWriter(new FileWriter("D:\\project\\IdentifierStyle\\log\\changed\\changed1.csv",true));
									bw.write(project+","+line);
									bw.newLine();
									bw.flush();
									bw.close();
									 
									String style1=CheckStyle(onetrans[i]);
									String style2=CheckStyle(onetrans[i-1]);
									if(!style1.equals(style2))
									{
										BufferedWriter bwww=new BufferedWriter(new FileWriter("D:\\project\\IdentifierStyle\\log\\style_changed\\styled_changed1.csv",true));
										bwww.write(project+","+line);
										bwww.newLine();
										bwww.flush();
										bwww.close();	 
									}
									
								}
							}
						}
					}
				}
			}
			br.close();
		}
	}
	
	
	public static void ParseAnalysisResult() throws Exception
	{
		Hashtable<String,Integer> hs=new Hashtable<String,Integer>();
		Hashtable<String,Integer> multihs=new Hashtable<String,Integer>();
		
		int changedcount=0,stylechangedcount=0;
		BufferedReader br1=new BufferedReader(new FileReader("D:\\project\\IdentifierStyle\\data\\FinalProjects.txt"));
		String project="";
		while((project=br1.readLine())!=null)
		{
			String TraceHistory="D:\\project\\IdentifierStyle\\log\\dump\\"+project+".csv";	
			File f=new File(TraceHistory);
			if(f.exists())
			{
				BufferedReader br=new BufferedReader(new FileReader(TraceHistory));
				String line="";
				while((line=br.readLine())!=null)
				{
					if(line.contains(","))
					{
						String split[]=line.split(",");
						if(split.length>3)
						{
							String trans=split[3];
							if(trans.contains("<-"))
							{
								String transpattern="";
								String onetrans[]=trans.split("<-");
								
								for(int i=1;i<onetrans.length;i++)
								{
									if(!onetrans[i].equals(onetrans[i-1]))
									{
										//it means that identifier has changed
										BufferedWriter bw=new BufferedWriter(new FileWriter("D:\\project\\IdentifierStyle\\log\\changed\\"+project+".csv",true));
										bw.write(line);
										bw.newLine();
										bw.close();
										changedcount++;
										
										String style1=CheckStyle(onetrans[i]);
										String style2=CheckStyle(onetrans[i-1]);
										if(!style1.equals(style2))
										{
											BufferedWriter bwww=new BufferedWriter(new FileWriter("D:\\project\\IdentifierStyle\\log\\style_changed\\"+project+".csv",true));
											bwww.write(line);
											bwww.newLine();
											bwww.close();
											
											stylechangedcount++;
											
											String classification=style1+"->"+style2;
											transpattern=transpattern+"->"+classification;
											
											if(hs.keySet().contains(classification))
											{
												int value=hs.get(classification);
												value++;
												hs.remove(classification);
												hs.put(classification, value);
											}
											else
											{
												hs.put(classification, 1);
											}
										}
										
									}
								}
								
								if(multihs.contains(transpattern))
								{
									int value=multihs.get(transpattern);
									value++;
									multihs.remove(transpattern);
									multihs.put(transpattern, value);
								}
								else
								{
									multihs.put(transpattern, 1);
								}
								
							}
						}
					}
				}
				br.close();
			}
		}
		br1.close();
		
		
		BufferedWriter bw=new BufferedWriter(new FileWriter("D:\\project\\IdentifierStyle\\log\\ChangeStyleStatistics.csv"));
		Set<String> keys=hs.keySet();
		for(String onekey:keys)
		{
			bw.write(onekey+","+hs.get(onekey));
			bw.newLine();
		}
		bw.write("Style Changed Count, "+stylechangedcount);
		bw.newLine();
		bw.write("Changed Count, "+changedcount);
		bw.newLine();
		Set<String> keyss=multihs.keySet();
		for(String onekey:keyss)
		{
			bw.write(onekey+","+multihs.get(onekey));
			bw.newLine();
		}
		bw.close();
	}
	
	public static String CheckStyle(String IdentifierName)
	{
		if(IdentifierName.contains("_"))
		{
			return "underscore";
		}
		else
		{
			LinkedList<String> result=CamelCaseSplit(IdentifierName);
			if(result.size()>1)
			{
				return "camel";					
			}
			else if(result.size()==1)
			{
				return "others";
			}
			else
			{
				return "null";
			}
		}
	}
	
	public static LinkedList<String> CamelCaseSplit(String s) 
	{
		
		LinkedList<String> resultlist = new LinkedList<String>();
		if(s.isEmpty())
		{
			return resultlist;
		}
		StringBuilder word = new StringBuilder();
		char[] buf = s.toCharArray();
		boolean prevIsupper=false;
		for(int i=0;i<buf.length;i++)
		{
			char ch= buf[i];
			if(Character.isUpperCase(ch))
			{
				if(i==0)
				{
					word.append(ch);
				}
				else if(!prevIsupper)
				{
					resultlist.add(word.toString());
					word=new StringBuilder();
					word.append(ch);
				}else
				{
					word.append(ch);
				}
				prevIsupper=true;
			}
			else
			{
				word.append(ch);
				prevIsupper=false;
			}
		}
		if(word!=null&&word.length()>0)
		{
			resultlist.add(word.toString());
		}
		
		
		return resultlist;
	
	}
	
	public static void AllProjectAnalysis() throws Exception
	{
		BufferedReader br1=new BufferedReader(new FileReader("D:\\project\\IdentifierStyle\\data\\FinalProjects1.txt"));
		String project="";
		while((project=br1.readLine())!=null)
		{
			File f=new File("D:\\project\\IdentifierStyle\\log\\dump\\"+project+".csv");
			if(!f.exists())
			{
				ProjectCommit(project);	
			}
		}
		br1.close();
	}
	
		
	public static void ProjectCommit(String projectname) throws Exception
	{
		
		String projectpath="D:\\project\\IdentifierStyle\\data\\GitProject\\"+projectname+"\\";
		String FileIndex="D:\\project\\IdentifierStyle\\data\\JavaFileIndex\\"+projectname+".txt";
		String LogOutput="D:\\project\\IdentifierStyle\\log\\log_output1.txt";
		String TraceHistory="D:\\project\\IdentifierStyle\\log\\dump\\"+projectname+".csv";	
		
		BufferedReader br=new BufferedReader(new FileReader(FileIndex));
		String line="";
		while((line=br.readLine())!=null)
		{
			Vector<String> allcode=new Vector<String>();
			BufferedReader read=new BufferedReader(new FileReader(line));
			String one="";
			while((one=read.readLine())!=null)
			{
				allcode.add(one);
			}
			read.close();
			
			Vector<idenDS> alliden=ObtainIdentifier(allcode,line);
			
			String file=line.substring(line.indexOf(projectpath)+projectpath.length(), line.length());
			file=file.replace("\\", "/");
			file="./"+file;
			
			for(idenDS oneiden:alliden)
			{
				String statement=oneiden.getStatement();
				int lineno=oneiden.getLocation();
				lineno++;
//				ExecuteCommand(projectname,"git log --reverse -L "+lineno+","+lineno+":"+file,LogOutput);
				ExecuteCommand(projectpath,"git log -L "+lineno+","+lineno+":"+file,LogOutput);
				Vector<commitMessage> allcom=ParseCommandContent(LogOutput);
				Vector<String> traceResult=TraceAnalysis(allcom,statement);
				if(traceResult.size()>1)
				{
//					System.err.println("***************************");
//					System.err.println(file+"  "+statement);
//					System.err.println("***************************");
					
					Vector<String> beforeId=ExtractIdFromStatement(traceResult,oneiden.getIdentifier(),oneiden.getType());
					
					BufferedWriter bw=new BufferedWriter(new FileWriter(TraceHistory,true));
					bw.write(oneiden.getType()+","+oneiden.getLocation()+","+line+",");
					
					bw.write(oneiden.getIdentifier()+"<-");
					for(String onetr:beforeId)
					{
						bw.write(onetr+"<-");
					}
					bw.write(",");
					
					for(String onehis:traceResult)
					{
						if(onehis.contains(","))
						{
							onehis=onehis.replace(",", " ");
						}
						bw.write(onehis+",");
					}
					bw.newLine();
					bw.close();
				}
			}
		
		}
		br.close();		
		
		ParseProjectResult(TraceHistory,projectname);
		
	}
	
	
	public static Vector<String> ExtractIdFromStatement(Vector<String> traceResult, String identifier, int type) throws Exception
	{
		Vector<String> result=new Vector<String>();
		for(int i=1;i<traceResult.size();i++)
//		for(String onetrace:traceResult)
		{
			String onetrace=traceResult.get(i);
			onetrace=onetrace.trim();
			if(onetrace.endsWith(";"))
            	onetrace=onetrace.substring(0,onetrace.length()-1);
			onetrace=onetrace.trim();
			
			if(type==1)
			{
            	if(onetrace.startsWith("/*"))
            		onetrace=onetrace.substring(onetrace.indexOf("*/")+2, onetrace.length()).trim();
            	if(onetrace.startsWith("//"))
            		onetrace=onetrace.substring(onetrace.indexOf("package "),onetrace.length());
            	if(onetrace.startsWith("package "))
            		onetrace=onetrace.substring(onetrace.indexOf(" ")+1,onetrace.length());
   
                result.add(onetrace);
			}
			else if(type==2)
			{
				if(onetrace.endsWith("{"))
					onetrace=onetrace.substring(0, onetrace.length()-1);
				if(onetrace.contains(" implements "))
					onetrace=onetrace.substring(0, onetrace.indexOf(" implements ")).trim();
				
				if(onetrace.contains(" extends "))
					onetrace=onetrace.substring(0, onetrace.indexOf(" extends ")).trim();
				
				if(onetrace.contains(" "))
				{
					onetrace=onetrace.substring(onetrace.lastIndexOf(" ")+1, onetrace.length()).trim();
					result.add(onetrace);				
				}
				else
				{
					result.add(onetrace);
				}
				
			}
			else if(type==3)
			{
				if(onetrace.contains("("))
					onetrace=onetrace.substring(0, onetrace.indexOf("(")).trim();
				
				if(onetrace.contains(" "))
				{
					onetrace=onetrace.substring(onetrace.lastIndexOf(" ")+1, onetrace.length()).trim();
					result.add(onetrace);				
				}
				else
				{
					result.add(onetrace);
				}
				
			}
			else if(type==4)
			{
				if(onetrace.contains("="))
					onetrace=onetrace.substring(0, onetrace.indexOf("=")).trim();
				if(onetrace.contains(" "))
				{
					onetrace=onetrace.substring(onetrace.lastIndexOf(" ")+1, onetrace.length()).trim();
					result.add(onetrace);				
				}
				else
				{
					result.add(onetrace);
				}
				
			}
			else if(type==5)
			{
				if(!onetrace.contains("=")&&onetrace.contains("("))
				{
					if(onetrace.contains(")"))
					{
						onetrace=onetrace.substring(onetrace.indexOf("(")+1, onetrace.indexOf(")"));
					}
					else
					{
						onetrace=onetrace.substring(onetrace.indexOf("(")+1, onetrace.length()).trim();
					}
					
					if(onetrace.contains(","))
					{
						Vector<String> needtest=new Vector<String>();
						String spl[]=onetrace.split(",");
						for(String onespl:spl)
						{
							onespl=onespl.trim();
							String test=onespl.trim();
							if(onespl.contains(" "))
							{
								test=onespl.substring(onespl.lastIndexOf(" ")+1, onespl.length());
							}
							needtest.add(test);
								
						}
						
						float max=0;
						String maxstring="";
						for(String ss:needtest)
						{
							float simi= SimilarityCal.calEditSimi(ss, identifier);
							if(simi>max)
							{
								max=simi;
								maxstring=ss;
							}
						}
						result.add(maxstring);
								
					}
					else if(onetrace.contains(":"))
					{
						onetrace=onetrace.substring(0,onetrace.indexOf(":")).trim();
						String test=onetrace;
						if(onetrace.contains(" "))
						{
							test=onetrace.substring(onetrace.lastIndexOf(" ")+1, onetrace.length());
						}
						result.add(test);
					}
					else if(onetrace.contains(";"))
					{
						onetrace=onetrace.substring(0,onetrace.indexOf(";")).trim();
						String test=onetrace;
						if(onetrace.contains(" "))
						{
							test=onetrace.substring(onetrace.lastIndexOf(" ")+1, onetrace.length());
						}
						result.add(test);
					}
					else
					{
						String test=onetrace.trim();
						if(onetrace.contains(" "))
						{
							test=onetrace.substring(onetrace.lastIndexOf(" ")+1, onetrace.length());
						}
						result.add(test);
					}
					
					
				}
				else
				{
					if(onetrace.contains("="))
						onetrace=onetrace.substring(0, onetrace.indexOf("=")).trim();
					
					if(onetrace.contains(" "))
					{
						onetrace=onetrace.substring(onetrace.lastIndexOf(" ")+1, onetrace.length()).trim();
						result.add(onetrace);				
					}
					else
					{
						result.add(onetrace);
					}
				}
			}
				
		}
		return result;
	}
	
	public static Vector<String> TraceAnalysis(Vector<commitMessage> allcom, String statement) throws Exception
	{
		statement=statement.trim();
		Vector<String> traceHistory=new Vector<String>();
		traceHistory.add(statement);
		
		for(commitMessage onecommit:allcom)
		{
			Vector<Diff> difflist=onecommit.getDifflist();
			for(Diff onediff:difflist)
			{
				Vector<String> content=onediff.getContent();
				for(String line:content)
				{
					if(line.startsWith("- "))
					{
						line=line.substring(1,line.length()).trim();
//						line=line.trim();
//						statement=statement.trim();
						float simi=SimilarityCal.calEditSimi(line, statement);
						if(simi>0.85&&simi!=1)
						{
							statement=line;
							traceHistory.add(statement);
							break;
						}
					}
				}
			}
		}
		return traceHistory;
	}
	
	public static Vector<commitMessage> ParseCommandContent(String filename) throws Exception
	{
		Vector<commitMessage> allmessage=new Vector<commitMessage>();
		
		Vector<String> allc=new Vector<String>();
		BufferedReader br=new BufferedReader(new FileReader(filename));
		String lines="";
		while((lines=br.readLine())!=null)
		{
			allc.add(lines);
		}
		br.close();
		
		Vector<Vector<String>> commitinfo=new Vector<Vector<String>>();
		Vector<String> onecom=new Vector<String>();
		for(int i=0;i<allc.size();i++)
		{
			if(allc.get(i).startsWith("commit "))
			{
				if(onecom.size()!=0)
				{
					Vector<String> temp=new Vector<String>();
					temp.addAll(onecom);
					commitinfo.add(temp);
					onecom.clear();
					onecom.add(allc.get(i));
				}
				else
				{
					onecom.add(allc.get(i));
				}
					
			}
			else
			{
				onecom.add(allc.get(i));
			}
		}
		
		if(onecom.size()!=0)
		{
			Vector<String> temp=new Vector<String>();
			temp.addAll(onecom);
			commitinfo.add(temp);
			onecom.clear();	
		}
		
		
//		System.out.println("commit NO: "+commitinfo.size());
		
		for(Vector<String> onecommit:commitinfo)
		{
//			for(String ssss:onecommit)
//				System.out.println(ssss);		
			String commitid="";
			String author="";
			String date="";
			String message="";
			StringBuilder sb=new StringBuilder();
			
			for(String line:onecommit)
			{
				if(line.startsWith("commit"))
	            {
	                commitid=line.substring("commit".length(), line.length()).trim();
	            }
	            else if(line.startsWith("Author:"))
	            {
	                author=line.substring("Author:".length(), line.length()).trim();
	            }
	            else if(line.startsWith("Date:"))
	            {
	                date=line.substring("Date:".length(), line.length()).trim();
	            }
	            else if(line.startsWith("    "))
	            {
	                  message+=line.trim()+"\n";
	            }
	            else
	            {
	            	sb.append(line+"\n");
	            }
			}
			
//			
//			System.out.println(commitid+"\n");
//			System.out.println(author+"\n");
//			System.out.println(date+"\n");
//			System.out.println(message+"\n");
			
			Vector<Diff> difflist=new Vector<Diff>();
			String mess=sb.toString();
			String split[]=mess.split("diff --git");
			
//			System.out.println(split.length-1);
			for(String s:split)
			{
				if(!s.trim().isEmpty())
				{
				    Diff onediff=AnalyzeDiff("diff --git "+s.trim());
//					System.out.println(onediff.toString());
//					System.out.println("****************************");
					difflist.add(onediff);
				}
			}
			
			commitMessage onecommitmessage=new commitMessage(commitid, author, date, message,difflist);
			allmessage.add(onecommitmessage);
		}
		return allmessage;
	}
	
	public static Diff AnalyzeDiff(String s)
	{
		String split[]=s.split("\n");
		String fromFile="";
		String toFile="";
		String index="";
		Vector<String> content=new Vector<String>(); 
		for(String oneline:split)
		{
			oneline=oneline.trim();
			if(oneline.startsWith("--- "))
			{
				fromFile=oneline.substring(oneline.indexOf("--- ")+"--- ".length(), oneline.length());
			}
			else if(oneline.startsWith("+++ "))
			{
				toFile=oneline.substring(oneline.indexOf("+++ ")+"+++ ".length(), oneline.length());
			}
			else if(oneline.startsWith("+++ "))
			{
				toFile=oneline.substring(oneline.indexOf("+++ ")+"+++ ".length(), oneline.length());
			}
			else if(oneline.startsWith("@@"))
			{
				index=oneline.replace("@", "").trim();
			}
			else
			{
				if(!oneline.startsWith("diff --git ")&&!oneline.isEmpty())
					content.add(oneline);
			}
		}
		
		Diff one=new Diff(fromFile,toFile,index,content);
		return one;
	}
		
	public static void ExecuteCommand(String projectdir,String cmd,String output) throws Exception
	{
		String[] command =
			{
					"cmd",
			};
			Process p = Runtime.getRuntime().exec(command);
			new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
			new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
			PrintWriter stdin = new PrintWriter(p.getOutputStream());
			stdin.println("D:");
			stdin.println("cd "+projectdir);
			stdin.println(cmd+" > "+output);
			stdin.close();
			int returnCode = p.waitFor();
			System.out.println("Return code = " + returnCode);
	}
		
	public static  Vector<idenDS> ObtainIdentifier(Vector<String> allstate, String javafilepath) throws Exception
	{
		System.out.println(javafilepath);
		Vector<IdentifierDS> packages=new Vector<IdentifierDS>();  //���뵱ǰ��package
		Vector<IdentifierDS> types=new Vector<IdentifierDS>();     //�࣬�ӿڣ�ö��
		Vector<IdentifierDS> methods=new Vector<IdentifierDS>();   //method��������constructor,setter,getter
		Vector<IdentifierDS> fields=new Vector<IdentifierDS>();    //
		Vector<IdentifierDS> variables=new Vector<IdentifierDS>(); //�����˺����Ĳ���
        
		CompilationUnit cu =null;
		
		try {
             cu = JavaParser.parse(new File(javafilepath));
            
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
//			BufferedWriter bw=new BufferedWriter(new FileWriter("D:\\project\\IdentifierStyle\\data\\JavaParserCannotParse.txt",true));
//            bw.write(javafilepath);
//            bw.newLine();
//            bw.close();
		}
        
        String packagename="";
        try {
        	Optional<PackageDeclaration> packagename1=cu.getPackageDeclaration();
        	if(packagename1.isPresent())
            {
            	
            	packagename=packagename1.get().toString().trim();
            	if(packagename.startsWith("/*"))
            		packagename=packagename.substring(packagename.indexOf("*/")+2, packagename.length()).trim();
            	if(packagename.startsWith("//"))
            		packagename=packagename.substring(packagename.indexOf("package "),packagename.length());
            	if(packagename.startsWith("package "))
            		packagename=packagename.substring(packagename.indexOf(" ")+1,packagename.length());
                if(packagename.endsWith(";"))
                	packagename=packagename.substring(0,packagename.length()-1);
            }
             
        }
        catch(Exception e)
		{
			System.err.println(e.toString());
		}
        int packloc=1;
        for(int i=0;i<allstate.size();i++)
        {
        	if(allstate.get(i).trim().startsWith("package "))
        	{
        		packloc=i+1;
        		break;
        	}
        }
       
        IdentifierDS newpackage=new IdentifierDS("","",packagename,"","",packloc);
        packages.add(newpackage);
         
        
        Hashtable<String,Integer> variableSet =new Hashtable<String,Integer>();	    	//���еı����Ͷ���
        VoidVisitor<Hashtable<String, Integer>> VariableCollector = new VariableCollector();
        try {
        VariableCollector.visit(cu, variableSet);
        }
        catch(Exception e)
		{
			System.err.println(e.toString());
		}
      
        
    	Vector<ClassDS> classdetails=new Vector<ClassDS>();
        VoidVisitor<Vector<ClassDS>> classNameCollector = new ClassCollector();
        try {
        classNameCollector.visit(cu, classdetails);
        }
        catch(Exception e)
		{
			System.err.println(e.toString());
		}

        
        for(ClassDS one :classdetails)
        {
          	IdentifierDS newclass=new IdentifierDS(one.getClassname(),"",one.getClassname(),"class","",one.getIndex());
          	types.add(newclass);
          	
    		Vector<MethodDS> methodlist=one.getMethodlist();
          	for(MethodDS onemethod:methodlist)
          	{
          		IdentifierDS newmethod=new IdentifierDS(one.getClassname(),onemethod.getMethodname(),onemethod.getMethodname(),onemethod.getReturntype(),"",onemethod.getBeginindex());
          		methods.add(newmethod);
          		
          		Vector<IdentifierDS> parameters=onemethod.getParameters();
          		for(IdentifierDS oneid:parameters)
          		{
	        			variables.add(oneid);
          		}
          		
          	}
          	        	
          	Vector<IdentifierDS> fieldlist=one.getFieldlist();
          	fields.addAll(fieldlist);
          	
        }
        
        
        Set<String> keyset=variableSet.keySet();
        for(String onekey:keyset)
        {
        	int onevalue=variableSet.get(onekey);
        	String methodpar="";
        	String classpar="";
        	for(ClassDS one :classdetails)
            {
              	              	
        		Vector<MethodDS> methodlist=one.getMethodlist();
              	for(MethodDS onemethod:methodlist)
              	{
              		if(onevalue>=onemethod.getBeginindex()&&onevalue<=onemethod.getEndindex())
              		{
              			methodpar=onemethod.getMethodname();
              			classpar=one.getClassname();
              			break;
              		}
              	}
            }
        	
        	
        	if(onekey.contains("="))
        	{
        		String front=onekey.substring(0, onekey.indexOf("=")).trim();
        		String end=onekey.substring(onekey.indexOf("=")+1, onekey.length()).trim();
        		String name=front.substring(front.lastIndexOf(" ")+1, front.length()).trim();
        		String type="";
        		
        		if(front.contains(" "))
        		{
        			front=front.substring(0, front.lastIndexOf(" "));
        			if(front.contains(" "))
        			{
        				type=front.substring(front.lastIndexOf(" ")+1,front.length()).trim();
        			}
        			else type=front;
        		}
        		else
        			type=front;
        		
        		type=type.trim();
        		IdentifierDS oneid=new IdentifierDS(classpar,methodpar,name,type,end,onevalue);
        		 
        		variables.add(oneid);
        		
        				
        	}
        	else
        	{
        		String name=onekey.substring(onekey.lastIndexOf(" ")+1, onekey.length()).trim();
        		onekey=onekey.substring(0, onekey.lastIndexOf(" ")).trim();
        		String type="";
        		if(onekey.contains(" "))
        			type=onekey.substring(onekey.lastIndexOf(" ")+1, onekey.length()).trim();
        		else
        			type=onekey;
        		
        		type=type.trim();
        		
        		IdentifierDS oneid=new IdentifierDS(classpar,methodpar,name,type,"",onevalue);
        		
        		variables.add(oneid);
        			 
        	}      	
        }
        
        
        Vector<idenDS> allid=new Vector<idenDS>();
        for(IdentifierDS onepackage:packages)
        {
//        	System.out.println(onepackage.toString());
        	String identifiername=onepackage.getName();
        	int location=onepackage.getLocation();
        	String singlestate="";
        	int purelocation=-1;
        	if(location-1>=0&&allstate.size()>0)
        	{
        		singlestate=allstate.get(location-1);
        		purelocation=location-1;
        	}
        	
        	if(singlestate.contains(identifiername))
        	{
        		idenDS oness=new idenDS(1,identifiername,singlestate,purelocation);
        		allid.add(oness);
        	}
        	else
        	{
        		System.err.println("1: ��ʶ��λ�ò��ԣ�"+identifiername+"  "+singlestate);
        	}
        	
        }
        for(IdentifierDS onetype:types)
        {
//        	System.out.println(onetype.toString());
        	
        	String identifiername=onetype.getName();
        	int location=onetype.getLocation();
        	location=location/100000;
        	
        	String singlestate="";
        	int purelocation=-1;
        	if(location-1>=0&&allstate.size()>0)
        	{
        		singlestate=allstate.get(location-1);
        		purelocation=location-1;
        	}
        	
        	if(singlestate.trim().startsWith("@"))
        	{
        		if(location<allstate.size())
        		{
	        		singlestate=allstate.get(location);
	        		purelocation=location;
        		}
        		
        		if(singlestate.trim().startsWith("@"))
            	{
            		singlestate=allstate.get(location+1);
            		purelocation=location+1;
            	}
        	}
        	
        	if(singlestate.contains(identifiername))
        	{
        		idenDS oness=new idenDS(2,identifiername,singlestate,purelocation);
        		allid.add(oness);
        	}
        	else
        	{
        		System.err.println("2: ��ʶ��λ�ò��ԣ�"+identifiername+"  "+singlestate);
        	}
        
        }
        for(IdentifierDS onemethod:methods)
        {
        	String identifiername=onemethod.getName();
        	int location=onemethod.getLocation();
        	
        	String singlestate=allstate.get(location-1);
        	int purelocation=location-1;
        	
        	if(singlestate.trim().startsWith("@"))
        	{
        		singlestate=allstate.get(location);
        		purelocation=location;
        		
        		if(singlestate.trim().startsWith("@"))
            	{
            		singlestate=allstate.get(location+1);
            		purelocation=location+1;
            	}
        	}
        	
        	if(singlestate.contains(identifiername))
        	{
        		idenDS oness=new idenDS(3,identifiername,singlestate,purelocation);
        		allid.add(oness);
        	}
        	else
        	{
        		System.err.println("3: ��ʶ��λ�ò��ԣ�"+identifiername+"  "+singlestate);
        	}
        }
        for(IdentifierDS onefield:fields)
        {
        	String identifiername=onefield.getName();
        	int location=onefield.getLocation();
        	String singlestate=allstate.get(location-1);
        	int purelocation=location-1;
        	if(singlestate.contains(identifiername))
        	{
        		idenDS oness=new idenDS(4,identifiername,singlestate,purelocation);
        		allid.add(oness);
        	}
        	else
        	{
        		System.err.println("4: ��ʶ��λ�ò��ԣ�"+identifiername+"  "+singlestate);
        	}
        }
        for(IdentifierDS onevariable:variables)
        {
        	String identifiername=onevariable.getName();
        	int location=onevariable.getLocation();
        	String singlestate=allstate.get(location-1);
        	int purelocation=location-1;
        	if(singlestate.trim().startsWith("@"))
        	{
        		singlestate=allstate.get(location);
        		purelocation=location;
        		if(singlestate.trim().startsWith("@"))
            	{
            		singlestate=allstate.get(location+1);
            		purelocation=location+1;
            	}
        	}
        	
        	if(singlestate.contains(identifiername))
        	{
        		idenDS oness=new idenDS(5,identifiername,singlestate,purelocation);
        		allid.add(oness);
        	}
        	else
        	{
        		if(purelocation+1<allstate.size())
        		{
	        		singlestate=singlestate+" "+allstate.get(purelocation+1);
	//        		purelocation=location+1;
	        		if(singlestate.contains(identifiername))
	            	{
	            		idenDS oness=new idenDS(5,identifiername,singlestate,purelocation);
	            		allid.add(oness);
	            	}
	        		else
	        		{
	        			System.err.println("5: ��ʶ��λ�ò��ԣ�"+identifiername+"  "+singlestate);
	        		}
        		}
        	}
        }
        
        
        return allid;
        
    }
	
	
}

class idenDS
{
	int type;
	String identifier;
	String statement;
	int location;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getStatement() {
		return statement;
	}
	public void setStatement(String statement) {
		this.statement = statement;
	}
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	public idenDS(int type, String identifier, String statement, int location) {
		super();
		this.type = type;
		this.identifier = identifier;
		this.statement = statement;
		this.location = location;
	}
	@Override
	public String toString() {
		return "idenDS [type=" + type + ", identifier=" + identifier + ", statement=" + statement + ", location="
				+ location + "]";
	}
	
}


class Diff
{
	String fromFile;
	String toFile;
	String index;
	Vector<String> content=new Vector<String>();
	public String getFromFile() {
		return fromFile;
	}
	public void setFromFile(String fromFile) {
		this.fromFile = fromFile;
	}
	public String getToFile() {
		return toFile;
	}
	public void setToFile(String toFile) {
		this.toFile = toFile;
	}
	 
	 
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public Vector<String> getContent() {
		return content;
	}
	public void setContent(Vector<String> content) {
		this.content = content;
	}
	public Diff(String fromFile, String toFile, String index, Vector<String> content) {
		super();
		this.fromFile = fromFile;
		this.toFile = toFile;
		this.index = index;
		this.content = content;
	}
	@Override
	public String toString() {
		return "Diff [fromFile=" + fromFile + ", toFile=" + toFile + ", index=" + index + ", content=" + content.toString() + "]";
	}
	 
	
	 
	 
	

}

class commitMessage
{
	String commitid;
	String author;
	String date;
	String message;
	Vector<Diff> difflist=new Vector<Diff>();
	public String getCommitid() {
		return commitid;
	}
	public void setCommitid(String commitid) {
		this.commitid = commitid;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Vector<Diff> getDifflist() {
		return difflist;
	}
	public void setDifflist(Vector<Diff> difflist) {
		this.difflist = difflist;
	}
	public commitMessage(String commitid, String author, String date, String message, Vector<Diff> difflist) {
		super();
		this.commitid = commitid;
		this.author = author;
		this.date = date;
		this.message = message;
		this.difflist = difflist;
	}
	@Override
	public String toString() {
		return "commitMessage [commitid=" + commitid + ", author=" + author + ", date=" + date + ", message=" + message
				+ ", difflist=" + difflist.size() + "]";
	}
	
	
	
}
