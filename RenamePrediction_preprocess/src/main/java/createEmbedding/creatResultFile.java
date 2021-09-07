package createEmbedding;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import detectId.DS.ClassDS;
import detectId.DS.IdentifierDS;
import detectId.DS.MethodDS;
import detectId.ParseInfo.ClassCollector;
import detectId.ParseInfo.VariableCollector;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class creatResultFile {
    public static void main(String[] args) throws Exception {
        createProj("flink");



    }
    /*
    In this step we merge the corresponding methods (renamed or unchanged) from each file pair (new and old versions) together to get the test data
    */

    private static void createProj(String proj) throws Exception {
        
        //输出位置

        BufferedWriter bw=new BufferedWriter(new FileWriter("C:\\project\\IdentifierStyle\\data\\VersionDB\\raw_data\\test_data_6x\\"+proj+"_result_t.csv",true));
        CsvWriter writer=new CsvWriter(bw,',');
        //读取记录了改名的csv文件
        CsvReader read = new CsvReader("C:\\project\\IdentifierStyle\\log\\dump\\"+proj+"_method.csv");
        System.out.println("read _method .csv");
        while (read.readRecord()) {




            String id = read.get(4).trim();
            String temp = read.get(3).replace("<-","_");
            System.out.println("TEST==="+temp);
            if(temp.length()==0) continue;
            String changeId=temp.substring(0,temp.length()-1).trim();
            String[] t = read.get(2).split("<=")[0].split("<-")[0].split("\\\\");
            String filename=t[t.length-1].trim();

            //获得对应的java文件，生成oldCContext，newContext
            String file_old = "C:\\project\\IdentifierStyle\\data\\VersionDB\\raw_project\\"+proj+"_old\\" + id + "_" + changeId + "_" + filename;
            String file_new = "C:\\project\\IdentifierStyle\\data\\VersionDB\\raw_project\\"+proj+"\\" + id + "_" + changeId + "_" + filename;
//        contextEmbedding ctx=new contextEmbedding();
            //返回一个list


            List<String[]> oldInfo=new ArrayList<>();
            List<String[]> newInfo=new ArrayList<>();
            oldInfo = create("old", file_old);
            newInfo = create("new", file_new);


                System.out.println(oldInfo.size());
                System.out.println(newInfo.size());


            String oldStmt = "", newStmt = "", oldname = "", newname = "",edge="";
            for (String[] s : oldInfo) {
                String type = s[1];
                oldname = s[2];
                oldStmt = s[3];
                if(s.length>=5) {
                    edge = s[4];
                }
                System.out.println("changeId="+changeId);
                System.out.println("oldName="+oldname);
                if (oldname.equals(changeId.split("_")[1])) {


                    //去newInfo找newname的代码
                    String[] newItem = findNewItem(newInfo, changeId.split("_")[0]);
                    if (newItem.length > 0) {
                        newname = newItem[2];
                        newStmt = newItem[3];
                        //写入文件
                        writer.writeRecord(new String[]{"1", type, oldname, newname, oldStmt, newStmt,edge});
                        System.out.println("label=1==========================================================");
                    }

                } else {
                    //在新文件中查找有相同名字的
                    String[] sameItem = findSameItem(newInfo, oldname);
                    if (sameItem.length > 0) {
                        newname = sameItem[2];
                        newStmt = sameItem[3];
                        //写入文件
                        writer.writeRecord(new String[]{"0", type, oldname, newname, oldStmt, newStmt,edge});
                    }
                }

            }
        }

//        compare
        writer.close();



    }

    private static String[] findSameItem(List<String[]> newInfo, String name) {
        for(String[] s:newInfo){
            if(s[2].equals(name)){
                return s;
            }

        }
        return new String[]{};
    }

    private static String[] findNewItem(List<String[]> newInfo, String name) {
        for(String[] s:newInfo){
            if(s[2].trim().equals(name.trim())){
                return s;
            }

        }
        return new String[]{};
    }


    public static  List<String[]> create(String ver, String File) throws Exception {

//        System.out.println(File);
//        index++;
        List<String[]> rawData = new ArrayList<>();
        List<String[]> raw = createContext(File, ver,rawData);


////        createContext(dstFile,project,"new");
        return raw;


    }

    public static  List<String[]> createContext(String loc, String ver,  List<String[]> rawData) throws Exception {

        List<String> methodName = new ArrayList<String>();
        List<String> methodBody = new ArrayList<String>();

        File f = new File(loc);
        if (!f.exists()) {
            return rawData;
        }
        BufferedReader br = new BufferedReader(new FileReader(f));

        Map<String, List> map = new HashMap<>();
//            map = JavaParserUtils.getData("C:\\Users\\delll\\IdeaProjects\\GunTreeTest\\src\\main\\resources\\funfile_old.java");
        try {
            map = JavaParserUtils.getData(loc);

        } catch (Exception e) {
            return rawData;
        }
        //1.  使用javaParser获取所有，<函数,函数体>
        methodName = map.get("method_name");
        methodBody = map.get("method_body");
        System.out.println("loc=" + loc);
        System.out.println(methodBody.size() + ":" + methodName.size());
        for (int i = 0; i < methodName.size(); i++) {
            //1.1 查看该文件的方法名和方法体
            System.out.println("method=" + methodName.get(i));
            System.out.println("context=" + normText(methodBody.get(i)));
            //查找相关实体，获取相关网络
            relateEmbedding re = new relateEmbedding(methodName.get(i), map);
            List[] network = re.searchRes(methodName.get(i));
            List<String> node = network[0];
            List<List> edge = network[1];
            StringBuffer edges=new StringBuffer();
            System.out.print("node=");
            for (String n : node) {
                System.out.print(n + " ");

            }
            System.out.println();
            System.out.print("edge=");
            for (List<String> e : edge) {
                System.out.print("<" + e.get(0) + "," + e.get(1) + ">");
                edges.append("<" + e.get(0) + "," + e.get(1) + ">"+"|");
            }


            System.out.println();
            //写入文件
            if(ver.equals("old")){
                String[] csvContent = {loc, "method", methodName.get(i), normText(methodBody.get(i)),normText(edges.toString())};
                rawData.add(csvContent);

            }else {
                String[] csvContent = {loc, "method", methodName.get(i), normText(methodBody.get(i))};
                rawData.add(csvContent);
            }


        }


        //2. 获取该文件的代码，义查找方法以外的标识符
        Vector<String> allcode = new Vector<String>();
        String one = "";
        while ((one = br.readLine()) != null) {
            allcode.add(one);
        }
        br.close();
//
        return rawData;


    }
    private static boolean isNotMethod(String identifier, List<String> methodName) {
        boolean b=true;
        if(methodName.contains(identifier.trim())){
            b=false;
        }
        return b;
    }



    public static String normText(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        //去除注释
        return dest;
    }
    public static  Vector<iden> ObtainIdentifier(Vector<String> allstate, String javafilepath) throws Exception
    {
        System.out.println(javafilepath);
        Vector<IdentifierDS> packages=new Vector<IdentifierDS>();  //加入当前的package
        Vector<IdentifierDS> types=new Vector<IdentifierDS>();     //类，接口，枚举
        Vector<IdentifierDS> methods=new Vector<IdentifierDS>();   //method，包括了constructor,setter,getter
        Vector<IdentifierDS> fields=new Vector<IdentifierDS>();    //
        Vector<IdentifierDS> variables=new Vector<IdentifierDS>(); //包括了函数的参数

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


        Hashtable<String,Integer> variableSet =new Hashtable<String,Integer>();	    	//所有的变量和对象
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


        Vector<iden> allid=new Vector<iden>();
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
                iden oness=new iden(1,identifiername,singlestate,purelocation);
                allid.add(oness);
            }
            else
            {
                System.err.println("1: 标识符位置不对！"+identifiername+"  "+singlestate);
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
                iden oness=new iden(2,identifiername,singlestate,purelocation);
                allid.add(oness);
            }
            else
            {
                System.err.println("2: 标识符位置不对！"+identifiername+"  "+singlestate);
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
                iden oness=new iden(3,identifiername,singlestate,purelocation);
                allid.add(oness);
            }
            else
            {
                System.err.println("3: 标识符位置不对！"+identifiername+"  "+singlestate);
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
                iden oness=new iden(4,identifiername,singlestate,purelocation);
                allid.add(oness);
            }
            else
            {
                System.err.println("4: 标识符位置不对！"+identifiername+"  "+singlestate);
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
                iden oness=new iden(5,identifiername,singlestate,purelocation);
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
                        iden oness=new iden(5,identifiername,singlestate,purelocation);
                        allid.add(oness);
                    }
                    else
                    {
                        System.err.println("5: 标识符位置不对！"+identifiername+"  "+singlestate);
                    }
                }
            }
        }


        return allid;

    }



}
