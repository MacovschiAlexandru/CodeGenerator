package excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;


public class ExcelController {
    private List<String> className = new ArrayList<String>();
    private List<String> classType = new ArrayList<String>();
    private List<String> classExtras = new ArrayList<String>();
    private HashMap<String,List<String>> Impl = new HashMap<>();
    private List<Integer> abstractization1 = new ArrayList<Integer>();

    private String classname;
    public ExcelController(String path) {
this.path = path;
    }
private String path;
    public void setPath(String path){
        this.path = path;
    }
    public List<Integer> getAbstractization1(){
        return this.abstractization1;
    }
    public List<String> getClassName(){
        return this.className;
    }
    public List<String> getClassType(){
        return this.classType;
    }
    public List<String> getClassExtras(){
        return this.classExtras;
    }
    public HashMap<String, List<String>> getImpl(){
        return this.Impl;
    }
    public void getClassInformation() throws IOException {
        FileInputStream fis = new FileInputStream(path);
        HSSFWorkbook wb = new HSSFWorkbook(fis);
            HSSFSheet sheet = wb.getSheetAt(0);
            FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
            System.out.println("Sheet name is: " + sheet.getSheetName());
            for (Row row : sheet) {
                for (Cell cell : row) {
                    if(cell.getStringCellValue().contains("Name") && cell.getStringCellValue().split(":")[0].equals("Name")){
                     String[] c = cell.getStringCellValue().split(":");
                    className.add(c[1]);
                    classname = c[1];
                     }
                    if(cell.getStringCellValue().contains("Type") && cell.getStringCellValue().split(":")[0].equals("Type")){
                      String[] c = cell.getStringCellValue().split(":");
                      classType.add(c[1]);
                    }
                    if(cell.getStringCellValue().contains("Implements") && cell.getStringCellValue().split(":")[0].equals("Implements")){
                      String[] c = cell.getStringCellValue().split(":");
                      List<String> impl = new ArrayList<>();
                      if(c.length!=1){
                          String[] c1= c[1].split(",");
                          if(c1.length!=0){
                              for(int i=0;i<c1.length;i++){
                                 impl.add(c1[i]);
                              }
                          }
                          Impl.put(classname, impl);
                      }
                      else{
                          Impl.put(classname, impl);
                      }
                    }
                    if(cell.getStringCellValue().contains("Extends") && cell.getStringCellValue().split(":")[0].equals("Extends")){
                        String[] c =cell.getStringCellValue().split(":",5);
                        if(!c[1].equals(""))
                        classExtras.add(c[1]);
                        else
                            classExtras.add("Nothing");

                    }
                    if(cell.getStringCellValue().contains("abstract") && cell.getStringCellValue().split(":")[0].equals("abstract(Y/N)")){
                        String[] c = cell.getStringCellValue().split(":",5);
                        if(c[1].equals("Y"))
                            abstractization1.add(1);
                        else
                            abstractization1.add(0);
                    }

                    }

                }

            }




    public void printClasses(){
        for(int i=0;i<className.size();i++)
        {
            System.out.println(className.get(i));
            for(Map.Entry<String,List<String>> entry:Impl.entrySet()){
                if(entry.getKey().equals(className.get(i))){
                    System.out.println(entry);
                }
            }
            System.out.println(classType.get(i));
            System.out.println(classExtras.get(i));
            System.out.println(abstractization1.get(i));

        }
    }
}

