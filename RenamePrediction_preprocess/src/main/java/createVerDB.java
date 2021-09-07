import com.csvreader.CsvReader;
import detectId.Trace.SyncPipe;

import java.io.*;

public class createVerDB {
    public static void main(String[] args) throws Exception {
        //传入项目名称
        ProjectCommit("flink");
    }

    private static void ProjectCommit(String project) throws Exception {
        //Location of the project folder containing the commit history
        String projectpath="C:\\project\\IdentifierStyle\\data\\GitProject\\"+project;
        //Location of the renaming method database
        String csvPath="C:\\project\\IdentifierStyle\\log\\dump\\"+project+"_method.csv";
        //Temp file
        String LogOutput = "C:\\Users\\delll\\IdeaProjects\\DatasetCreate\\src\\main\\resources\\log.txt";
       

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
                //All changes of the identifier are traversed
                for(int i=0;i<hisId.length-1;i++){
                    String change=hisId[i]+"<-"+hisId[i+1];
                    String curCom=csvReader.get(4+i);
                    //把标识符产生变化的提交中涉及到的文件的前后两个版本下载下来
                    genFile(project,loc[i],curCom,change);
                    curCom=csvReader.get(4+(i+1));


                }



            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private static void genFile(String project, String loc, String curCom, String change) throws Exception {
        //the location of original and current file
        String[] srcAnddst=loc.split("<-");

        change=change.replace("<-","_");
        //获取新的Filelocation
        String[] data_n=srcAnddst[0].split("\\\\");
        String fileName_n=data_n[data_n.length-1];
         //获取旧的Filelocation
        String[] data_o=srcAnddst[1].split("\\\\");
        String fileName_o=data_o[data_o.length-1];




        //newCom读取当前的代码
        generateNew(srcAnddst[0],curCom,fileName_n,"C:\\project\\IdentifierStyle\\data\\VersionDB\\raw_project\\"+project+"\\"+curCom+"_"+change+"_"+fileName_n);

        //oldcam读取记录代码的上一个版本
        generateOld(srcAnddst[1],fileName_o,"C:\\project\\IdentifierStyle\\data\\VersionDB\\raw_project\\"+project+"_old\\"+curCom+"_"+change+"_"+fileName_o);

    }

    private static void generateOld(String location,String fileName, String output) throws Exception {
       

        String[] data=location.split("\\\\");


        String proj=data[0]+"\\\\"+data[2]+"\\\\"+data[4]+"\\\\"+data[6]+"\\\\"+data[8]+"\\\\"+data[10];

        //先回退到版本old,再回退到上一个版本
// 注意这里的git命令由于在windows操作需要做一点字符上的处理，在其他系统上操作git命令也需要修改
        ExecuteCommand(proj,"git reset --hard \"HEAD^\"","C:\\Users\\delll\\IdeaProjects\\DatasetCreate\\src\\main\\resources\\result.txt");


        copyTo(location,output);

    }

    private static void generateNew(String location, String curCom,String fileName,String output) throws Exception {

        String[] data=location.split("\\\\");

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
             BufferedReader br = new BufferedReader(reader)
        ) {
            String line;

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
