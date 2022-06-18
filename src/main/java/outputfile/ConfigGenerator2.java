package outputfile;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import services.FileSystemService;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigGenerator2 {

    public ConfigGenerator2(){

    }
    private String path;
    private String saveName;
    public void setSaveName(String saveName){
        this.saveName=saveName;
    }
    private List<File> files = new ArrayList<>();
    public String getPath(){
        return this.path;
    }
    public void setPath(String path){
        this.path = path;
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
    public void generate() throws IOException {
        int rownum1=0;
        int rownum2=0;
        String structName="";
        String structType = "";
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Classes");
        HSSFSheet sheet1 = wb.createSheet("Contents");
        OutputStream fileOut = new FileOutputStream(FileSystemService.getApplicationHomePath().toString()+ "\\" + saveName +".xls");
        for(File file:files){
            HashMap<String,List<String>> nametype = new HashMap<>();
            String t="";
            String mname="";
            String mtype="";
            String tempr = "";
            BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
            String st;
            while((st=br.readLine())!=null){ ///take content for first sheet
                String name ="";
                String type ="";
                String ext ="";
                String imp ="";
                String abs ="N";
                if((st.contains("public") && st.contains("class") && !st.contains("\"")) || (!st.contains("\"") && st.contains("public") && st.contains("Interface"))){
                    System.out.println(st);
                    String[] s = st.split(" ");
                   for(int i=0;i<s.length;i++){
                       if(s[i].equals("class") || s[i].equals("Interface")){
                          type = s[i];
                          name = s[i+1];
                          structName=name;
                          structType=type;
                       }
                       if(s[i].equals("extends")){
                           ext = s[i+1];
                       }
                       if(s[i].equals("implements")){
                           imp = s[i+1].replace("{","");
                       }
                       if(s[i].equals("abstract")){
                           abs = "Y";
                       }

                   }
                    sheet.createRow(rownum1++).createCell(1).setCellValue("Name:"+name);
                    sheet.createRow(rownum1++).createCell(1).setCellValue("Type:"+type);
                    sheet.createRow(rownum1++).createCell(1).setCellValue("Extends:"+ext);
                    sheet.createRow(rownum1++).createCell(1).setCellValue("Implements:"+imp);
                    sheet.createRow(rownum1++).createCell(1).setCellValue("abstract(Y/N):"+abs);
                }

            }
            if(structType.equals("class")){
                sheet1.createRow(rownum2++).createCell(1).setCellValue("Class:" + structName);
            }
            else{
                sheet1.createRow(rownum2++).createCell(1).setCellValue("Interface:" + structName);
            }
            br = new BufferedReader(new FileReader(file.getAbsolutePath()));
            while((st=br.readLine())!=null){///class specific variables
                if((st.contains("public") || st.contains("private") || st.contains("protected")) && !st.contains("{")){
                   String[] s = st.split(" ");
                    List<String> temp = new ArrayList<>();
                   for(String s1:s){
                       if(!s1.equals("")){
                           temp.add(s1);
                       }
                   }
                   for(int i=0;i<temp.size();i++){
                       if(temp.get(i).equals("public") || temp.get(i).equals("private") || temp.get(i).equals("protected")){
                           if(temp.get(i+1).equals("static") && !temp.get(i+2).equals("final")){
                               t = t+ temp.get(i+3).replace(";","") + "(" + temp.get(i+2) + ";" + temp.get(i) +";"+temp.get(i+1) +"),";
                           }
                           if(temp.get(i+1).equals("static") && temp.get(i+2).equals("final")){
                               t = t+temp.get(i+4).replace(";","") + "(" + temp.get(i+3) + ";" + temp.get(i) + ";" + temp.get(i+1) + " " + temp.get(i+2)+ "),";
                           }
                           if(temp.get(i+1).equals("final")){
                               t = t+temp.get(i+3).replace(";","") + "(" + temp.get(i+2) + ";" + temp.get(i) + ";" + temp.get(i+1)+"),";
                           }
                           if(!temp.get(i+1).equals("static") && !temp.get(i+1).equals("final")){
                               t = t+temp.get(i+2).replace(";","") + "(" + temp.get(i+1) +";" + temp.get(i) + "),";
                           }
                       }
                   }

                }

            }
            sheet1.createRow(rownum2++).createCell(1).setCellValue("Variables:"+t);
            br = new BufferedReader(new FileReader(file.getAbsolutePath()));
            while((st=br.readLine())!=null){ ///extracting methods


                         if((st.contains("public")&& st.contains("(") && st.contains(")")) && (!st.contains(";") && !st.contains("\"") && !st.contains("=") &&!st.contains("set") &&!st.contains("get") && !st.contains(structName))){
                             List<String> tmp = new ArrayList<>();
                             List<String> tmp2 = new ArrayList<>();
                             String tt="";
                             String n1 = st.split("[(]")[0];
                             String[] n2 = n1.split(" ");
                             for(int i=0;i<n2.length;i++){
                                 if(!n2[i].equals("")){
                                     tmp.add(n2[i]);
                                 }
                             }
                            mname = tmp.get(tmp.size()-1);
                            n1 = st.split("[(]")[1];
                            String n3 = n1.split("[)]")[0];
                            mname = mname + "(" + n3 + ")";
                            for(int i=1;i<n2.length-1;i++){
                                if(!n2[i].equals("")){
                                   tt = tt+n2[i] + " ";
                                }
                            }
                            String use = tt.split(" ", 2)[1];
                            tmp2.add(use);

                            if(st.contains("throws") && !st.contains("\"")){
                                String s10 = st.split("[)]")[1];
                                String[] s11 = s10.split(" ");
                                for(int i=0;i<s11.length;i++){
                                    if(!s11[i].equals("") && !s11[i].equals("throws")){
                                        tmp2.add(s11[i]);
                                    }
                                }
                            }
                             nametype.put(mname,tmp2);
                         }

            }
            for(Map.Entry<String,List<String>>entry:nametype.entrySet()){
                tempr = tempr + entry.getKey() + ",";
            }
            sheet1.createRow(rownum2++).createCell(1).setCellValue("Methods:"+tempr);
            for(Map.Entry<String,List<String>>entry:nametype.entrySet()){
                List<String> dosmt = entry.getValue();
                String stuff="";
                for(int i=1;i<dosmt.size();i++){
                    stuff = stuff + dosmt.get(i).replace("{", "");
                }
                sheet1.createRow(rownum2++).createCell(1).setCellValue(entry.getKey().split("[(]")[0] + "(" + entry.getValue().get(0) + "):,/,:" + stuff);
            }
        }
        wb.write(fileOut);
    }

}

