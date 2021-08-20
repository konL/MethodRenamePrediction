package detectId.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;


public class SimilarityCal {
	
	
	
	/**
	 * �����������ƶ�
	 * @param s1
	 * @param s2
	 * @return
	 * @throws Exception
	 */
	public static float calVSMSimi(String s1,String s2,Hashtable<String,Float> idf) throws Exception
    {

//		System.out.println(s1);
//		System.err.println(s2);
   	    Hashtable<String,Integer> ht1=StringProcess.convertStringToVector(s1);
		Hashtable<String,Integer> ht2=StringProcess.convertStringToVector(s2);
		float similarity=0;
		float upper=0;
		float down1=0;
		float down2=0;
		Set<String> alltoken=new HashSet<String>();
		alltoken.addAll(ht1.keySet());
		alltoken.addAll(ht2.keySet());
		Iterator<String> it=alltoken.iterator();
		while(it.hasNext())
		{
			String token=it.next();
			if(ht1.keySet().contains(token)&&ht2.keySet().contains(token)&&idf.keySet().contains(token))
			{
				upper+=(Math.log(ht1.get(token))+1.0)*idf.get(token)*(Math.log(ht2.get(token))+1.0)*idf.get(token);
			}
			
		}
		Iterator<String> itht1=ht1.keySet().iterator();
		while(itht1.hasNext())
		{
			String tokeninht1=itht1.next();
			if(idf.keySet().contains(tokeninht1))
			{
				down1+=(Math.log(ht1.get(tokeninht1))+1)*idf.get(tokeninht1)*(Math.log(ht1.get(tokeninht1))+1)*idf.get(tokeninht1);
			}
		}
		Iterator<String> itht2=ht2.keySet().iterator();
		while(itht2.hasNext())
		{
			String tokeninht2=itht2.next();
			if(idf.keySet().contains(tokeninht2))
			{
				down2+=(Math.log(ht2.get(tokeninht2))+1)*idf.get(tokeninht2)*(Math.log(ht2.get(tokeninht2))+1)*idf.get(tokeninht2);
			}
		}
		if(down1!=0&&down2!=0)
		    similarity=(float) (upper/(Math.sqrt(down1)*Math.sqrt(down2)));
		return similarity;
    }
    
	
    /**
     * ����༭�������ƶ�
     * @param str1
     * @param str2
     * @return
     * @throws Exception
     */
	public static float calEditSimi(String str1,String str2) throws Exception
    {
    	str1=str1.toLowerCase();
		str2=str2.toLowerCase(); 

        int len1 = str1.length();  
        int len2 = str2.length();  

        int[][] dif = new int[len1 + 1][len2 + 1];  

        for (int a = 0; a <= len1; a++) {  
            dif[a][0] = a;  
        }  
        for (int a = 0; a <= len2; a++) {  
            dif[0][a] = a;  
        }  
      
        int temp;  
        for (int i = 1; i <= len1; i++) {  
            for (int j = 1; j <= len2; j++) {  
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {  
                    temp = 0;  
                } else {  
                    temp = 1;  
                }  
                // ȡ����ֵ����С��   
                dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,  
                        dif[i - 1][j] + 1);  
            }  
        }  
    
         float simi = 1 - (float) dif[len1][len2]/ Math.max(str1.length(), str2.length());  
         return simi;
    }
    
    
	
	
	/**
	 * ����NGram���ƶ�
	 * @param source
	 * @param target
	 * @param n
	 * @return
	 */
    public static  float calNGramSimi(String source,String target,int n)
    {

    	
    	final int sl = source.length();
        final int tl = target.length();
        
        if (sl == 0 || tl == 0) {
          if (sl == tl) {
            return 1;
          }
          else {
            return 0;
          }
        }

        int cost = 0;
        if (sl < n || tl < n) {
          for (int i=0,ni=Math.min(sl,tl);i<ni;i++) {
            if (source.charAt(i) == target.charAt(i)) {
              cost++;
            }
          }
          return (float) cost/Math.max(sl, tl);
        }

        char[] sa = new char[sl+n-1];
        float p[]; //'previous' cost array, horizontally
        float d[]; // cost array, horizontally
        float _d[]; //placeholder to assist in swapping p and d
        
        //construct sa with prefix
        for (int i=0;i<sa.length;i++) {
          if (i < n-1) {
            sa[i]=0; //add prefix
          }
          else {
            sa[i] = source.charAt(i-n+1);
          }
        }
        p = new float[sl+1]; 
        d = new float[sl+1]; 
      
        // indexes into strings s and t
        int i; // iterates through source
        int j; // iterates through target

        char[] t_j = new char[n]; // jth n-gram of t

        for (i = 0; i<=sl; i++) {
            p[i] = i;
        }

        for (j = 1; j<=tl; j++) {
            //construct t_j n-gram 
            if (j < n) {
              for (int ti=0;ti<n-j;ti++) {
                t_j[ti]=0; //add prefix
              }
              for (int ti=n-j;ti<n;ti++) {
                t_j[ti]=target.charAt(ti-(n-j));
              }
            }
            else {
              t_j = target.substring(j-n, j).toCharArray();
            }
            d[0] = j;
            for (i=1; i<=sl; i++) {
                cost = 0;
                int tn=n;
                //compare sa to t_j
                for (int ni=0;ni<n;ni++) {
                  if (sa[i-1+ni] != t_j[ni]) {
                    cost++;
                  }
                  else if (sa[i-1+ni] == 0) { //discount matches on prefix
                    tn--;
                  }
                }
                float ec = (float) cost/tn;
                // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
                d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+ec);
            }
            // copy current distance counts to 'previous row' distance counts
            _d = p;
            p = d;
            d = _d;
        }

        // our last action in the above loop was to switch d and p, so p now
        // actually has the most recent cost counts
        return 1.0f - (p[sl] / Math.max(tl, sl));
    	
    	
    }
   
    /**
     * ����Jaccard���ƶ� 
     * @param s1
     * @param s2
     * @return
     * @throws Exception
     */
    public static float calJaccardSimi(String s1,String s2) throws Exception
    {
    	Hashtable<String,Integer> hsAPI=StringProcess.convertStringToVector(s1);
 		Hashtable<String,Integer> hsSegment=StringProcess.convertStringToVector(s2);
 		 
    	
    	Set<String> se=new HashSet<String>();
    	se.addAll(hsAPI.keySet());
    	se.addAll(hsSegment.keySet());
    	
    	
    	int count=0;
    	Iterator<String> it=se.iterator();
    	while(it.hasNext())
    	{
    		String key=it.next();
    		if(hsAPI.keySet().contains(key)&&hsSegment.keySet().contains(key))
    		{
    			count++;
    		}
    	}
    	if(se.size()!=0)
    	    return ((float)count)/se.size();
    	else
    		 return 0.0f;
    	
    }
    

    
    public static float calWordNetSimi(String s1,String s2)
	{
	   if(s1.contains("\""))
		   s1=s1.replace("\"", " ");
	   if(s2.contains("\""))
		   s2=s2.replace("\"", " ");
	   String news1="";
	   StringTokenizer st = new StringTokenizer(s1," .	*\n\r[]()/\\{};:~''^_\"=,<>+-!|&@#%$?");
	   while (st.hasMoreTokens()) 
	   {
		   news1=news1+" "+st.nextToken();
	   }
	   
	   
	   String news2="";
	   st = new StringTokenizer(s2," .	*\n\r[]()/\\{};:~''^_\"=,<>+-!|&@#%$?");
	   while (st.hasMoreTokens()) 
	   {
		   news2=news2+" "+st.nextToken();
	   }
	   
		String command="C:\\Users\\jingxuan\\Thanh\\bin\\Debug\\WordsMatching.exe \""+news1+"\" \""+news2+"\"";
	    float result=-1;
	    String cmd = "cmd /c "+command ;
	    
	   try {
	    Process process = Runtime.getRuntime().exec(cmd);	    
	    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    String line = null;	    
	    while ((line = reader.readLine()) != null)
	    {	     
	    	result=Float.parseFloat(line);
	    }
	    process.waitFor();
	    process.getOutputStream().close();	
	    process.getInputStream().close();
	    reader.close();
	   // process.destroy();
	   } catch (Exception e)
	   {
	    e.printStackTrace();
	   }
	   
	   return result;
	}
   
    
    
    
    public static int min(int... is)
	{  
       int min = Integer.MAX_VALUE;  
       for (int i : is) 
       {  
           if (min > i) 
           {  
               min = i;  
           }  
       }  
       return min; 
	} 
    
    
    public static Hashtable<String,Integer> makeIDF(Vector<String> allSentence) throws Exception
    {
    	Hashtable<String,Integer> idf=new Hashtable<String,Integer>();
    	
    	for(String onesen:allSentence)
    	{
    		Hashtable<String,Integer> termvec=StringProcess.convertStringToVector(onesen);
    		Set<String> keyset=termvec.keySet();
    		for(String oneterm:keyset)
    		{
    			if(idf.keySet().contains(oneterm))
    			{
    				int size=idf.get(oneterm);
    				size++;
    				idf.remove(oneterm);
    				idf.put(oneterm, size);
    			}
    			else
    			{
    				idf.put(oneterm, 1);
    			}
    		}
    	}
    	
    	return idf;
    }

    
    
}
