package example_1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

class BackgroundMusic implements Runnable {
    private String musicPath;

    public BackgroundMusic(String musicPath) {
        this.musicPath = musicPath;
    }

    @Override
    public void run() {
        try {
            File musicFile = new File(musicPath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY); // 무한 반복
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}


class Task
{
   private JPanel taskPanel;
   public ArrayList<CheckList> checkLists;
   public static int totalScore = 100;
    private String title;
    private JTextField titleTextField;
    public boolean isRemoving = false;

    public Task(String title) {
       checkLists = new ArrayList<>();
        this.title = title;
        titleTextField = new JTextField(title, 15);
        titleTextField.setPreferredSize(new Dimension(200, 40));
        titleTextField.setEditable(false);
        
        Border blackline;
       blackline = BorderFactory.createLineBorder(Color.black);
        
       taskPanel = new JPanel();
       taskPanel.setLayout(new BorderLayout());
       taskPanel.setBorder(blackline);
       
       JPanel taskTopPanel = new JPanel(new BorderLayout()); // 상단 패널
       taskTopPanel.setPreferredSize(new Dimension(420, 50));
       JPanel taskCenterPanel = new JPanel(); // 중앙 패널
       taskCenterPanel.setLayout(new BoxLayout(taskCenterPanel, BoxLayout.Y_AXIS));
       
       JPanel taskTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
       JPanel taskButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
       JPanel checkListAddPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
       
       JButton modifyTaskButton = new JButton("수정"); //카테고리 수정버튼
       JButton removeTaskButton = new JButton("삭제"); //카테고리 삭제버튼
       JButton checkListAddButton = new JButton("항목 추가"); //카테고리 세부사항 추가버튼
       
       JTextField checkListTitleInput = new JTextField("항목 이름");
       
       modifyTaskButton.addActionListener(new ActionListener() { //수정버튼 클릭 이벤트 핸들러
          public void actionPerformed(ActionEvent e) {
               modifyTask(modifyTaskButton);
           }
       });
       
       removeTaskButton.addActionListener(new ActionListener() { //삭제버튼 클릭 이벤트 핸들러
          public void actionPerformed(ActionEvent e) {
             if(ToDoList.isModifying && modifyTaskButton.getText().equals("수정완료"))
              {
                ToDoList.isModifying = false;
              }
               removeTask(taskPanel);
               checkLists.clear();
               Container cp = taskPanel.getParent();
               cp.remove(taskPanel);
               cp.repaint();
               isRemoving = true;
           }
       });
       
       checkListAddButton.addActionListener(new ActionListener() { //카테고리 세부사항 추가시 이벤트 핸들러
           public void actionPerformed(ActionEvent e)
           {
              if(ToDoList.isModifying)
              {
                 JOptionPane.showMessageDialog(taskPanel, "먼저 수정을 완료해 주세요.", "오류 메시지", JOptionPane.WARNING_MESSAGE);
                 return;
              }
              
              String checkListScoreInput = JOptionPane.showInputDialog(taskPanel, checkListTitleInput, "점수");
              String checkListTitle = checkListTitleInput.getText();
              if(checkListScoreInput != null)
              {
                 checkListScoreInput = checkListScoreInput.strip();
                 checkListTitle = checkListTitle.strip();
                 try {
                    if(checkListTitle.equals(""))
                     {
                       JOptionPane.showMessageDialog(taskPanel, "이름을 입력해 주세요.", "오류 메시지", JOptionPane.WARNING_MESSAGE);
                     }
                    else if(checkListScoreInput.equals(""))
                    {
                       JOptionPane.showMessageDialog(taskPanel, "점수를 입력해 주세요.", "오류 메시지", JOptionPane.WARNING_MESSAGE);
                    }
                    else if(Integer.parseInt(checkListScoreInput) <= 0 || (Task.totalScore - Integer.parseInt(checkListScoreInput)) < 0)
                    {
                       JOptionPane.showMessageDialog(taskPanel, "1보다 크고 총 점수가 100점 이하가 되는 값을 입력해 주세요.", "오류 메시지", JOptionPane.WARNING_MESSAGE);
                    }
                    else
                    {
                       checkLists.add(new CheckList(checkListTitle, Integer.parseInt(checkListScoreInput)));
                       Task.totalScore -= Integer.parseInt(checkListScoreInput);
                       updateCheckLists(taskCenterPanel, checkListAddPanel);
                    }
                 }
                 catch(NumberFormatException e1)
                 {
                    JOptionPane.showMessageDialog(taskPanel, "1보다 크고 총 점수가 100점 이하가 되는 값을 입력해 주세요.", "오류 메시지", JOptionPane.WARNING_MESSAGE);
                 }
              }
              
               checkListTitleInput.setText("항목 이름");
           }
       });
       
       taskPanel.add(taskTopPanel, BorderLayout.NORTH);
       taskPanel.add(taskCenterPanel, BorderLayout.CENTER);
       
       taskTopPanel.add(taskTextPanel, BorderLayout.CENTER);
       taskTopPanel.add(taskButtonPanel, BorderLayout.EAST);
       
       taskTextPanel.add(titleTextField);
       
       taskButtonPanel.add(modifyTaskButton);
       taskButtonPanel.add(removeTaskButton);
       
       taskCenterPanel.add(checkListAddPanel);
       
       checkListAddPanel.add(checkListAddButton);
    }
    
    private void modifyTask(JButton button)
    {
       if(button.getText().equals("수정"))
        {
          if(ToDoList.isModifying)
           {
              JOptionPane.showMessageDialog(taskPanel, "먼저 수정을 완료해 주세요.", "오류 메시지", JOptionPane.WARNING_MESSAGE);
              return;
           }
          
          titleTextField.setEditable(true);
            button.setText("수정완료");
            ToDoList.isModifying = true;
        }
        else
        {
           if(titleTextField.getText().strip().equals(""))
           {
              JOptionPane.showMessageDialog(taskPanel, "제목을 입력해 주세요.", "오류 메시지", JOptionPane.WARNING_MESSAGE);
              titleTextField.setText(getTitle());
           }
           
           setTitle(titleTextField.getText().strip());
           titleTextField.setEditable(false);
            button.setText("수정");
            ToDoList.isModifying = false;
        }
    }
    
    private void removeTask(JPanel panel)
   {
       for(int i = 0; i<checkLists.size(); i++)
       {
          //각 체크리스트의 점수 총 점수에 더하기
          checkLists.get(i).removeCheckList(checkLists.get(i).getPanel());
       }
       panel.removeAll();
       panel.revalidate();
       panel.repaint();
       //아예 삭제
   }
    
    private void updateCheckLists(JPanel panel, JPanel subPanel)
   {
       panel.removeAll();
       panel.add(subPanel);
       for(int i = 0; i<checkLists.size(); i++)
       {
          if(checkLists.get(i).isRemoving)
          {
             checkLists.remove(i);
             i--;
             continue;
          }
          panel.add(checkLists.get(i).getPanel());
       }
       panel.revalidate();
       panel.repaint();
     //System.out.println(checkLists.size() + "개");
   }
    
    //Setters
    public void setTitle(String title)
    {
       this.title = title;
       titleTextField.setText(title);
    }
    
    // Getters
    public JPanel getPanel()
    {
       return taskPanel;
    }
    public String getTitle() {
        return title;
    }
}

class CheckList
{
   private JPanel checkListPanel;
    private String description;
    private JCheckBox checkBox;
    private int score;
    private JTextField scoreTextField;
    public boolean isRemoving = false;

    public CheckList(String description, int score) { //생성자
        this.description = description;
        this.score = score;
        checkBox = new JCheckBox(description);
        checkBox.setPreferredSize(new Dimension(150, 30));
        scoreTextField = new JTextField(Integer.toString(score), 5);
        scoreTextField.setHorizontalAlignment(JLabel.RIGHT);
        scoreTextField.setEditable(false);
        JLabel scoreLabel = new JLabel("점");
        
        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
               if(checkBox.isSelected())
               {
                  ToDoList.updateTotalScore(getScore());
               }
               else
               {
                  ToDoList.updateTotalScore(-getScore());
               }
            }
        });
        
        checkListPanel = new JPanel(new BorderLayout());
        checkListPanel.setPreferredSize(new Dimension(420, 40));
        
        JPanel checkListTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JPanel checkListButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        JButton modifyCheckListButton = new JButton("수정"); //세부사항 수정버튼
       JButton removeCheckListButton = new JButton("삭제"); //세부사항 삭제버튼
       
       modifyCheckListButton.addActionListener(new ActionListener() { //세부사항 수정 이벤트 핸들러
          public void actionPerformed(ActionEvent e) {
               modifyCheckList(modifyCheckListButton);
           }
       });
       
       removeCheckListButton.addActionListener(new ActionListener() { // 세부사항 삭제 이벤트 핸들러
          public void actionPerformed(ActionEvent e) {
             if(ToDoList.isModifying && modifyCheckListButton.getText().equals("수정완료"))
              {
                ToDoList.isModifying = false;
              }
               removeCheckList(checkListPanel);
               Container cp = checkListPanel.getParent();
               cp.remove(checkListPanel);
               cp.repaint();
               isRemoving = true;
           }
       });
      
       checkListPanel.add(checkListTextPanel, BorderLayout.CENTER);
       checkListPanel.add(checkListButtonPanel, BorderLayout.EAST);
       
       checkListTextPanel.add(checkBox);
       checkListTextPanel.add(scoreTextField);
       checkListTextPanel.add(scoreLabel);
       
       checkListButtonPanel.add(modifyCheckListButton);
       checkListButtonPanel.add(removeCheckListButton);
    }
    
    private void modifyCheckList(JButton button)
    {
       if(button.getText().equals("수정"))
        {
          if(ToDoList.isModifying)
           {
              JOptionPane.showMessageDialog(checkListPanel, "먼저 수정을 완료해 주세요.", "오류 메시지", JOptionPane.WARNING_MESSAGE);
              return;
           }
          
          String checkListTitle = JOptionPane.showInputDialog(checkListPanel, "편집", getDescription());
          if(checkListTitle != null)
          {
             checkListTitle = checkListTitle.strip();
             if(checkListTitle.equals(""))
             {
                JOptionPane.showMessageDialog(checkListPanel, "이름을 입력해 주세요.", "오류 메시지", JOptionPane.WARNING_MESSAGE);
             }
             else
             {
                setDescription(checkListTitle);
                scoreTextField.setEditable(true);
                button.setText("수정완료");
                ToDoList.isModifying = true;
             }
          }
        }
       else
       {
          try
          {
             if(scoreTextField.getText().strip().equals(""))
             {
                JOptionPane.showMessageDialog(checkListPanel, "점수를 입력해 주세요.", "오류 메시지", JOptionPane.WARNING_MESSAGE);
                scoreTextField.setText(Integer.toString(getScore()));
             }
             else if(Integer.parseInt(scoreTextField.getText().strip()) <= 0 || (Task.totalScore + getScore() - Integer.parseInt(scoreTextField.getText().strip())) < 0)
             {
                JOptionPane.showMessageDialog(checkListPanel, "1보다 크고 총 점수가 100점 이하가 되는 값을 입력해 주세요.", "오류 메시지", JOptionPane.WARNING_MESSAGE);
                scoreTextField.setText(Integer.toString(getScore()));
             }
          }
          catch(NumberFormatException e1)
           {
              JOptionPane.showMessageDialog(checkListPanel, "1보다 크고 총 점수가 100점 이하가 되는 값을 입력해 주세요.", "오류 메시지", JOptionPane.WARNING_MESSAGE);
              scoreTextField.setText(Integer.toString(getScore()));
           }
          
          if(checkBox.isSelected())
           {
              ToDoList.updateTotalScore(-getScore());
           }
          Task.totalScore += getScore();
         setScore(Integer.parseInt(scoreTextField.getText().strip()));
         scoreTextField.setText(Integer.toString(getScore()));
         Task.totalScore -= getScore();
         if(checkBox.isSelected())
           {
              ToDoList.updateTotalScore(getScore());
           }
         scoreTextField.setEditable(false);
         button.setText("수정");
         ToDoList.isModifying = false;
       }
    }
    
    public void removeCheckList(JPanel panel)
   {
       if(checkBox.isSelected())
       {
          ToDoList.updateTotalScore(-getScore());
       }
       Task.totalScore += getScore();
       setScore(0);
       
       panel.removeAll();
       panel.revalidate();
       panel.repaint();
       //아예 삭제
   }
    
    //Setters
    public void setDescription(String description)
    {
       this.description = description;
       checkBox.setText(description);
    }
    public void setScore(int score)
    {
       this.score = score;
    }

    // Getters
    public JPanel getPanel()
    {
       return checkListPanel;
    }
    public JCheckBox getCheckBox()
    {
       return checkBox;
    }
    public String getDescription() {
        return description;
    }

    public int getScore() {
        return score;
    }
}

public class ToDoList extends JFrame
{
   public ArrayList<Task> tasks;
   public static int totalGainScore = 0;
   public static JLabel totalScoreLabel;
   public static boolean isModifying = false;
   private JScrollPane taskListScrollPane;
      
   public ToDoList()
   {
      tasks = new ArrayList<>();
      Border blackline;
       blackline = BorderFactory.createLineBorder(Color.black);
         
      setTitle("도파민 디톡스");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      Container cp = getContentPane(); // 컨텐트팬 생성
       cp.setLayout(new BorderLayout(0, 30));
         
       JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); // 상단 패널
       topPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
       JPanel centerPanel = new JPanel(new BorderLayout()); // 중앙 패널
       centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
       JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 0)); // 하단 패널
       bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
       
       TitledBorder taskListTitle;
       taskListTitle = BorderFactory.createTitledBorder(blackline, "할 일 목록");
       JPanel taskListPanel = new JPanel(new GridLayout(0, 2, 30, 20));
       taskListScrollPane = new JScrollPane(taskListPanel);
       taskListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
       taskListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
       taskListPanel.setBorder(taskListTitle);
       
       //점수표시
       TitledBorder totalScoreLabelTitle;
       totalScoreLabelTitle = BorderFactory.createTitledBorder(blackline, "오늘의 점수");
       totalScoreLabelTitle.setTitleJustification(TitledBorder.CENTER);
       totalScoreLabel = new JLabel(totalGainScore + " 점");
       totalScoreLabel.setHorizontalAlignment(JLabel.CENTER);
       totalScoreLabel.setPreferredSize(new Dimension(150, 75));
       totalScoreLabel.setBorder(totalScoreLabelTitle);
       
       JButton todoAddButton = new JButton("할 일 추가");
       JButton clearAllButton = new JButton("전체 삭제");
       todoAddButton.setPreferredSize(new Dimension(100, 50));
       clearAllButton.setPreferredSize(new Dimension(100, 50));
     
       clearAllButton.addActionListener(new ActionListener() {
    	   //전체 삭제버튼 이벤트 핸들러
          public void actionPerformed(ActionEvent e) {
               tasks.clear();
               totalGainScore = 0;
               Task.totalScore = 100;
               isModifying = false;
               updateTasks(taskListPanel);
           }
       });
      
       todoAddButton.addActionListener(new ActionListener() {
    	   //할 일 카테고리 추가버튼 이벤트 핸들러
           public void actionPerformed(ActionEvent e)
           {
              if(ToDoList.isModifying)
              {
                 JOptionPane.showMessageDialog(cp, "먼저 수정을 완료해 주세요.", "오류 메시지", JOptionPane.WARNING_MESSAGE);
                 return;
              }
              
               String todoTitle = JOptionPane.showInputDialog(cp, "할 일 추가");
               if(todoTitle != null)
               {
                  todoTitle = todoTitle.strip();
                  if(todoTitle.equals(""))
                  {
                     JOptionPane.showMessageDialog(cp, "제목을 입력해 주세요.", "오류 메시지", JOptionPane.WARNING_MESSAGE);
                  }
                  else
                  {
                     tasks.add(new Task(todoTitle));
                     updateTasks(taskListPanel);
                    }
               }
           }
       });
       
       //상단 패널에 총 점수 표시
       topPanel.add(totalScoreLabel);
       
//       centerPanel.add(taskListPanel);
       centerPanel.add(taskListScrollPane, BorderLayout.CENTER);
       
       //하단 패널에 버튼들 추가
       bottomPanel.add(todoAddButton);
       bottomPanel.add(clearAllButton);
         
       //컨텐트팬에 패널 추가
       cp.add(topPanel, BorderLayout.NORTH);
       cp.add(centerPanel, BorderLayout.CENTER);
       cp.add(bottomPanel, BorderLayout.SOUTH);
         
       setResizable(true);
       setSize(1120, 630);
       setLocationRelativeTo(null);
       setVisible(true);
   }
   
   private void updateTasks(JPanel panel)
   {
       panel.removeAll();
       for(int i = 0; i<tasks.size(); i++)
       {
          if(tasks.get(i).isRemoving)
          {
             tasks.remove(i);
             i--;
             continue;
          }
          panel.add(tasks.get(i).getPanel());
       }
       panel.revalidate();
       panel.repaint();
   }
   
   public static void updateTotalScore(int score)
   {
      totalGainScore += score;
      totalScoreLabel.setText(totalGainScore + " 점");
   }
   
   public static void main(String[] args)
   {
      ToDoList frame = new ToDoList();
      Thread musicThread = new Thread(new BackgroundMusic("C:\\Users\\USER\\piano\\piano.wav")); // 음악 파일 경로 지정
      musicThread.start();
   }
}