import excel.ExcelController;
import excel.ExcelController2;
import outputfile.*;
import services.FileSystemService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;


public class MyFrame extends JFrame implements ActionListener {
    private String path;
    private JButton button, button1, button3, button4;
    private JTextField sheetPath = new JTextField();
    private JTextField saveName = new JTextField();
    private JTextField text = new JTextField("ExGen: a new way to generate code!");
    private JTextField saveFolder = new JTextField();
    public MyFrame(String title){
        super(title);
        this.setSize(1200,600);
        this.setLocation(100,100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sheetPath.setPreferredSize(new Dimension(250,40));
        saveName.setPreferredSize(new Dimension(250,40));
        sheetPath.setBackground(Color.ORANGE);
        saveFolder.setBackground(Color.ORANGE);
        saveFolder.setPreferredSize(new Dimension(250,40));
        saveName.setBackground(Color.WHITE);
        button = new JButton("Select sheet");
         button.addActionListener(this);
         button1 = new JButton("Generate config sheet");
         button1.addActionListener(this);
         button3 = new JButton("Generate!");
         button3.addActionListener(this);
        button4 = new JButton("Select folder");
        button4.addActionListener(this);
         Container mainContainer = this.getContentPane();
         mainContainer.setLayout(new BorderLayout(8,6));
         mainContainer.setBackground(Color.YELLOW);
         this.getRootPane().setBorder(BorderFactory.createMatteBorder(4,4,4,4,Color.GREEN));
         text.setHorizontalAlignment(JTextField.CENTER);
         text.setEditable(false);
         saveFolder.setEditable(false);
         text.setBackground(Color.YELLOW);
         mainContainer.add(text);

         //TopPanel
         JPanel topPanel = new JPanel();
         topPanel.setBorder(new LineBorder(Color.BLACK, 3));
         topPanel.setBackground(Color.ORANGE);
         topPanel.setLayout(new FlowLayout(5));
         topPanel.add(button);
         topPanel.add(sheetPath);

         topPanel.add(button4);
         topPanel.add(saveFolder);
         topPanel.add(button3);
         mainContainer.add(topPanel,BorderLayout.NORTH);

         //MidPanel
         JPanel midPanel = new JPanel();
         midPanel.setBorder(new LineBorder(Color.BLACK,3));
         midPanel.setLayout(new FlowLayout(4,4,4));
         midPanel.setBackground(Color.BLUE);

         //SideLeftPanel
         JPanel gridPanel = new JPanel();
         gridPanel.setLayout(new GridLayout(6,1,4,4));
         gridPanel.setBorder(new LineBorder(Color.BLACK,2));
         gridPanel.setBackground(Color.BLUE);
         gridPanel.add(button1);
         gridPanel.add(saveName);
         midPanel.add(gridPanel);
         mainContainer.add(midPanel,BorderLayout.WEST);
         this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
         if(e.getSource() == button){
             JFileChooser fileChooser = new JFileChooser();
             int response = fileChooser.showOpenDialog(null); //select the file you want to open
             if(response == JFileChooser.APPROVE_OPTION){
                         path = fileChooser.getSelectedFile().getAbsolutePath();
                         sheetPath.setText(path);
             }
         }
         if(e.getSource()==button1){
             if(!saveName.getText().isEmpty()) {
                 JFileChooser fileChooser = new JFileChooser();
                 ConfigGenerator cg = new ConfigGenerator();
                 cg.setSaveName(saveName.getText());
                 int resp = fileChooser.showOpenDialog(null);
                 if (resp == JFileChooser.APPROVE_OPTION) {
                     CreateDirectory.makeDir();
                     cg.setPath(FileSystemService.getApplicationHomePath().toString());
                     cg.obtainFiles(fileChooser.getCurrentDirectory().toString());
                     cg.printFiles();
                     try {
                         cg.generateConfig();
                     } catch (IOException exc) {
                         exc.printStackTrace();
                     }
                 }
             }
             else{
                 saveName.setText("Choose a name!");
             }

         }
         if(e.getSource()==button3){
             if(sheetPath.getText().isEmpty()){
                 sheetPath.setText("Please choose a file");
             }
             if(saveFolder.getText().isEmpty()){
                 saveFolder.setText("Please choose a destination");
             }
             else{
                 String[] ext = sheetPath.getText().split("[.]");
                  if(ext[ext.length-1].equals("xls")){
                      CreateDirectory.makeDir();
                      ExcelController ex = new ExcelController(path);
                      ExcelController2 e1 = new ExcelController2(path);
                      JavaFileGenerator f = new JavaFileGenerator(ex,e1);
                      f.setDir(saveFolder.getText());
                      try {
                          f.createFile();
                      } catch (IOException exc) {
                          sheetPath.setText("Something went wrong");
                      }
                      sheetPath.setText(sheetPath.getText() + " - generated!");
                  }
                  else{
                      sheetPath.setText("You did not select the correct file type!");
                  }
             }
         }
         if(e.getSource()==button4){
             JFileChooser fileChooser = new JFileChooser();
             int response = fileChooser.showOpenDialog(null);
             if(response==JFileChooser.APPROVE_OPTION){
                        saveFolder.setText(fileChooser.getCurrentDirectory().getAbsolutePath());
             }
         }

    }

}
