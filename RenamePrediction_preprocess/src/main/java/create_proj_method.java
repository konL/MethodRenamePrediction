import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.IOException;

public class create_proj_method {
    public static void main(String[] args) throws IOException {
        //Extract renaming method name from a project
        ProjectRun("flink");

    }

    private static void ProjectRun(String proj) throws IOException {
     
        String dataSource = "C:\\project\\IdentifierStyle\\log\\dump\\" + proj + ".csv";
        String outputFile = "C:\\project\\IdentifierStyle\\log\\dump\\" + proj + "_method.csv";
        // 创建CSV对象
        CsvWriter csvWriter = new CsvWriter(outputFile);
        CsvReader stmtReader = new CsvReader(dataSource);
        while (stmtReader.readRecord()) {
                String[] opdata = stmtReader.getRawRecord().split(",");
                String changeIden = stmtReader.get(3);
                int changeNum = (changeIden.split("<-").length) - 1;
                String stmt = stmtReader.get(4 + changeNum);
                String[] d=changeIden.split("<-");
                 //分类为函数,则写入到新文件
                if (stmt.contains( d[0]+ "(")||stmt.contains( d[0]+ " throw")) {
                    csvWriter.write("method");
                    csvWriter.writeRecord(opdata);
                    System.out.println("method------->" + stmt);
                }

        }
        csvWriter.close();
    }
}


