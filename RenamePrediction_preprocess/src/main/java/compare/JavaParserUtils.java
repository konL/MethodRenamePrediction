package compare;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;
import java.util.*;


public class JavaParserUtils {
    static List<String> methodName=new ArrayList<String>();
    static List<String> fieldsName=new ArrayList<String>();
    static List<String> methodBody=new ArrayList<String>();
    static List<String> varibleName=new ArrayList<String>();
    static List<String> callSet=new ArrayList<String>();
    //存储对应的xxName和xxxDeclaration
    static Map<String, FieldDeclaration> fieldMap=new HashMap<>();
    static Map<String, VariableDeclarationExpr> variableMap=new HashMap<>();
    static Map<String, MethodDeclaration> methodMap=new HashMap<>();
    static Map<String, String> nameExprMap=new HashMap<>();


    public static Map<String,List> getData(String file) throws Exception{
//        FileInputStream in = new FileInputStream("D:\\kon_data\\JAVA_DATA\\SpotRenaming\\ast-javaParser\\src\\main\\resources\\Srccode.java");
        FileInputStream in = new FileInputStream(file);

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

        nameExp.walk(Node.TreeTraversal.PARENTS, node -> {
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
                    path.replace(0, 1, ".");
                    path.insert(0, pkg.get().getNameAsString());
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
                    path.replace(0, 1, ".");
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
                    path.replace(0, 1, ".");
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
                    path.replace(0, 1, ".");
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
           //System.out.println("methodName:"+n.getName());

           // System.out.println("methodParams:"+n.getBody().);
//            System.out.println("----------------------------------------------------------------------------");
//            System.out.println("methodBody:"+n.getBody());
//            System.out.println("----------------------------------------------------------------------------");
            methodBody.add(n.getBody().toString());

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
            System.out.println("class:"+n.getName());
            System.out.println("extends:"+n.getExtendedTypes());
            System.out.println("implements:"+n.getImplementedTypes());


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
                nameExprMap.put(n.getNameAsString(), nameExprMap.get(n.getNameAsString())+(getParents(n)));
            }
            super.visit(n, arg);
        }



    }
}
