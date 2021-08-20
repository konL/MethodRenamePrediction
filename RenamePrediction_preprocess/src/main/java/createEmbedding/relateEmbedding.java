package createEmbedding;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;

import java.util.*;

public class relateEmbedding {
    static String ent;
    static Map<String, List> map;
    static List<String>  fieldsName;
    static List<String> methodName;
    static List<String> variableName;
    static List<String> callSet;
    //获取declaration
    static Map<String, FieldDeclaration> fieldMap;
    static Map<String, MethodDeclaration> methodMap;
    static Map<String, VariableDeclarationExpr> variableMap;
    static Map<String, String> callMap;
    static Map<String, List<String>> classMap;
    public relateEmbedding(String ent,Map<String, List> map){
        this.ent=ent;
        this.map=map;

        fieldsName=map.get("fields_name");
        methodName=map.get("method_name");
        variableName=map.get("variable_name");
        callSet=map.get("call_relation");
        fieldMap=JavaParserUtils.fieldMap;
        methodMap=JavaParserUtils.methodMap;
        variableMap=JavaParserUtils.variableMap;
        callMap=JavaParserUtils.nameExprMap;
        classMap=getClassMap();


    }

    private Map<String, List<String>> getClassMap() {
        Map<String,List<String>> temp=JavaParserUtils.classMap;
        Map<String, List<String>>m=new HashMap<>();
        for (String cla:temp.keySet()){
            List<String> par=temp.get(cla);
            List<String> tmplist=new ArrayList<>();
            for(String a:par){
                tmplist.add(a);
                //System.out.println(cla+":"+a);

            }
            m.put(cla,tmplist);
        }
        return m;
    }


    public   List[]searchRes(String ent) {
// 1）Inclusion：包含e直接包含的实体和直接包含e的元素。
//2）Sibling：e是一个方法，同一个类中的所有方法和字段都被认为是紧密相关的实体
//3）Reference：e所引用的所有实体和引用e的实体
//4)   Inheritance：e是一个类，则其超类和子类

        List<String> res = new ArrayList<>();

        //加入相关的

        Set<String> set = new HashSet<>();
        set.add(ent);
        //1.Inclusion-method：实体是函数，包含该函数的实体，这个包含只有除了函数以外的实体包含
        if(methodName.contains(ent)){

            MethodDeclaration m=methodMap.get(ent);
            //包含这个函数的肯定是类或其他函数等等
            String[] classandother=JavaParserUtils.getParents(m).split("_");
            set.add(classandother[0]);
            //【0】是pkg单独取出，【1】是其父类（用.连接），可能无
            if(classandother.length>1) {
                System.out.print("ent=" + ent + ",classandother=" + classandother[0] + "," + classandother[1]);
                String[] data = classandother[1].split("\\.");
                for (String s : data) {
                    if (!s.equals("")) {
                        set.add(s);
                    }
                }
            }



        }
        //2.Inclusion-callSet：实体是函数，包含该函数的实体
//        if(classMap.containsKey(ent)) {
//
//            //所继承的，所实现的类等导致的变化
//            for (String s : classMap.keySet()) {
//                List<String> cla = classMap.get(s);
//                if (cla != null) {
//                    for (String parent : cla) {
//
//                        set.add(s);
//
//
//                    }
//
//                }
//
//
//            }
//            //子元素产生的变化，只考虑method的变化
//            for (String s : methodMap.keySet()) {
//                MethodDeclaration m = methodMap.get(s);
//                if (m != null) {
//                    String[] data = JavaParserUtils.getParents(m).split("\\.");
//                    for (String parent : data) {
//                        if (parent.equals(ent)) {
//                            set.add(s);
//                            break;
//                        }
//
//                    }
//
//                }
//
//
//            }
////        for (String e : callSet) {
////
////            String[] allparents = callMap.get(e).split("_");
////            for (String parent : allparents) {
////                String[] data = parent.split("\\.");
////                for (String pa : data) {
////                    if (!pa.equals(ent)) {
////                        set.add(e);
////                    }
////                }
////
////
////            }
////        }
//
//
//        }
//        if(fieldsName.contains(ent)){
//
//            FieldDeclaration f=fieldMap.get(ent);
//            String[] data=JavaParserUtils.getParents(f).split("\\.");
//
//            for(String s:data) {
//                if (!s.equals("")) {
//                    set.add(s);
//                }
//            }
//
//        }
//        if(variableName.contains(ent)){
//            VariableDeclarationExpr v=variableMap.get(ent);
//            String[] data=JavaParserUtils.getParents(v).split("\\.");
//            for(String s:data){
//                if(!s.equals("")){
//                    set.add(s);
//                }
//            }
//
//        }
        //2.Inclusion-callSet:callSet集合是在函数中的实体，即ent在函数中
        if(callSet.contains(ent)){
            String[] allparents=callMap.get(ent).split("_");
            for(String parent:allparents){
                String[]data=parent.split("\\.");
                for(String s:data){
                    if(!s.equals("")){
                        set.add(s);
                    }
                }
            }


        }
            List<String> v = new ArrayList<>();
            List<List<String>> es = new ArrayList<>();
            for (String s : set) {
//            System.out.println("set_ele="+s);
                v.add(s);
                List<String> edgePair = new ArrayList<>();
                edgePair.add(ent);
                edgePair.add(s);
                es.add(edgePair);
            }


            return new List[]{v, es};
        }


    }
