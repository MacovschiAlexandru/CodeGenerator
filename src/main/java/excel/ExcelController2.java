package excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ExcelController2 {

    private List<String> className =new ArrayList<>();
    private HashMap<String, List<String>> classVariables = new HashMap<>();
    private List<String> variableName = new ArrayList<String>();
    private List<String> variableType = new ArrayList<String>();
    private List<String> variableProtection = new ArrayList<String>();
    private HashMap<String, List<String>> classMethodsSignature = new HashMap<>();
    private HashMap<String, List<String>> classMethods = new HashMap<>();
    private HashMap<String, List<String>> methodVariables = new HashMap<>();
    private String classname= "";
    private HashMap<String, String> methodType = new HashMap<>();
    private List<String> classvariableinfo;
    private HashMap<String, List<String>> methodContent = new HashMap<>();
    private HashMap<String, List<Integer>> consGettersSetters = new HashMap<>();
    private HashMap<String,String> excep = new HashMap<>();


    public HashMap<String,String> getExcep(){
        return this.excep;
    }
    public HashMap<String, List<Integer>> getConsGettersSetters(){
        return this.consGettersSetters;
    }
    public HashMap<String, List<String>> getMethodContent(){
        return this.methodContent;
    }
    public HashMap<String, String> getMethodType(){
        return this.methodType;
    }
    public List<String> getClassName(){
        return this.className;
    }
    public HashMap<String, List<String>> getClassVariables(){
        return this.classVariables;
    }
    public HashMap<String, List<String>> getClassMethodsSignature(){
        return this.classMethodsSignature;
    }
    public HashMap<String, List<String>> getClassMethods(){
        return this.classMethods;
    }
    public HashMap<String, List<String>> getMethodVariables(){
        return this.methodVariables;
    }
    public ExcelController2(String path) {
           this.path = path;
    }
     private String path;
    public void setPath(String path){
        this.path = path;
    }
    public void getClassContent() throws IOException {
        FileInputStream fis = new FileInputStream(path);
        HSSFWorkbook wb = new HSSFWorkbook(fis);
        HSSFSheet sheet = wb.getSheetAt(1);
        System.out.println("Sheet name is: " + sheet.getSheetName());
        for (Row row : sheet) {
            for (Cell cell : row) {
                if((cell.getStringCellValue().contains("Class") && cell.getStringCellValue().split(":")[0].equals("Class")) || (cell.getStringCellValue().contains("interface")&& cell.getStringCellValue().split(":")[0].equals("interface"))){
                    String[] c = cell.getStringCellValue().split(":");
                    className.add(c[1]);
                    classname = c[1];
                }
                if(cell.getStringCellValue().contains("constructor")){
                    List<Integer> values = new ArrayList<>();
                    String[] c = cell.getStringCellValue().split(":");
                        String[] v = c[1].split(",");
                        for(int i=0;i<v.length;i++){
                            if(v[i].equals("1")){
                                values.add(1);
                            }
                            else{
                                values.add(0);
                            }
                        }
                    consGettersSetters.put(classname,values);


                }
                if(cell.getStringCellValue().contains("Variables") && cell.getStringCellValue().split(":")[0].equals("Variables")){
                    classvariableinfo = new ArrayList<>();
                    String[] c = cell.getStringCellValue().split(":");
                    if (c.length != 1) {
                            String[] variables = c[1].split(",");
                            for(int i=0;i< variables.length;i++){
                                String name = "";
                                String type = "";
                                String access = "";
                                String exType= "";
                                String[] temp = variables[i].split("[(]");
                                name = temp[0];
                                temp = temp[1].split("[)]");
                                String[] c1 = temp[0].split(";");
                                if(c1.length==3){
                                    type = c1[0];
                                    access = c1[1];
                                    exType = c1[2];
                                }
                                else if(c1.length==2){
                                    type = c1[0];
                                    access=c1[1];
                                }
                                variableProtection.add(access);
                                variableType.add(type);
                                variableName.add(name);
                                classvariableinfo.add(access);
                                classvariableinfo.add(type);
                                classvariableinfo.add(name);
                                classvariableinfo.add(exType);
                            }
                        classVariables.put(classname, classvariableinfo);
                    }
                    else{
                        classVariables.put(classname, classvariableinfo);
                    }
                }
                if(cell.getStringCellValue().contains("Methods") && cell.getStringCellValue().split(":")[0].equals("Methods")){
                    List<String> methods = new ArrayList<String>();
                    List<String> methodNames = new ArrayList<String>();
                   String[] temp1 = cell.getStringCellValue().split(":");
                   if(temp1.length ==1){

                   }
                   else{
                       String[] temp = temp1[1].split(",");
                       for(int i=0;i<temp.length;i++){
                           String methodSignature = temp[i].replace(';', ',');
                           methods.add(methodSignature);
                           String[] temp2 = temp[i].split("[(]");
                           methodNames.add(temp2[0]);

                       }
                   }
                     classMethodsSignature.put(classname,methods);
                   classMethods.put(classname,methodNames);
                }
                for(Map.Entry<String, List<String>> entry : classMethodsSignature.entrySet()){
                    List<String> Methods = entry.getValue();
                    List<String> vars = new ArrayList<>();
                    List<String> content = new ArrayList<>();
                    for(int i=0;i<Methods.size();i++) {

                        if(cell.getStringCellValue().contains(Methods.get(i)) && !cell.getStringCellValue().contains("Methods")) {
                            String[] data = cell.getStringCellValue().split(":");
                            String mtype = cell.getStringCellValue().split("_")[0];
                            methodType.put(Methods.get(i)+"_"+classname,mtype.replace(")",""));
                            if(data.length>1){
                                String[] variables = data[1].split("/");
                                String[] contents = variables[1].split(",");
                                String[] temp = variables[0].split(",");
                                for (int j = 0; j < temp.length; j++) {
                                    String[] temp1 = temp[j].split("[(]");
                                    vars.add(temp1[0]);
                                    String type = temp1[1].replace(")", "");
                                    vars.add(type);
                                }
                                for(int k=0;k<contents.length;k++){
                                    content.add(contents[k]);
                                }
                                methodVariables.put(Methods.get(i)+"_"+classname,vars);
                                methodContent.put(Methods.get(i)+"_"+classname, content);
                                if(data.length == 3){
                                    excep.put(Methods.get(i)+"_"+classname, cell.getStringCellValue().split(":")[2]);
                                }
                                else{
                                    excep.put(Methods.get(i)+"_"+classname, "");
                                }
                            }
                            else{
                                methodVariables.put(Methods.get(i)+"_"+classname,vars);
                                methodContent.put(Methods.get(i)+"_"+classname, content);
                            }

                        }

                    }

                }

            }

        }

    }

    public void printData(){
        System.out.println();
        for(int i=0;i<className.size();i++){
            System.out.println(className.get(i));
        }
        System.out.println();
              for(Map.Entry<String,List<String>> entry:classVariables.entrySet()){
                  System.out.println(entry);

              }
        System.out.println();

              for(Map.Entry<String, List<String>> entry: classMethodsSignature.entrySet()){
                  System.out.println(entry);

              }
        System.out.println();
              for(Map.Entry<String, List<String>> entry: classMethods.entrySet()){
                  System.out.println(entry);

              }
        System.out.println();
              for(Map.Entry<String, List<String>> entry: methodVariables.entrySet()){
                  System.out.println(entry);

              }
        System.out.println();
              for(Map.Entry<String, List<String>> entry: methodContent.entrySet()){
                  System.out.println(entry);

              }
        System.out.println();
              for(Map.Entry<String,String> entry:methodType.entrySet()){
                  System.out.println(entry);

              }
        for(Map.Entry<String, List<Integer>> entry:consGettersSetters.entrySet()){
            System.out.println(entry);
        }

        System.out.println();

        for(Map.Entry<String,String>entry:excep.entrySet()){
            System.out.println(entry);
        }
        System.out.println();
    }
}

