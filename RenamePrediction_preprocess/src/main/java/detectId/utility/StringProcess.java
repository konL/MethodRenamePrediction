package detectId.utility;


import com.aliasi.sentences.MedlineSentenceModel;
import com.aliasi.sentences.SentenceModel;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;
import uk.ac.open.crc.intt.IdentifierNameTokeniser;
import uk.ac.open.crc.intt.IdentifierNameTokeniserFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringProcess {
	
	/**
	 * ���ַ�����ʾ����������ʽ��Ȩ��Ϊtf
	 * @param str
	 * @return
	 * @throws IOException
	 */
	 public static Hashtable<String, Integer> convertStringToVector(String str) throws IOException
	 {
       
		 Vector<String> stopword=new Vector<String>();
	     BufferedReader br = new BufferedReader(new FileReader("E:\\API\\data\\stopword.txt"));
	     String str1="";
	     while((str1=br.readLine())!=null)
	     {
	    	 stopword.add(str1);
	     }
	     br.close();
		 //Token
	     str=str.toLowerCase();
		 StringTokenizer st = new StringTokenizer(str," .	*\n\r[]()/\\{};:~''^_\"=,<>+-!|&@#%$?");
		 Hashtable<String, Integer> ht=new Hashtable<String, Integer>();
		 while (st.hasMoreTokens()) 
		 {
			 String temp=st.nextToken();
			 temp=temp.trim();
			 if(!temp.isEmpty()&&!stopword.contains(temp))
			 {
				 Vector<String> v=tokenSplit(temp);
				 for(int i=0;i<v.size();i++)    
				 {
					 String low=v.get(i).toLowerCase();
					 Stemmer s=new Stemmer();
				     String ok=s.porterstem(low);
					 
					 if(!isNumeric(ok))
					 {	
						 
						 //Stemming							    			     
						    if(stopword.contains(ok))
						    		 continue;
						    	 
						    else 
						    {
						       if(ht.containsKey(ok))
						       {
								   int num=ht.get(ok);
								   num++;
								   ht.remove(ok);
								   ht.put(ok, num);
						        }
						       else
						           ht.put(ok, 1);
						    }				     			     
					     
					 }
				 }
			 }
		 }
		 
		 return ht;
	 }
	 
	 
	 public static Hashtable<String, Integer> convertStringToVectorNoSplit(String str) throws IOException
	 {
       
		 Vector<String> stopword=new Vector<String>();
	     BufferedReader br = new BufferedReader(new FileReader("E:\\API\\data\\stopword.txt"));
	     String str1="";
	     while((str1=br.readLine())!=null)
	     {
	    	 stopword.add(str1);
	     }
	     br.close();
		 //Token
	     str=str.toLowerCase();
		 StringTokenizer st = new StringTokenizer(str," .	*\n\r[]()/\\{};:~''^_\"=,<>+-!|&@#%$?");
		 Hashtable<String, Integer> ht=new Hashtable<String, Integer>();
		 while (st.hasMoreTokens()) 
		 {
			 String temp=st.nextToken();
			 temp=temp.trim();
			 if(!temp.isEmpty()&&!stopword.contains(temp))
			 {
//				 Vector<String> v=tokenSplit(temp);
//				 for(int i=0;i<v.size();i++)    
//				 {
//					 String low=v.get(i).toLowerCase();
					 String low=temp;
					 Stemmer s=new Stemmer();
				     String ok=s.porterstem(low);
					 
					 if(!isNumeric(ok))
					 {	
						 
						 //Stemming							    			     
						    if(stopword.contains(ok))
						    		 continue;
						    	 
						    else 
						    {
						       if(ht.containsKey(ok))
						       {
								   int num=ht.get(ok);
								   num++;
								   ht.remove(ok);
								   ht.put(ok, num);
						        }
						       else
						           ht.put(ok, 1);
						    }				     			     
					     
					 }
//				 }
			 }
		 }
		 
		 return ht;
	 }
	 
	 
	 
	 
	 /**
		 * ��camelcase��token���
		 * @param s
		 * @return
		 */
		public static Vector<String> tokenSplit(String s)
		{
			if(s.contains("\""))
				s=s.replace("\"", " ");
			if(s.contains("{"))
				s=s.replace("{", " ");
			if(s.contains("}"))
				s=s.replace("}", " ");
			s=s.replaceAll(" +", " ");
//			System.err.println(s.trim());
//			System.out.println("okokok "+s);
			Vector<String> v=new Vector<String>();
//			if(hasDigit(s)==true)
//			{
//				v.add(s);
//			}
//			else
//			{
			try {
				IdentifierNameTokeniserFactory a=new IdentifierNameTokeniserFactory();
				IdentifierNameTokeniser b=a.create();
				String temp[]= b.tokenise(s).toArray(new String[0]);
				for(int i=0;i<temp.length;i++)
					v.add(temp[i]);
				if(temp.length>1)
				   v.add(s);
			}
			catch(Exception e)
			{
				System.out.println(s);
			}
//			}
			return v;
		}
		
		
		/**
		    * �ж��ַ����ǲ�������
		    * @param str
		    * @return
		    */
			public static boolean isNumeric(String str)
			{ 
				if(str.length()>=2)
			   {
				   if(str.charAt(0)=='0'&&str.charAt(1)=='x')
				   {
					   
					   for(int i=str.length()-1;i>1;i--)
					   {
						   if(!Character.isDigit(str.charAt(i))&&str.charAt(i)!='a'&&str.charAt(i)!='b'&&str.charAt(i)!='c'&&str.charAt(i)!='d'&&str.charAt(i)!='e'&&str.charAt(i)!='f')
							   return false;
					   }
					   return true;
				   }
				   else
				   {
					   for(int i=str.length();--i>=0;)
					   {
						   if(!Character.isDigit(str.charAt(i)))
							   return false;
					   }
					   return true;
				   }
			   }
			   else
			   {
				   for(int i=str.length();--i>=0;)
				   {
					   if(!Character.isDigit(str.charAt(i)))
						   return false;
				   }
				   return true;
			   }
				 
			}
			
			
			
			/**
			 * ��һ����ı��־�
			 * @param text
			 * @return
			 */
			public static Vector<String> sentenceSplit(String text)
		 	{
		    	text=text.replace("&quot;", " ");
		    	text=text.replace("&quot", " ");
		      	final TokenizerFactory TOKENIZER_FACTORY = IndoEuropeanTokenizerFactory.INSTANCE;
		 	    final SentenceModel SENTENCE_MODEL  = new MedlineSentenceModel();

		 	    Vector<String> result=new Vector<String>();
		 		List<String> tokenList = new ArrayList<String>();
		 		List<String> whiteList = new ArrayList<String>();
		 		Tokenizer tokenizer = TOKENIZER_FACTORY.tokenizer(text.toCharArray(),0,text.length());
		 		tokenizer.tokenize(tokenList,whiteList);

		 		String[] tokens = new String[tokenList.size()];
		 		String[] whites = new String[whiteList.size()];
		 		tokenList.toArray(tokens);
		 		whiteList.toArray(whites);
		 		int[] sentenceBoundaries = SENTENCE_MODEL.boundaryIndices(tokens,whites);

		 		int sentStartTok = 0;
		 		int sentEndTok = 0;
		 		for (int i = 0; i < sentenceBoundaries.length; ++i) 
		 		{
		 		    sentEndTok = sentenceBoundaries[i];
		 		    String onesen="";
		 		    for (int j=sentStartTok; j<=sentEndTok; j++)
		 		    {
		 			   onesen+=tokens[j]+whites[j+1];
		 		    }
		 		   // System.out.println(onesen);
		 		    if(!onesen.trim().isEmpty())
		 		       result.add(onesen);
		 		    sentStartTok = sentEndTok+1;
		 		}
		 		
		 		Vector<String> newresult=new Vector<String>();
		 		for(String sen:result)
		 		{
		 			if(sen.contains(":    "))
		 			{
			 			String[] spli=sen.split(":    ");
			 			for(int i=0;i<spli.length;i++)
			 			{
			 				String need=spli[i].trim();
			 				if(!need.isEmpty())
			 				   newresult.add(need);
			 			}
		 			}
		 			else
		 			{
		 				if(!sen.trim().isEmpty())
		 				    newresult.add(sen);
		 			}
		 		}
		 		
		 		Vector<String> finalresult=new Vector<String>();
		 		for(String s:newresult)
		 		{
		 			s=s.replaceAll(" +", " ");
		 			finalresult.add(s.toLowerCase());
		 		}
		 		
		 		return finalresult;
		 	}
			
			
			
			
			
			 
			
			
			/**
			 * ɾ�����еı�����
			 * @param s
			 * @return
			 */
			public static String deletePunctuation(String s)
		    {
				String content=s;
				content=content.replaceAll( "[\\pP+~$`^=|<>�����ޣ�������������]" , "");
				return content;
		    } 

			
			public static Vector<String> deleteTag(String str)
			{
				Vector<String> aftersplit=new Vector<String>();
				if(str.contains("<p>"))
				{
					String[] split=str.split("<p>");
					for(String substr:split)
					{
						aftersplit.add(substr);
					}
				}
				else
				{
					aftersplit.add(str);
				}
				
				Vector<String> result=new Vector<String>();
				
				for(String s:aftersplit)
				{
					//ɾ����ǩ
			    	while(true)
				    {
					   if(s.contains("<")&&s.contains(">")&&s.indexOf("<")<s.indexOf(">"))
					   {
						   s=s.substring(0, s.indexOf("<"))+" "+s.substring(s.indexOf(">")+1, s.length());
					   }
					   else if(s.contains("<")&&!s.contains(">"))
				       {
						   s=s.substring(0, s.indexOf("<"));
					   }
					   else if(!s.contains("<")&&s.contains(">"))
					   {
						   s=s.substring(s.indexOf(">")+1, s.length());
					   }
					   else
						   break;
				   }
			    	
			    	result.add(s);
				}
		    	return result;
			}
			
			
			
			public static Vector<String> CodeSplittoState(String code)
			{
				code=code.replace("&lt;", "");
				code=code.replace("&gt;", "");
				code=code.replace("&quot;", "");
				code=code.replace("&quot", "");
				Vector<String> result=new Vector<String>();
				code=code.trim();
				if(code.contains(",     "))
				{
					String[] split=code.split(",     ");
					for(int i=0;i<split.length;i++)
					{
						if(!split[i].trim().isEmpty())
							result.add(split[i].trim());
					}
				}
				else
				{
					result.add(code);
				}
				Vector<String> newresult=new Vector<String>();
				for(String onesen:result)
				{
					if(onesen.contains("    "))
					{
						String[] split=onesen.split("    ");
						for(String s:split)
						{
							s=s.trim();
							if(!s.isEmpty())
							{
								String dups = s.replaceAll("[\\pP��������]", "");
								if(!dups.trim().isEmpty())
								    newresult.add(s);
							}
						}
					}
					else
					{
						String newonesen = onesen.replaceAll("[\\pP��������]", "");
						if(!newonesen.trim().isEmpty())
								newresult.add(onesen.trim());
					}
				}
				
				
				Vector<String> newnewresult=new Vector<String>();
				for(String s:newresult)
				{
					s=s.trim();
					if(s.contains(";")&&!s.startsWith("for"))
					{
						String[] split=s.split(";");
						for(String so:split)
						{
							String newso=so.replaceAll("[\\pP��������]", "");
							if(!newso.trim().isEmpty())
							    newnewresult.add(so.trim());
						}
					}
					else
					{
						String newso=s.replaceAll("[\\pP��������]", "");
						if(!newso.trim().isEmpty())
						    newnewresult.add(s);
					}
				}
				
				Vector<String> newnewnewresult=new Vector<String>();
				for(String s:newnewresult)
				{
					if(s.contains("{"))
					{
						s=s.replace("{", "<<<<<");
						String[] split=s.split("<<<<<");
						for(String so:split)
						{
							String newso=so.replaceAll("[\\pP��������]", "");
							if(!newso.trim().isEmpty())
							    newnewnewresult.add(so.trim());
						}
						
					}
					else
					{
						String newso=s.replaceAll("[\\pP��������]", "");
						if(!newso.trim().isEmpty())
						    newnewnewresult.add(s);
					}
				}
				
				
				Vector<String> finalresult=new Vector<String>();
				for(String s:newnewnewresult)
				{
					
					if(s.trim().contains(" //"))
					{
						String[] split=s.trim().split(" //");
						for(String so:split)
						{
							String newso=so.replaceAll("[\\pP��������]", "");
							if(!newso.trim().isEmpty())
							    finalresult.add(so.trim());
						}
					}
					else
					{
						String newso=s.replaceAll("[\\pP��������]", "");
						if(!newso.trim().isEmpty())
						    finalresult.add(s);
					}
				}
				
				
				Vector<String> finresult=new Vector<String>();
				for(String s:finalresult)
				{
					s=s.replaceAll(" +", " ");
					finresult.add(s.toLowerCase());
				}
				return finresult;
			}
			
			
			
			public static String Clean(String s)
		    {
		    	String content=s;
		    	content=content.replaceAll("[\\pP��������]", " ");
				content=content.replace("<", " ");
				content=content.replace(">", " ");
				content=content.replace("=", " ");
				content=content.replace("+", " ");
				content=content.replaceAll(" +", " ");
				StringBuilder sb=new StringBuilder();
				StringTokenizer st = new StringTokenizer(content," .	*\n\r[]()/\\{};:~''^_\"=,<>+-!|&@#%$?");
				while (st.hasMoreTokens()) 
				{
					 String temp=st.nextToken();
					 Stemmer ste=new Stemmer();
					 String ok=ste.porterstem(temp.trim());
					 sb.append(ok+" ");
				}
				
				 
				
				String needreturn=sb.toString().trim();
				if(needreturn.contains("\n"))
					needreturn=needreturn.replace("\n", "");
				needreturn=needreturn.toLowerCase();
				return needreturn;
		    }
			
			
		  
			public static boolean hasDigit(String content)
			{
				boolean flag = false;
				Pattern p = Pattern.compile(".*\\d+.*");
				Matcher m = p.matcher(content);
				if (m.matches())
					flag = true;
				return flag;
			}
			 
		   
		     
//		    public static void main(String args[]) throws Exception
//		    {
//		    	System.out.println(hasDigit("DeflaterOutputStream"));
//		    }

}
