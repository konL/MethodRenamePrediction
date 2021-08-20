import com.csvreader.CsvReader;
import detectId.Trace.SyncPipe;

import java.io.*;

public class createVerDB {
    public static void main(String[] args) throws Exception {
        //传入项目名称
        //ProjectCommit("zeppelin");
        ProjectCommit("Test");
       //ProjectCommit("facebook-android-sdk");
        //ProjectCommit("dubbo");
    }

    private static void ProjectCommit(String project) throws Exception {
        String projectpath="C:\\project\\IdentifierStyle\\data\\GitProject\\"+project;
        String csvPath="C:\\project\\IdentifierStyle\\log\\dump\\"+project+".csv";
        //String csvPath="C:\\project\\IdentifierStyle\\log\\dump\\"+project+"_test.csv";
        String LogOutput = "C:\\Users\\delll\\IdeaProjects\\DatasetCreate\\src\\main\\resources\\log.txt";
        System.out.println(ExecuteCommand(projectpath, "git rev-parse HEAD ",LogOutput));


        try {
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader(csvPath);
            while (csvReader.readRecord()){
                // 读一整行
                // System.out.println(csvReader.getRawRecord());
                // 打印标识符变化情况
                System.out.println(csvReader.get(3));
                //生成对比的两个文件
                String locHis=csvReader.get(2);
                String his=csvReader.get(3);
                String[] hisId=his.split("<-");
                String[] loc=locHis.split("<=");
                //第一个版本比对

                //String curCom=finalCom;
//                String oldCom=csvReader.get(4);


                for(int i=0;i<hisId.length-1;i++){
                    String change=hisId[i]+"<-"+hisId[i+1];
                    System.out.println(change);
//                    System.out.println(curCom+","+oldCom);
                    String curCom=csvReader.get(4+i);
                    //生成两个文件传入ProjectDiff
                    genFile(project,loc[i],curCom,change);
                    ProjectDiff(change);
                    //删除新旧文件夹中的对比文件
                    curCom=csvReader.get(4+(i+1));


                }
                //结束后把old文件夹和new文件夹中的内容清空



            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void ProjectDiff(String change) {
        //读取旧文件，新文件，对比
        //读取变化的标识符
        String changeId=change;
    }

    private static void genFile(String project, String loc, String curCom, String change) throws Exception {
        String[] srcAnddst=loc.split("<-");

        change=change.replace("<-","_");
        //获取FileName和前面的文件夹名字
        String[] data_n=srcAnddst[0].split("\\\\");
        String fileName_n=data_n[data_n.length-1];

        String[] data_o=srcAnddst[1].split("\\\\");
        String fileName_o=data_o[data_o.length-1];




        //newCom读取当前的代码
        generateNew(srcAnddst[0],curCom,fileName_n,"C:\\project\\IdentifierStyle\\data\\VersionDB\\raw_project\\"+project+"\\"+curCom+"_"+change+"_"+fileName_n);

        //oldcam读取记录代码的上一个版本
        generateOld(srcAnddst[1],fileName_o,"C:\\project\\IdentifierStyle\\data\\VersionDB\\raw_project\\"+project+"_old\\"+curCom+"_"+change+"_"+fileName_o);

    }

    private static void generateOld(String location,String fileName, String output) throws Exception {
        System.out.println("location="+location);

        String[] data=location.split("\\\\");
//        for (String s:data){
//            System.out.println("generateNew======>"+s);
//
//        }

        String proj=data[0]+"\\\\"+data[2]+"\\\\"+data[4]+"\\\\"+data[6]+"\\\\"+data[8]+"\\\\"+data[10];

        //先回退到版本old,再回退到上一个版本
//        ExecuteCommand(proj,"git reset --hard "+oldCom,"C:\\Users\\delll\\IdeaProjects\\DatasetCreate\\src\\main\\resources\\result.txt");
//        copyTo(location,output);
        ExecuteCommand(proj,"git reset --hard \"HEAD^\"","C:\\Users\\delll\\IdeaProjects\\DatasetCreate\\src\\main\\resources\\result.txt");


        copyTo(location,output);

    }

    private static void generateNew(String location, String curCom,String fileName,String output) throws Exception {

        String[] data=location.split("\\\\");
//        for (String s:data){
//            System.out.println("generateNew======>"+s);
//
//        }

        String proj=data[0]+"\\\\"+data[2]+"\\\\"+data[4]+"\\\\"+data[6]+"\\\\"+data[8]+"\\\\"+data[10];

        ExecuteCommand(proj,"git reset --hard "+curCom,"C:\\Users\\delll\\IdeaProjects\\DatasetCreate\\src\\main\\resources\\result.txt");
        copyTo(location,output);
    }

    private static void copyTo(String location, String output) throws IOException {
        Boolean validFile=match(output);
        //读取
        File in=new File(location);

        //写入
        File out=new File(output);
        if(in.exists() && validFile) {
            System.out.println("hello====");
            BufferedReader br = new BufferedReader(new FileReader(in));
            BufferedWriter bw = new BufferedWriter(new FileWriter(out));

            String line = "";
            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.newLine();
            }

            br.close();
            bw.close();
        }
    }

    private static Boolean match(String fileName) {
        return fileName.matches("[^/\\\\<>*?|\"]+\\.[^/\\\\<>*?|\"]+");
    }

    private static String readCurCommit(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line = br.readLine();

        br.close();
        return line;
    }

    public static String ExecuteCommand(String projectdir,String cmd,String output) throws Exception
    {
        String final_com=null;
        System.out.println("projectdir:"+projectdir);

        String[] command =
                {
                        "cmd",
                };
        Process p = Runtime.getRuntime().exec(command);
        new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
        new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
        PrintWriter stdin = new PrintWriter(p.getOutputStream());
        stdin.println("c:");
        stdin.println("cd "+projectdir);

            stdin.println(cmd + " > " + output);



        stdin.close();
        int returnCode = p.waitFor();
        System.out.println("Return code = " + returnCode);

        try (FileReader reader = new FileReader(output);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            //网友推荐更加简洁的写法
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                //System.out.println(line);
                final_com=line;
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return final_com;
    }
}
