package createEmbedding;


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


public class contextEmbedding {


    public static void main(String[] args) throws Exception {
        ProjectCommit("cassandra");
        System.out.println(index);

    }
    public static void ProjectCommit(String project) throws Exception {

//        String csvPath = "C:\\project\\IdentifierStyle\\log\\dump\\" + project + ".csv";
        String newdir="C:\\project\\IdentifierStyle\\data\\VersionDB\\raw_project\\"+project;
        String olddir="C:\\project\\IdentifierStyle\\data\\VersionDB\\raw_project\\"+project+"_old";

        File file = new File(newdir);        //获取其file对象
        File[] fs = file.listFiles();

        List<String> l = new ArrayList<>();
        //遍历path下的文件和目录，放在File数组中
        for (File f : fs) {
            create(project,f.getAbsolutePath(),"new");

        }



        }


    static int index=0;
    private static void create(String project, String change, String curCom, String filename) throws Exception {
        String changeName=change.replace("<-","_");
        String srcFile = "C:\\project\\IdentifierStyle\\data\\VersionDB\\ver_old\\"+project+"\\"+curCom+"_"+changeName+"_"+filename;
        String dstFile = "C:\\project\\IdentifierStyle\\data\\VersionDB\\ver_new\\"+project+"\\"+curCom+"_"+changeName+"_"+filename;
        System.out.println(srcFile);
        System.out.println(dstFile);
        index++;
        BufferedWriter bw=new BufferedWriter(new FileWriter("C:\\project\\IdentifierStyle\\data\\VersionDB\\changeIdentifier\\"+project+"_old_context.csv",true));
        CsvWriter cwriter = new CsvWriter(bw,',');
        createContext(srcFile,project,cwriter);
        cwriter.close();
        bw.close();

////        createContext(dstFile,project,"new");


    }
    public static void create(String project, String File,String ver) throws Exception {

        System.out.println(File);
        index++;
        BufferedWriter bw=new BufferedWriter(new FileWriter("C:\\project\\IdentifierStyle\\data\\VersionDB\\raw_data\\"+project+"_"+ver+"_context_test.csv",true));
        CsvWriter cwriter = new CsvWriter(bw,',');
        createContext(File,project,cwriter);
        cwriter.close();
        bw.close();

////        createContext(dstFile,project,"new");


    }
    public static void createContext(String loc, String proj, CsvWriter cwriter) throws Exception {
       List<String> methodName = new ArrayList<String>();
        List<String> methodBody = new ArrayList<String>();
        File f = new File(loc);
        if (!f.exists()) {
            return;
        }
        BufferedReader br = new BufferedReader(new FileReader(f));

        Map<String, List> map = new HashMap<>();
//            map = JavaParserUtils.getData("C:\\Users\\delll\\IdeaProjects\\GunTreeTest\\src\\main\\resources\\funfile_old.java");
        try {
            map = JavaParserUtils.getData(loc);

        } catch (Exception e) {
            return;
        }
        //1.  使用javaParser获取所有，<函数,函数体>
        methodName = map.get("method_name");
        methodBody = map.get("method_body");
        System.out.println("loc="+loc);
        System.out.println(methodBody.size()+":"+methodName.size());
        for (int i = 0; i < methodName.size(); i++) {
            //1.1 查看该文件的方法名和方法体
            System.out.println("method=" + methodName.get(i));
            System.out.println("context=" + normText(methodBody.get(i)));

            //查找相关实体，获取相关网络
//            relateEmbedding re = new relateEmbedding(methodName.get(i), map);
//            List[] network = re.searchRes(methodName.get(i));
//            List<String> node = network[0];
//            List<List> edge = network[1];
//            StringBuffer edges=new StringBuffer();
//            System.out.print("node=");
//            for (String n : node) {
//                System.out.print(n + " ");
//
//            }
//            System.out.println();
//            System.out.print("edge=");
//            for (List<String> e : edge) {
//                System.out.print("<" + e.get(0) + "," + e.get(1) + ">");
//                edges.append("<" + e.get(0) + "," + e.get(1) + ">"+"|");
//            }
            System.out.println();
            //写入文件
            String[] csvContent = { loc,"method", methodName.get(i),normText(methodBody.get(i))};
            cwriter.writeRecord(csvContent);


        }


        //2. 获取该文件的代码，义查找方法以外的标识符
        Vector<String> allcode = new Vector<String>();
        String one = "";
        while ((one = br.readLine()) != null) {
            allcode.add(one);
        }
        br.close();
//            Vector<iden> alliden = ObtainIdentifier(allcode, "C:\\Users\\delll\\IdeaProjects\\GunTreeTest\\src\\main\\resources\\funfile_old.java");
        //3. 获取文件中的所有标识符
        Vector<iden> alliden = ObtainIdentifier(allcode, loc);
        for (iden id : alliden) {

            //获得相应的标识符
            String identifier = id.getIdentifier();
            //获取对应代码
            String statement = id.getStatement();
            if (isNotMethod(identifier, methodName)) {
                System.out.println("other=" + normText(identifier.trim()));
                System.out.println("context=" + normText(statement.trim()));
//                bw.write(loc+" [other] "+normText(identifier.trim())+" "+normText(statement.trim()));
//                bw.write(loc+","+ "other," +normText(identifier.trim())+","+"\""+normText(statement.trim())+"\"");
//
////                bw.newLine();
//                String[] csvContent = { loc,"other", normText(identifier.trim()),normText(statement.trim()) };
//                cwriter.writeRecord(csvContent);
//                //查找相关实体，获取相关网络
//                relateEmbedding re = new relateEmbedding(identifier, map);
//                List[] network = re.searchRes(identifier);
//                List<String> node = network[0];
//                List<List> edge = network[1];
////                int label = getLabel(identifier,proj,loc);
//                System.out.print("node=");
//                for (String n : node) {
//                    System.out.print(n + " ");
//                }
//                System.out.println();
//                System.out.print("edge=");
//                for (List<String> e : edge) {
//                    System.out.print("<" + e.get(0) + "," + e.get(1) + ">");
//                }
//                System.out.println();


            }
//                System.out.println(normText(methodBody.get(i)));

        }

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

class iden
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
    public iden(int type, String identifier, String statement, int location) {
        super();
        this.type = type;
        this.identifier = identifier;
        this.statement = statement;
        this.location = location;
    }
    @Override
    public String toString() {
        return "iden [type=" + type + ", identifier=" + identifier + ", statement=" + statement + ", location="
                + location + "]";
    }

}

