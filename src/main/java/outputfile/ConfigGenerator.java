package outputfile;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import services.FileSystemService;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigGenerator {
    public ConfigGenerator(){

    }
    private String path;
    private List<File> files = new ArrayList<>();
    public String getPath(){
        return this.path;
    }
    public void setPath(String path){
        this.path = path;
    }
    private String saveName;
    public void setSaveName(String saveName){
        this.saveName=saveName;
    }
    public void obtainFiles(String path){
        File f = new File(path);
        if(f.isDirectory()){
            System.out.println("directory found");
            File[] fl = f.listFiles();
            for(File fl1 : fl){
                if(fl1.isDirectory()){
                    obtainFiles(fl1.getAbsolutePath());
                }
                else{
                    files.add(fl1);
                }

            }
        }
        else{

            files.add(f);
        }

    }
    public void printFiles(){
        for(File file:files){
            System.out.println(file);
        }
    }

    public void generateConfig() throws IOException {
        String name = "";
        String type = "";
        int inMethod = 0;
        int rowNum1 = 0;
        int rowNum2 = 0;
        int isAbs = 0;
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Classes");
        HSSFSheet sheet1 = wb.createSheet("Contents");
        OutputStream fileOut = new FileOutputStream(FileSystemService.getApplicationHomePath().toString()+ "\\" + saveName +".xls");

        for(File file:files){
            HashMap<String, String> nametype = new HashMap<>();
            String mtype="";
            String mname = "";
            String msign= "";
            String mvar = "";
            String temp = "";
            BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
            String st;
            while((st=br.readLine())!=null){
            if(st.contains("public") && st.contains("class")){
                   String[] c = st.split(" ");
                   for(int i=0;i<c.length;i++){
                       if(c[i].equals("class")){
                           sheet.createRow(rowNum1).createCell(1).setCellValue("Name:" + c[i+1].replace("{",""));
                           name = c[i+1].replace("{","");
                           type = c[i];
                           rowNum1++;
                       }
                   }
                   if(st.contains("extends")){
                       for(int i=0;i<c.length;i++){
                           if(c[i].equals("extends")){
                               if(c[i+1].replace("{","").equals("Exception") || c[i+1].replace("{","").equals("RuntimeException"))
                               {
                                   sheet.createRow(rowNum1++).createCell(1).setCellValue("Type:exception");
                                   sheet.createRow(rowNum1).createCell(1).setCellValue("Extends:" + c[i+1].replace("{",""));
                                   type = "exception";
                                   rowNum1++;
                               }
                               else{
                                   sheet.createRow(rowNum1++).createCell(1).setCellValue("Type:class");
                                   sheet.createRow(rowNum1).createCell(1).setCellValue("Extends:" + c[i+1].replace("{",""));

                                   rowNum1++;
                               }

                           }
                       }

                   }else{
                                sheet.createRow(rowNum1++).createCell(1).setCellValue("Type:" + type);
                                sheet.createRow(rowNum1).createCell(1).setCellValue("Extends:");
                                rowNum1++;
                   }
                   if(st.contains("implements")){
                       for(int i=0;i<c.length;i++){
                           if(c[i].equals("implements")){
                               sheet.createRow(rowNum1).createCell(1).setCellValue("Implements:" + c[i+1].replace("{",""));

                               rowNum1++;
                           }
                       }
                   }else{
                       sheet.createRow(rowNum1).createCell(1).setCellValue("Implements:");
                       rowNum1++;
                   }
                   if(st.contains("abstract")){
                       sheet.createRow(rowNum1).createCell(1).setCellValue("abstract(Y/N):Y");
                       rowNum1++;
                       isAbs = 1;
                   }else{
                       sheet.createRow(rowNum1).createCell(1).setCellValue("abstract(Y/N):N");
                       rowNum1++;
                   }

            }else if(st.contains("interface")){
                    String[] c = st.split(" ");
                    for(int i=0;i<c.length;i++){
                        if(c[i].equals("interface")){
                            sheet.createRow(rowNum1).createCell(1).setCellValue("Name:" + c[i+1].replace("{",""));
                            type = c[i];
                            name = c[i+1];
                            rowNum1++;
                        }

                    }
                sheet.createRow(rowNum1++).createCell(1).setCellValue("Type:interface");
                   if(st.contains("extends")){
                       for(int i=0;i<c.length;i++){
                           if(c[i].equals("extends")){
                               sheet.createRow(rowNum1).createCell(1).setCellValue("Extends:" + c[i+1].replace("{",""));
                               rowNum1++;
                           }
                       }
                   }else{
                       sheet.createRow(rowNum1).createCell(1).setCellValue("Extends:");
                       rowNum1++;
                   }
                    sheet.createRow(rowNum1++).createCell(1).setCellValue("Implements:");
                    sheet.createRow(rowNum1++).createCell(1).setCellValue("abstract(Y/N):N");
            }
             else {
                if(type.equals("class")){
                    sheet1.createRow(rowNum2++).createCell(1).setCellValue("Class" + ":" + name.replace("{",""));
                }
                else{
                    if(!type.equals("exception"))
                    sheet1.createRow(rowNum2++).createCell(1).setCellValue(type + ":" + name.replace("{",""));
                }

                String t = "";
                String[] cgs = new String[4];
                for (int k = 0; k < cgs.length; k++) {
                    cgs[k] = "0";
                }
                if (st.contains("Get")) {
                    cgs[2] = "1";
                }
                if (st.contains("Set")) {
                    cgs[3] = "1";
                }
                if (!st.contains("{")) {
                    while ((st.contains("public") || st.contains("private") || st.contains("protected")) && !st.contains("main") && !st.contains("abstract")) {
                        String[] c = st.split(" ");
                        if (c.length == 3) {
                            t = t + c[2].replace(";", "") + "(" + c[1] + ";" + c[0] + "),";
                        } else if (c.length == 4) {
                            t = t + c[3].replace(";", "") + "(" + c[2] + ";" + c[1] + ";" + c[0] + "),";
                        } else if (c.length == 5) {
                            t = t + c[4].replace(";", "") + "(" + c[3] + ";" + c[0] + ";" + c[1] + " " + c[2] + "),";
                        }
                        st = br.readLine();
                    }

                }

                while ((st = br.readLine()) != null) {
                    if (st.contains("{")) {
                        if (st.contains(name)) {
                            String stuff = "";
                            String st1 = br.readLine();
                            while (!st1.contains("}")) {
                                stuff = stuff + st1;
                                st1 = br.readLine();
                            }
                            if (stuff.isEmpty()) {
                                cgs[0] = "1";
                            } else {
                                cgs[1] = "1";
                            }
                        }
                        if (!st.contains(name) && !st.contains("Get") && !st.contains("Set") && st.contains("public") && !st.contains("///")) {
                            System.out.println(name+type);
                            mtype = "";
                            String[] st1 = st.split("[(]")[0].split(" ");
                            msign = st1[st1.length - 1] + "(" + st.split("[(]")[1].replace("{", "");
                            msign = msign.replace(",", ";");
                            for(int j=0;j<st1.length-1;j++){
                                if(j == st1.length-2){
                                    mtype = mtype + st1[j];
                                }
                                else{
                                    mtype = mtype + st1[j] + " ";
                                }
                            }
                            nametype.put(msign, mtype);
                        }
                        if (st.contains("Get")) {
                            cgs[2] = "1";
                        }
                        if (st.contains("Set")) {
                            cgs[3] = "1";
                        }

                    }
                }
                if(type.equals("interface")){
                    BufferedReader br1 = new BufferedReader(new FileReader(file.getAbsolutePath()));
                    String rd;
                    while((rd = br1.readLine())!=null){
                            if((rd.contains("private") && rd.contains("{")) || (rd.contains("default") && rd.contains("{"))|| (rd.contains("static")&& rd.contains("{"))){
                                String[] rd1 = rd.split("[(]")[0].split(" ");
                                mtype = "";
                                for(int j=0;j< rd1.length-1;j++){
                                    if(j == rd1.length-2){
                                        mtype = mtype + rd1[j];
                                    }
                                    else{
                                        mtype = mtype + rd1[j] + " ";
                                    }
                                }
                                msign = rd1[rd1.length-1] + "(" + rd.split("[(]")[1].replace("{", "");
                                msign = msign.replace(",",";");
                                nametype.put(msign,mtype);
                            }
                            else if(!rd.contains("private") && !rd.contains("default") && !rd.contains("static") && !rd.contains("=") && rd.contains(";")){
                                String[] rd1 = rd.split("[(]")[0].split(" ");
                                mtype = "";
                                for(int j=0;j< rd1.length-1;j++){
                                    if(j == rd1.length-2){
                                        mtype = mtype + rd1[j];
                                    }
                                    else{
                                        mtype = mtype + rd1[j] + " ";
                                    }
                                }
                                msign = rd1[rd1.length-1] + "(" + rd.split("[(]")[1].replace(";","") + rd.split("[)]")[1];
                                msign = msign.replace(";","");
                                msign = msign.replace(",", ";");
                                nametype.put(msign,mtype);
                            }
                    }
                }
                if(type.equals("class")){
                    BufferedReader br2 = new BufferedReader(new FileReader(file.getAbsolutePath()));
                    String rd2;
                    while((rd2=br2.readLine())!=null){
                        if(rd2.contains("void main")){
                            msign = "psvm()";
                            mtype = "void";
                            nametype.put(msign,mtype);
                            break;
                        }
                    }
                }
                if(isAbs == 1){
                    BufferedReader br2 = new BufferedReader(new FileReader(file.getAbsolutePath()));
                    String rd2;
                    while((rd2=br2.readLine())!=null){
                        if(rd2.contains("abstract") && !rd2.contains("class")){
                            mtype = "";
                            String[] rd1 = rd2.split("[(]")[0].split(" ");
                            for(int j =0;j<rd1.length-1;j++){
                                if(j == rd1.length-2){
                                    mtype = mtype + rd1[j];
                                }
                                else{
                                    mtype = mtype + rd1[j] + " ";
                                }
                            }
                            msign = rd1[rd1.length-1] + "(" +  rd2.split("[(]")[1].replace(";","");
                            nametype.put(msign, mtype);
                        }
                    }
                }
                  for(Map.Entry<String,String> entry:nametype.entrySet()){
                      System.out.println(entry);
                      temp = temp+ entry.getKey().split("[)]")[0] + "),";
                  }

                  if(!type.equals("exception")){
                      sheet1.createRow(rowNum2).createCell(1).setCellValue("Variables:" + t);
                      rowNum2++;
                      sheet1.createRow(rowNum2++).createCell(1).setCellValue("Methods:"+temp);
                      for(Map.Entry<String,String> entry: nametype.entrySet()){

                          if(entry.getKey().contains("throws")){

                              sheet1.createRow(rowNum2++).createCell(1).setCellValue(entry.getValue()+"_"  + entry.getKey().split("[)]")[0] + ")" + ":,/,:"
                                      + entry.getKey().split("[)]")[1].split(" ")[1]);
                          }
                          else{
                              sheet1.createRow(rowNum2++).createCell(1).setCellValue(entry.getValue()+"_"  + entry.getKey() + ":,/,");
                          }

                      }
                      if(!type.equals("interface"))
                      sheet1.createRow(rowNum2++).createCell(1).setCellValue("constructor(no-arg),constructor(arg),getters,setters:" + cgs[0] + "," + cgs[1]+"," + cgs[2] + "," +cgs[3]);

                  }


                  break;

            }

            }

        }
        wb.write(fileOut);
        }

}
