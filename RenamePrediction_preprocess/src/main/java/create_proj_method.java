import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.IOException;

public class create_proj_method {
    public static void main(String[] args) throws IOException {
        ProjectRun("flink");

    }

    private static void ProjectRun(String proj) throws IOException {
        //变化标识符数据源
        String dataSource = "C:\\project\\IdentifierStyle\\log\\dump\\" + proj + ".csv";
        String outputFile = "C:\\project\\IdentifierStyle\\log\\dump\\" + proj + "_method.csv";


        //1.获取操作数据源中的标识符部分


            // 创建CSV读对象

            CsvWriter csvWriter = new CsvWriter(outputFile);

            //读取dump
            CsvReader stmtReader = new CsvReader(dataSource);
            while (stmtReader.readRecord()) {
                String[] opdata = stmtReader.getRawRecord().split(",");
                String changeIden = stmtReader.get(3);
//                    if (changeIden.contains(iden)) {
                int changeNum = (changeIden.split("<-").length) - 1;
                //读取到stmt
                String stmt = stmtReader.get(4 + changeNum);

                String[] d=changeIden.split("<-");
                if (stmt.contains( d[0]+ "(")||stmt.contains( d[0]+ " throw")) {
                    //分类为函数,则写入到新文件
                    csvWriter.write("method");
                    csvWriter.writeRecord(opdata);
                    System.out.println("method------->" + stmt);



                }

        }
        csvWriter.close();
    }
}


