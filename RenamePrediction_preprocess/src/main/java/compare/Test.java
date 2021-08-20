package compare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Test {

    public static void main(String[] args) throws IOException {
        // org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor.java
        String lineStr = "dubbo.txt:org.apache.dubbo.metadata.report.support:AbstractMetadataReport:getProtocol:URL#url:String#SimpleType String VariableName stringVar VariableName urlVar MethodName getParameter VariableName SIDE_KEY VariableName stringVar Operator = VariableName stringVar Operator == NullLiteral null VariableName urlVar MethodName getProtocol VariableName stringVar ReturnStatement return VariableName stringVar \n" ;
        String[] elements = lineStr.split(":");
        // org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor.java

        String packageName = elements[1];
        String className = elements[2];
        // ProjectName : packageName : className : methodName : arguments : returnType.
        String methodName = elements[3];
        String proj=elements[0];
        proj=proj.substring(0,proj.indexOf(".txt"));
        String arg=elements[4].replace("#","+");
       int sharposition=elements[5].indexOf("#");
       String returnType=elements[5].substring(0,sharposition);
       String infotokens=elements[5].substring(sharposition+2);
        String methodInfo = proj +"\\:" + packageName + ":" + className + ":" + methodName + ":" + arg + ":" + returnType;
        System.out.println(methodInfo);
        System.out.println(infotokens);
//        //获取predict.txt
//        String proj="hbase";
//        Set<String> results=new HashSet<>();
//        BufferedReader resRead=new BufferedReader(new FileReader("C:\\project\\IdentifierStyle\\data\\VersionDB\\prepocessed_data\\predict.txt"));
//        String resStr=null;
//        while((resStr=resRead.readLine())!=null) {
//            results.add(resStr.trim());
//        }
//        System.out.println(results);
//
//
//        //获取所有rename_list
//        List<String> rename_list=new ArrayList<>();
//
//        //proj.csv获取文件（来自ver_old）
//        BufferedReader br=new BufferedReader(new FileReader("C:\\project\\IdentifierStyle\\log\\dump\\"+proj+".csv"));
//        String line=null;
//        while((line=br.readLine())!=null) {
//            String[] data = line.split(",");
//            //获取change变化
//            String[] changes = data[3].split("<-");
//            String oldName=changes[changes.length-1];
//            rename_list.add(oldName);
//        }
//        //没有在results出现过的则是fn
//        int fn=0;
//        for (String ele:rename_list){
//            if(!results.contains(ele)){
//                //实际rename了却未被预测的
//                fn++;
//            }
//        }
//        System.out.println("fn="+fn);

    }
    }


