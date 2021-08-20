package createEmbedding;

import com.csvreader.CsvReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class deleteinvalidFile {
    public static void main(String[] args) {
        deleteFile("zeppelin");
    }

    private static void deleteFile(String proj) {
        String newdir="C:\\project\\IdentifierStyle\\data\\VersionDB\\raw_project\\"+proj;
        String olddir="C:\\project\\IdentifierStyle\\data\\VersionDB\\raw_project\\"+proj+"_old";
        //进入文件夹，获取文件名字
        File file = new File(newdir);		//获取其file对象
        File[] fs = file.listFiles();
        int index=0;
        List<String> l=new ArrayList<>();
        //遍历path下的文件和目录，放在File数组中
        for(File f:fs){					//遍历File[]数组

            String filename=f.getName();
            String[] data=filename.split("_");
            String s=data[0]+"_"+data[data.length-1];
//            deleteNonMethod(s,f,proj);
            //1。删除重复文件
            if(!l.contains(s)){
                //2.删除非method文件

                //3.分层抽样
//                char start=s.charAt(0);

//                if(!containStart(start,l)) {
                    l.add(s);
//                    System.out.println(f.getName());
                    index++;
//                }else{
//                    f.delete();
//                }
            }else{

                f.delete();
            }




        }

     System.out.println(index);




        }

    private static void deleteNonMethod(String s, File f, String proj) {
        List<String> list=new ArrayList<>();
        try{
        CsvReader stmtReader = new CsvReader("C:\\project\\IdentifierStyle\\log\\dump\\"+proj+"_method.csv");
        while (stmtReader.readRecord()) {
            //读取id和文件？
            String changeIden = stmtReader.get(4);

            int changeNum = (changeIden.split("<-").length) - 1;
            String[] locchange=stmtReader.get(3).split("<=");
            String id=null;
            String[] loc=locchange[0].split("\\\\");

            String filename=loc[loc.length-1];
            System.out.println(filename);
            for(int i=0;i<changeNum;i++) {
//                String[] loc=locchange[i].split("\\\\");
//                String filename=loc[loc.length-1];
                id=stmtReader.get(5+i);
                list.add(id+"_"+filename);
                System.out.println(id+"_"+filename);

            }


        }
            }
        catch(Exception e){

        }

        if(!list.contains(s)){
            f.delete();
        }
    }

    private static boolean containStart(char start, List<String> l) {
        boolean isEnough=false;
        //遍历所有
        int num=0;
        for(String s:l){
            if (s.charAt(0)==start){
                num++;
            }
            if (num==2){
                isEnough=true;

                break;
            }
        }
        return isEnough;
    }

}





