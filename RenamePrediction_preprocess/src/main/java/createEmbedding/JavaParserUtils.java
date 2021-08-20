package createEmbedding;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;
import java.util.*;
import java.util.function.Consumer;


public class JavaParserUtils {
    static List<String> methodName;

    static List<String> methodBody;
    static List<String> fieldsName;
    static List<String> varibleName;
    static List<String> callSet;
    //存储对应的xxName和xxxDeclaration
    static Map<String, FieldDeclaration> fieldMap=new HashMap<>();
    static Map<String, VariableDeclarationExpr> variableMap=new HashMap<>();
    static Map<String, MethodDeclaration> methodMap=new HashMap<>();
    static Map<String, String> nameExprMap=new HashMap<>();
    static Map<String, List<String>> classMap=new HashMap<>();

    //    public static void main(String[] args) throws Exception {
//        Map<String,List> map=getData("C:\\project\\IdentifierStyle\\data\\GitProject\\hbase\\hbase-client\\src\\main\\java\\org\\apache\\hadoop\\hbase\\MetaTableAccessor.java");
//
//        List<String> call=map.get("call_relation");
//        System.out.println("【callSet】");
//        for (String s:call){
//            System.out.println("("+s+")");
//            System.out.println("=====>"+nameExprMap.get(s));
//        }
//    }
    public static Map<String,List> getData(String filePath) throws Exception{
        methodName=new ArrayList<String>();
        methodBody=new ArrayList<String>();
        fieldsName=new ArrayList<String>();
       varibleName=new ArrayList<String>();
       callSet=new ArrayList<String>();
        FileInputStream in = new FileInputStream(filePath);



        // parse the file
        CompilationUnit cu = JavaParser.parse(in);





        // prints the resulting compilation unit to default system output
//        System.out.println(cu.toString());

        cu.accept(new Visitor(), null);
//        //查看list
//        for(String mn:methodBody){
//            System.out.println(mn);
//        }
        Map<String,List> map=new HashMap<>();
        map.put("fields_name",fieldsName);
        map.put("method_name",methodName);
        map.put("method_body",methodBody);
        map.put("variable_name",varibleName);
        map.put("call_relation",callSet);
        return map;


    }
    public static String getParents(final NameExpr nameExp) {
        final StringBuilder path = new StringBuilder();

        nameExp.walk(Node.TreeTraversal.PARENTS, new Consumer<Node>() {
            @Override
            public void accept(Node node) {
                if (node instanceof ClassOrInterfaceDeclaration) {
                    path.insert(0, ((ClassOrInterfaceDeclaration) node).getNameAsString());
                    path.insert(0, '.');
                }
                if (node instanceof ObjectCreationExpr) {
                    path.insert(0, ((ObjectCreationExpr) node).getType().getNameAsString());
                    path.insert(0, '.');
                }
                if (node instanceof MethodDeclaration) {
                    path.insert(0, ((MethodDeclaration) node).getNameAsString());
                    path.insert(0, '.');
                }
                if (node instanceof CompilationUnit) {
                    final Optional<PackageDeclaration> pkg = ((CompilationUnit) node).getPackageDeclaration();
                    if (pkg.isPresent()) {
                        path.replace(0, 1, "_");
                        path.insert(0, pkg.get().getNameAsString());
                    }
                }
            }
        });

        // convert StringBuilder into String and return the String
        //System.out.println("parents:"+path.toString());
        return path.toString();
    }
    public static String getParents(final MethodDeclaration methodDeclaration) {
        final StringBuilder path = new StringBuilder();

        methodDeclaration.walk(Node.TreeTraversal.PARENTS, node -> {
            if (node instanceof ClassOrInterfaceDeclaration) {
                path.insert(0, ((ClassOrInterfaceDeclaration) node).getNameAsString());
                path.insert(0, '.');
            }
            if (node instanceof ObjectCreationExpr) {
                path.insert(0, ((ObjectCreationExpr) node).getType().getNameAsString());
                path.insert(0, '.');
            }
            if (node instanceof MethodDeclaration) {
                path.insert(0, ((MethodDeclaration) node).getNameAsString());
                path.insert(0, '.');
            }
            if (node instanceof CompilationUnit) {
                final Optional<PackageDeclaration> pkg = ((CompilationUnit) node).getPackageDeclaration();
                if (pkg.isPresent()) {
                    path.replace(0, 1, "_");
                    path.insert(0, pkg.get().getNameAsString());
                }
            }
        });

        // convert StringBuilder into String and return the String
        System.out.println("parents:"+path.toString());
        return path.toString();
    }
    public static String getParents(final VariableDeclarationExpr variableDeclaration) {
        final StringBuilder path = new StringBuilder();

        variableDeclaration.walk(Node.TreeTraversal.PARENTS, node -> {
            if (node instanceof ClassOrInterfaceDeclaration) {
                path.insert(0, ((ClassOrInterfaceDeclaration) node).getNameAsString());
                path.insert(0, '.');
            }
            if (node instanceof ObjectCreationExpr) {
                path.insert(0, ((ObjectCreationExpr) node).getType().getNameAsString());
                path.insert(0, '.');
            }
            if (node instanceof MethodDeclaration) {
                path.insert(0, ((MethodDeclaration) node).getNameAsString());
                path.insert(0, '.');
            }
            if (node instanceof CompilationUnit) {
                final Optional<PackageDeclaration> pkg = ((CompilationUnit) node).getPackageDeclaration();
                if (pkg.isPresent()) {
                    path.replace(0, 1, "_");
                    path.insert(0, pkg.get().getNameAsString());
                }
            }
        });

        // convert StringBuilder into String and return the String
        System.out.println("parents:"+path.toString());
        return path.toString();
    }

    public static String getParents(final FieldDeclaration fieldDeclaration) {
        final StringBuilder path = new StringBuilder();

        fieldDeclaration.walk(Node.TreeTraversal.PARENTS, node -> {
            if (node instanceof ClassOrInterfaceDeclaration) {
                path.insert(0, ((ClassOrInterfaceDeclaration) node).getNameAsString());
                path.insert(0, '.');
            }
            if (node instanceof ObjectCreationExpr) {
                path.insert(0, ((ObjectCreationExpr) node).getType().getNameAsString());
                path.insert(0, '.');
            }
            if (node instanceof MethodDeclaration) {
                path.insert(0, ((MethodDeclaration) node).getNameAsString());
                path.insert(0, '.');
            }
            if (node instanceof CompilationUnit) {
                final Optional<PackageDeclaration> pkg = ((CompilationUnit) node).getPackageDeclaration();
                if (pkg.isPresent()) {
                    path.replace(0, 1, "_");
                    path.insert(0, pkg.get().getNameAsString());
                }
            }
        });

        // convert StringBuilder into String and return the String
        System.out.println("parents:"+path.toString());
        return path.toString();
    }


    private static class Visitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(final FieldDeclaration n, Void arg) {
            System.out.println("Fields:"+n.getVariables());
            fieldsName.add(n.getVariables().toString());
            fieldMap.put(n.getVariables().toString(),n);
            super.visit(n, arg);
        }
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            /* here you can access the attributes of the method.
             this method will be called for all methods in this
             CompilationUnit, including inner class methods */

            methodName.add(n.getNameAsString());
            methodMap.put(n.getNameAsString(),n);

            //首先分割
            String body=n.getBody().toString();
            int s=body.toString().lastIndexOf("[");
            body=body.substring(s+1,body.length()-1);
            String[] line=body.split("\n");
            StringBuffer sb=new StringBuffer();
            for(String l:line){
                if(!l.trim().startsWith("//") &&!l.trim().startsWith("/*")&&!l.trim().startsWith("*")){
                    sb.append(l.trim());
                }

            }
            methodBody.add(n.getDeclarationAsString().trim()+sb.toString());
//            System.out.println("body==="+n.getDeclarationAsString().trim()+sb.toString());

            //getParents(n);

            super.visit(n, arg);
        }

        @Override
        public void visit(VariableDeclarationExpr n, Void arg) {
            //System.out.println(n.getVariables());
            String data=n.getVariables().toString();
            String[] set=data.substring(1,data.length()-1).split("=");
            varibleName.add(set[0].trim());
            variableMap.put(set[0].trim().toString(),n);



            super.visit(n, arg);
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
//            System.out.println("class:"+n.getName());
//            System.out.println("extends:"+n.getExtendedTypes());
//            System.out.println("implements:"+n.getImplementedTypes());
            List<String> classInfo=new ArrayList<>();
            classInfo.add(n.getExtendedTypes().toString().replace("[","").replace("]",""));
            classInfo.add(n.getImplementedTypes().toString().replace("[","").replace("]",""));


            if(n.isInnerClass()){
                classInfo.add(n.getParentNode().toString());
            }







            classMap.put(n.getName().toString(),classInfo);

            super.visit(n, arg);
        }

        @Override
        public void visit(PackageDeclaration n, Void arg) {
            System.out.println("package:"+n.getName());
            super.visit(n, arg);
        }
        @Override
        public void visit(MethodCallExpr n, Void arg) {
            //System.out.println("MethodCallExpr:"+n.getName());
            super.visit(n, arg);
        }
        @Override
        public void visit(NameExpr n, Void arg) {
            //System.out.println("NameExpr:"+n.getName());
            getParents(n);

            callSet.add(n.getNameAsString());
            //就是函数中调用的东西
            if(nameExprMap.get(n.getNameAsString())==null) {
                nameExprMap.put(n.getNameAsString(), getParents(n));
            }else{
                nameExprMap.put(n.getNameAsString(), nameExprMap.get(n.getNameAsString())+"_"+(getParents(n)));
            }
            super.visit(n, arg);
        }



    }
}
