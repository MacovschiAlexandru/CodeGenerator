package outputfile;

import excel.ExcelController;
import excel.ExcelController2;
import services.FileSystemService;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaFileGenerator {
    private File file;
    private String dir;


    private  ExcelController excelFirstSheet;
    private  ExcelController2 excelController2;
    public JavaFileGenerator(ExcelController excelFirstSheet, ExcelController2 excelController2){
        this.excelFirstSheet=excelFirstSheet;
        this.excelController2=excelController2;

    }
    public void setDir(String dir){
        this.dir=dir;
    }


public void createFile() throws IOException{

        excelFirstSheet.getClassInformation();
        excelController2.getClassContent();
        excelFirstSheet.printClasses();
        excelController2.printData();
        int k; /// general variable for iteration through lists needed to write the code
        for(int i=0;i<excelFirstSheet.getClassName().size();i++){
            int isMainF=0;
            int isStatic=0;
            String filename="";
                filename = dir.toString();
                filename +="\\" +  excelFirstSheet.getClassName().get(i) + ".java";
                file = new File(filename);
                System.out.println(filename);
                boolean res;
                try{
                    res = file.createNewFile();
                    if(res){
                        System.out.println("file created");
                    }
                    else{
                        System.out.println("file already exists");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            FileWriter writer = new FileWriter(filename);
            writer.write("public ");
            if(excelFirstSheet.getAbstractization1().get(i) == 1){
                writer.write("abstract ");
            }
            if(excelFirstSheet.getClassType().get(i).equals("exception")){
                writer.write("class");
            }
            else{
                writer.write( excelFirstSheet.getClassType().get(i));
            }

            if(excelFirstSheet.getClassType().get(i).contains("static")){
                isStatic =1;
            }
            writer.write( " " + excelFirstSheet.getClassName().get(i));

            if(excelFirstSheet.getClassExtras().get(i).equals("Nothing")){

            }
            else{
                writer.write(" extends " + excelFirstSheet.getClassExtras().get(i));
            }
            for(Map.Entry<String, List<String>>entri:excelFirstSheet.getImpl().entrySet()){
                if(excelFirstSheet.getClassName().get(i).equals(entri.getKey())){
                    List<String> Implementations = entri.getValue();
                    if(!Implementations.isEmpty())
                    writer.write(" implements ");
                    for(int p=0;p<Implementations.size();p++){
                        if(p==Implementations.size()-1){
                            writer.write(Implementations.get(p));
                        }
                        else{
                            writer.write(Implementations.get(p)+",");
                        }
                    }
                }
            }
            writer.write("{" + "\n");
            if(excelFirstSheet.getClassType().get(i).equals("exception")){
                writer.write("public " + excelFirstSheet.getClassName().get(i) + "(){\n");
                writer.write("super();\n}\n");
                writer.write("public " + excelFirstSheet.getClassName().get(i) + "(String message){\nsuper(message);\n}\n" );
            }
            for(int j=0;j<excelController2.getClassName().size();j++){
              if(filename.contains(excelController2.getClassName().get(j) +".java") == true){
                  System.out.println(filename);
                   for(Map.Entry<String, List<String>> entry: excelController2.getClassVariables().entrySet()){
                       if(excelController2.getClassName().get(j).equals(entry.getKey())) {
                           List<String> variables = entry.getValue();
                           for (k = 0; k < variables.size(); k = k + 4) {
                               if(excelFirstSheet.getClassType().get(i).equals("interface")){
                                   writer.write("public ");
                               }
                               else{
                                   writer.write(variables.get(k) + " ");
                               }

                               if(!variables.get(k+3).equals(""))
                                   writer.write(variables.get(k + 3) + " ");
                               writer.write(variables.get(k + 1) + " ");
                               writer.write(variables.get(k + 2) + ";");
                               writer.write("\n");

                           }
                           break;
                       }

                   }
                   for(Map.Entry<String, List<Integer>> entry:excelController2.getConsGettersSetters().entrySet()){
                       if(excelController2.getClassName().get(j).equals(entry.getKey())){
                           List<Integer> cgs = entry.getValue();
                               if(cgs.get(0) == 1){
                                   writer.write("public " + excelController2.getClassName().get(j) + "(){\n\n}\n");
                               }
                           if(cgs.get(1) == 1){
                               String t = "";
                               String t1 = "";
                               writer.write("public "+ excelController2.getClassName().get(j));
                               writer.write("(");
                               for(int q=0;q < excelFirstSheet.getClassName().size();q++)
                               {
                                   if(!excelFirstSheet.getClassExtras().get(q).equals("Nothing")){
                                       if(excelFirstSheet.getClassName().get(q).equals(excelController2.getClassName().get(j))){
                                           List<String> extendedVars = excelController2.getClassVariables().get(excelFirstSheet.getClassExtras().get(q));
                                               for(int q1=0;q1<extendedVars.size();q1=q1+4){
                                                   t = t+extendedVars.get(q1+2) + ", ";
                                                   t1 = t1+ extendedVars.get(q1+1)+" "+ extendedVars.get(q1+2).split("=")[0] + ", ";
                                               }
                                       }

                                   }
                               }

                               if(!t.isEmpty())
                               {
                                   t = t.substring(0,t.length()-2);
                               }
                               if(!t1.isEmpty()){
                                   t1 =t1.substring(0,t1.length()-2);
                               }
                               writer.write(t1);
                               System.out.println(t1);
                               for(Map.Entry<String, List<String>> entry1:excelController2.getClassVariables().entrySet()){
                                  if(excelController2.getClassName().get(j).equals(entry1.getKey())){
                                      List<String> classV = entry1.getValue();
                                      System.out.println(classV.size());
                                      if(classV.size()==0){
                                          writer.write(")");
                                      }
                                      else{
                                          if(!t1.isEmpty()){
                                              writer.write(", ");
                                          }
                                              for(int z=0;z<classV.size();z=z+4){
                                                  writer.write(classV.get(z+1) + " " + classV.get(z+2));
                                                  if (z + 3 != classV.size() - 1) {
                                                      writer.write(", ");
                                                  } else {
                                                      writer.write(")");
                                                  }
                                              }


                                      }


                                      writer.write("{\n");
                                      if(!t.isEmpty()){
                                          writer.write("super(" + t + ");\n");
                                      }
                                      for(int z=0;z<classV.size();z=z+4){
                                          writer.write("this." + classV.get(z+2) + " = " + classV.get(z+2)+";\n");
                                      }
                                      writer.write("\n}\n");
                                  }

                               }
                           }
                         if(cgs.get(2) == 1){
                             for(Map.Entry<String, List<String>> e1:excelController2.getClassVariables().entrySet()){
                                 if(excelController2.getClassName().get(j).equals(e1.getKey())){
                                     List<String> getterStuff = e1.getValue();
                                     for(int q2 = 0;q2 <getterStuff.size();q2=q2+4){
                                         writer.write("\npublic " + getterStuff.get(q2+1) + " Get"+ getterStuff.get(q2+2) +"(){\n");
                                         writer.write("return " + getterStuff.get(q2+2) + ";\n}\n");
                                     }
                                 }
                             }
                         }
                         if(cgs.get(3)==1){
                             for(Map.Entry<String,List<String>> e1:excelController2.getClassVariables().entrySet()){
                                 if(excelController2.getClassName().get(j).equals(e1.getKey())){
                                     List<String> setterStuff = e1.getValue();
                                     for(int q2=0;q2<setterStuff.size();q2=q2+4){
                                         writer.write("\npublic void Set" + setterStuff.get(q2+2) +"(" + setterStuff.get(q2+1) + " " + setterStuff.get(q2+2)+ "){\n");
                                         writer.write("this."+ setterStuff.get(q2+2) + " = " + setterStuff.get(q2+2) + ";\n}\n");
                                     }
                                 }
                             }
                         }

                       }
                   }
                  for(k=0;k<excelController2.getClassMethodsSignature().size();k++) {
                      int isAbs = 0;
                      String tempo="";
                        if(excelFirstSheet.getClassType().get(i).equals("class") || excelFirstSheet.getClassType().get(i).equals("static class")){
                            if(excelFirstSheet.getClassType().equals("static class")){
                                isStatic=1;
                            }
                      List<String> methodSignatures = excelController2.getClassMethodsSignature().get(excelController2.getClassName().get(j));
                      for (int k1 = 0; k1 < methodSignatures.size(); k1++) {
                          if (methodSignatures.get(k1).equals("psvm()")) {
                              isMainF = 1;
                              writer.write("public static void main(String[] args)");
                              writer.write("{ \n");
                          } else {
                              String temp1 = methodSignatures.get(k1);
                              for (Map.Entry<String, String> entry : excelController2.getMethodType().entrySet()) {
                                  if (entry.getKey().equals(temp1 + "_" + excelController2.getClassName().get(j))) {
                                      tempo = temp1 + "_" + excelController2.getClassName().get(j);
                                      writer.write(entry.getValue() + " ");
                                            if(entry.getValue().contains("abstract")){
                                                isAbs = 1;
                                            }
                                            else{
                                                isAbs = 0;
                                            }
                                  }

                              }
                          }
                          if (methodSignatures.get(k1).equals("psvm()")) {

                          } else {
                              if(isAbs == 1){
                              writer.write(methodSignatures.get(k1));
                             if(!excelController2.getExcep().get(tempo).equals("")){
                                 writer.write(" throws " + excelController2.getExcep().get(tempo));
                             }
                             writer.write(";\n");
                              }
                              else{
                                      writer.write(methodSignatures.get(k1));
                                      if(!excelController2.getExcep().get(tempo).equals("")){
                                          writer.write("throws " + excelController2.getExcep().get(tempo) + "{\n");
                                      }
                                      else{
                                          writer.write("{\n");
                                      }
                              }

                          }
                          for (int k2 = 0; k2 < excelController2.getMethodVariables().size(); k2++) {
                              String temp = methodSignatures.get(k1);
                              for (Map.Entry<String, List<String>> entry : excelController2.getMethodVariables().entrySet()) {
                                  if (entry.getKey().equals(temp + "_" + excelController2.getClassName().get(j))) {
                                      List<String> methodVariables = entry.getValue();
                                      for (int k3 = 0; k3 < methodVariables.size(); k3 = k3 + 2) {

                                          writer.write(methodVariables.get(k3 + 1) + " ");
                                          writer.write(methodVariables.get(k3) + ";");
                                          writer.write("\n");

                                      }

                                  }

                              }
                              for (int y = 0; y < excelController2.getMethodContent().size(); y++) {
                                  for (Map.Entry<String, List<String>> entry : excelController2.getMethodContent().entrySet()) {
                                      if (entry.getKey().equals(temp + "_" + excelController2.getClassName().get(j))) {
                                          List<String> methodContent = entry.getValue();
                                          for (int y1 = 0; y1 < methodContent.size(); y1++) {
                                              if (methodContent.get(y1).contains("output")) {
                                                  writer.write("return " + methodContent.get(y1).split(" ")[1] + ";");
                                              } else {
                                                  if(methodContent.get(y1).contains("{")){
                                                      writer.write(methodContent.get(y1) + "\n");
                                                  }
                                                  else if(methodContent.get(y1).contains("}")){
                                                      writer.write(methodContent.get(y1).replace("}","") + "\n}\n");
                                                  }
                                                  else{
                                                      writer.write(methodContent.get(y1) + ";\n");
                                                  }
                                              }
                                          }
                                          if(isAbs == 0)
                                          writer.write("\n/*end of method*/}\n");

                                      }
                                  }
                                  break;
                              }
                              break;
                          }
                      }
                      break;

                  }
                        else{
                            if(excelFirstSheet.getClassType().get(i).equals("interface")){
                                for(Map.Entry<String,List<String>>entri3:excelController2.getClassMethodsSignature().entrySet()){
                                    if(entri3.getKey().equals(excelFirstSheet.getClassName().get(i))){
                                        List<String> imp = entri3.getValue();
                                        for(int o5=0;o5<imp.size();o5++){
                                            String p1 = imp.get(o5)+ "_" + excelFirstSheet.getClassName().get(i);
                                            String p = excelController2.getExcep().get(p1);
                                            System.out.println(p1);
                                            writer.write("\n" + excelController2.getMethodType().get(imp.get(o5) + "_" + entri3.getKey()) + " " + imp.get(o5));
                                            if(!p.equals("")){
                                                if(excelController2.getMethodType().get(p1).contains("private")){
                                                    writer.write("throws " + p + "{\n");
                                                    List<String> intStuff = excelController2.getMethodContent().get(p1);
                                                    for(String s:intStuff){
                                                        writer.write(s +";\n");
                                                    }
                                                    if(intStuff.size()==0){
                                                        writer.write("/*this method is private and it needs implementation*/\n");
                                                    }
                                                    writer.write("}\n");
                                                }
                                                else if(excelController2.getMethodType().get(p1).contains("default")){
                                                    writer.write("throws " + p + "{\n");
                                                    List<String> intStuff = excelController2.getMethodContent().get(p1);
                                                    for(String s:intStuff){
                                                        writer.write(s +";\n");
                                                    }
                                                    if(intStuff.size()==0){
                                                        writer.write("/*this method is default and it needs implementation*/\n");
                                                    }
                                                    writer.write("}\n");
                                                }
                                                else if(excelController2.getMethodType().get(p1).contains("static")){
                                                    writer.write("throws " + p + "{\n");
                                                    List<String> intStuff = excelController2.getMethodContent().get(p1);
                                                    for(String s:intStuff){
                                                        writer.write(s +";\n");
                                                    }
                                                    if(intStuff.size()==0){
                                                        writer.write("/*this method is static and it needs implementation*/\n");
                                                    }
                                                    writer.write("}\n");
                                                }
                                                else{
                                                    writer.write("throws " +p +";\n");
                                                }
                                            }
                                            else{
                                                if(excelController2.getMethodType().get(p1).contains("private")){
                                                    writer.write( "{\n");
                                                    List<String> intStuff = excelController2.getMethodContent().get(p1);
                                                    for(String s:intStuff){
                                                        writer.write(s +";\n");
                                                    }
                                                    if(intStuff.size()==0){
                                                        writer.write("/*this method is private and it needs implementation*/\n");
                                                    }
                                                    writer.write("}\n");
                                                }
                                                else if(excelController2.getMethodType().get(p1).contains("default")){
                                                    writer.write( "{\n");
                                                    List<String> intStuff = excelController2.getMethodContent().get(p1);
                                                    for(String s:intStuff){
                                                        writer.write(s +";\n");
                                                    }
                                                    if(intStuff.size()==0){
                                                        writer.write("/*this method is default and it needs implementation*/\n");
                                                    }
                                                    writer.write("}\n");
                                                }
                                                else if(excelController2.getMethodType().get(p1).contains("static")){
                                                    writer.write( "{\n");
                                                    List<String> intStuff = excelController2.getMethodContent().get(p1);
                                                    for(String s:intStuff){
                                                        writer.write(s +";\n");
                                                    }
                                                    if(intStuff.size()==0){
                                                        writer.write("/*this method is default and it needs implementation*/\n");
                                                    }
                                                    writer.write("}\n");
                                                }
                                                else{
                                                    writer.write(";\n");
                                                }
                                            }
                                        }
                                        writer.write("\n");

                                    }


                                }
                                   break;
                            }
                        }

                  }
              for(Map.Entry<String,List<String>>entri2:excelFirstSheet.getImpl().entrySet()){
                     if(entri2.getKey().equals(excelController2.getClassName().get(j))){
                         List<String> imps = entri2.getValue();
                         for(int o=0;o<imps.size();o++){
                             List<String> imps1 = excelController2.getClassMethodsSignature().get(imps.get(o));
                             for(int o1=0;o1< imps1.size();o1++){
                                 if(!excelController2.getMethodType().get(imps1.get(o1) +"_"+ imps.get(o)).contains("private")
                                 && !excelController2.getMethodType().get(imps1.get(o1) +"_"+ imps.get(o)).contains("default")
                                 && !excelController2.getMethodType().get(imps1.get(o1) +"_"+ imps.get(o)).contains("static")){
                                     writer.write("\n" +excelController2.getMethodType().get(imps1.get(o1) +"_"+ imps.get(o))+" " + imps1.get(o1) + "{");
                                     writer.write("///method declared in " + imps.get(o) + " interface\n\n}\n");
                                 }

                             }
                         }
                     }
                  }
                  String tempo = excelFirstSheet.getClassExtras().get(j);
                  for(Map.Entry<String,List<String>>entri11:excelController2.getClassMethodsSignature().entrySet()){
                      if(entri11.getKey().equals(tempo)){
                          List<String> temp2 = entri11.getValue();
                          for(int m1=0;m1<temp2.size();m1++){
                              for(Map.Entry<String,String>entri12:excelController2.getMethodType().entrySet()){
                                  if(entri12.getKey().equals(temp2.get(m1)+"_"+tempo)){
                                      String tp = entri12.getValue();
                                      if(tp.contains("abstract")){
                                          String[] splitted = tp.split(" ");
                                          writer.write("public ");
                                          for(int m2=1;m2<splitted.length;m2++){
                                              if(!splitted[m2].equals("abstract"))
                                              writer.write(splitted[m2] + " ");
                                          }
                                          for(Map.Entry<String,List<String>>entri13:excelController2.getClassMethodsSignature().entrySet()){
                                              if(entri13.getKey().equals(tempo)){
                                                  List<String> tempor = entri13.getValue();
                                                  for(int m3=0;m3<tempor.size();m3++){

                                                      if(tempor.get(m3).contains(temp2.get(m1))){
                                                          writer.write(tempor.get(m3));
                                                      }
                                                  }
                                              }
                                          }
                                          writer.write("{///method from abstract class " + tempo + " \n\n}\n");
                                      }
                                  }
                              }
                          }
                      }
                  }
              }
            }
            writer.write("\n}");
            writer.close();

        }
}

}
