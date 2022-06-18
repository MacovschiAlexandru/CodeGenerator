package outputfile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class RefactorGenerator {
    private List<File> files = new ArrayList<>();
    public RefactorGenerator(){

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
    public String refactorCode(String toReplace, String replacement) throws IOException {
        for(File file:files){
             String content = new String(Files.readAllBytes(file.toPath()));
             content = content.replace(toReplace,replacement);
            System.out.println(content);
        }
        return "";
    }

}
