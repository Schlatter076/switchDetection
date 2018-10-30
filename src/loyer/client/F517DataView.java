package loyer.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PiePlot;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import loyer.dataBase.F517Data;
import loyer.dataBase.F517Tools;
import loyer.dataBase.RecordData;
import loyer.dataBase.RecordTools;
import loyer.exception.InputStreamCloseFail;
import loyer.exception.NoSuchPort;
import loyer.exception.NotASerialPort;
import loyer.exception.PortInUse;
import loyer.exception.ReadDataFromSerialFail;
import loyer.exception.SerialPortParamFail;
import loyer.exception.TooManyListeners;
import loyer.serial.SerialPortTools;
import javax.swing.ScrollPaneConstants;
import javax.swing.JToggleButton;

public class F517DataView {

  private String pwd = null;
  private JFrame frame;
  private JMenuBar menuBar;
  private JButton exitButt;
  private JButton resultButt;
  private JButton toolsButt;
  private JLabel timeLabel;
  private Timer timer1;
  private Timer timer2;
  private JProgressBar progressBar;
  private int progressValue = 0;
  private JButton helpButt;
  private JPanel dataPanel;
  private int okCount = 0;
  private int ngCount = 0;
  private int sumCount = 0;
  private int timeCount = 0;
  private MyPieChart myPieChart;
  private ChartPanel chartPanel;
  private PiePlot piePlot;
  private JTextField sumField;
  private JTextField okField;
  private JTextField ngField;
  private JTextField timeField;
  private JLabel sumLabel;
  private JLabel okLabel;
  private JLabel ngLabel;
  private JLabel tLabel;
  private JButton statuButt;
  private JScrollPane scrollPane;
  private JTable table;
  private JLabel lblpks;
  private JTextField typeField;
  private JLabel typeLabel;
  private JLabel statuLabel;
  /**保存屏幕尺寸,宽x高*/
  private int[] size = new int[2];
  private JCheckBox nayin;
  private JCheckBox spotTest;
  private JTextField text_1;
  private JTextField text_2;
  private JTextField text_3;
  private JTextField text_4;
  private JTextField text_5;
  private JTextField text_6;
  private JTextField text_7;
  private JTextField text_8;
  private JTextField text_9;
  private JTextField text_10;
  private JTextField text_11;
  private MyTableCellRenderer myRenderer;
  
  private ArrayList<String> portList = SerialPortTools.findPort();
  private static final byte FIRST_TEXT = (byte) 0xf3;
  private static final byte SECOND_TEXT = (byte) 0xf4;
  private static final byte END_TEXT = 0x0a;
  private static final int BUFFER_SIZE = 11;
  private static byte[] bytes = new byte[BUFFER_SIZE];
  private static byte data = 0;
  private static int rxCounter = 0;
  private static boolean hasData = false;
  private SerialPort COM1;
  private SerialPort COM2;
  private JToggleButton com1Butt;
  private JToggleButton com2Butt;
  
  
  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          F517DataView window = new F517DataView();
          window.frame.setVisible(true);
          window.initPort();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public F517DataView() {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frame = new JFrame();
    frame.getContentPane().setBackground(new Color(245, 245, 245));
    //窗口添加关闭事件
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        close();
      }
    });
    frame.setTitle("F517副驾四向开关测试系统");
    Toolkit tk = Toolkit.getDefaultToolkit();
    //size[0] = tk.getScreenSize().width;
    //size[1] = tk.getScreenSize().height;
    final int WIDTH = tk.getScreenSize().width;
    final int HEIGHT = tk.getScreenSize().height;
    
    //设置窗口大小
    //frame.setBounds(0, 0, size[0], size[1] - 50);
    frame.setBounds(0, 0, WIDTH, HEIGHT - 50);
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frame.setResizable(false);   //窗口大小不可改
    //设置窗口背景透明
    //((JPanel) frame.getContentPane()).setOpaque(false);
    //更换窗口咖啡图标
    frame.setIconImage(tk.getImage(frame.getClass().getResource("/Kyokuto.png")));
    //frame.setUndecorated(true);  //去掉窗口装饰
    menuBar = new JMenuBar();
    frame.setJMenuBar(menuBar);
    
    exitButt = new JButton("退出系统(EXIT)");
    exitButt.setOpaque(false);
    exitButt.setBackground(new Color(245, 245, 245));
    exitButt.setForeground(Color.BLACK);
    exitButt.setFont(new Font("宋体", Font.PLAIN, 14));
    menuBar.add(exitButt);
    
    //给退出按钮添加事件
    exitButt.addActionListener(event -> close());
    
    resultButt = new JButton("查看测试结果(Result)");
    resultButt.setOpaque(false);
    resultButt.setBackground(new Color(245, 245, 245));
    resultButt.setForeground(Color.BLACK);
    resultButt.setFont(new Font("宋体", Font.PLAIN, 14));
    menuBar.add(resultButt);
    //添加事件
    resultButt.addActionListener(event -> JOptionPane.showMessageDialog(null, resultButt.getText()));
    
    toolsButt = new JButton("调试工具(Tools)");
    toolsButt.setOpaque(false);
    toolsButt.setBackground(new Color(245, 245, 245));
    toolsButt.setForeground(Color.BLACK);
    toolsButt.setFont(new Font("宋体", Font.PLAIN, 14));
    menuBar.add(toolsButt);
    //打开串口调试工具
    toolsButt.addActionListener(event -> {
      if(COM1 != null) {
        SerialPortTools.closePort(COM1);
        COM1 = null;
        com1Butt.setSelected(false);
      }
      if(COM2 != null) {
        SerialPortTools.closePort(COM2);
        COM2 = null;
        com2Butt.setSelected(false);
      }
      UsartTools.getUsartTools();
    });
    
    helpButt = new JButton("帮助(H)");
    helpButt.setOpaque(false);
    helpButt.setBackground(new Color(245, 245, 245));
    helpButt.setForeground(Color.BLACK);
    helpButt.setFont(new Font("宋体", Font.PLAIN, 14));
    menuBar.add(helpButt);
    
    com1Butt = new JToggleButton("COM1");
    com1Butt.addActionListener(e -> {
      if(COM1 == null) {  //如果串口1被关闭了
        initCOM1();
      }
      else
        com1Butt.setSelected(true);
    });
    com1Butt.setBackground(new Color(245, 245, 245));
    com1Butt.setFont(new Font("宋体", Font.PLAIN, 14));
    menuBar.add(com1Butt);
    
    com2Butt = new JToggleButton("COM2");
    com2Butt.addActionListener(e -> {
      if(COM2 == null) {
        initCOM2();
      }
      else
        com2Butt.setSelected(true);
    });
    com2Butt.setBackground(new Color(245, 245, 245));
    com2Butt.setFont(new Font("宋体", Font.PLAIN, 14));
    menuBar.add(com2Butt);
    //打开帮助页面
    helpButt.addActionListener(event -> HelpPanel.getHelpPanel());
    frame.getContentPane().setLayout(new BorderLayout(0, 0));
    
    timeLabel = new JLabel("yyyy-MM-dd   HH:mm:ss     ");
    timeLabel.setForeground(Color.BLACK);
    timeLabel.setFont(new Font("宋体", Font.PLAIN, 16));
    timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    frame.getContentPane().add(timeLabel, BorderLayout.SOUTH);
    
    progressBar = new JProgressBar();
    progressBar.setOpaque(false);
    progressBar.setForeground(new Color(50, 205, 50));
    frame.getContentPane().add(progressBar, BorderLayout.NORTH);
    
    dataPanel = new JPanel();
    dataPanel.setBackground(new Color(245, 245, 245));
    frame.getContentPane().add(dataPanel, BorderLayout.CENTER);
    //饼图显示
    myPieChart = new MyPieChart(okCount, ngCount);
    chartPanel = myPieChart.getChartPanel();
    chartPanel.setOpaque(false);
    //chartPanel.setBounds(size[0]*2/3, size[1]/8, size[0]/3 - 10, size[1]/2);
    chartPanel.setBounds(WIDTH*2/3, HEIGHT/8, WIDTH/3 - 10, HEIGHT/2);
    dataPanel.add(chartPanel);
    setPieChart(10, 5);
    dataPanel.setLayout(null);
    //测试结果显示框
    sumField = new JTextField();
    //sumField.setBounds(size[0]/30, 32, size[0]/10, 50);
    sumField.setBounds(WIDTH/30, 35, WIDTH/10, 50);
    sumField.setEditable(false);
    sumField.setForeground(new Color(0, 0, 0));
    sumField.setHorizontalAlignment(SwingConstants.CENTER);
    sumField.setFont(new Font("Times New Roman", Font.PLAIN, 50));
    sumField.setBackground(new Color(245, 245, 245));
    dataPanel.add(sumField);
    sumField.setColumns(10);
    
    sumLabel = new JLabel("测试总数(PCS)：");
    //sumLabel.setBounds(size[0]/30, 10, size[0]/10, 15);
    sumLabel.setBounds(WIDTH/30, 15, WIDTH/10, 15);
    sumLabel.setForeground(new Color(0, 0, 0));
    sumLabel.setFont(new Font("等线", Font.BOLD, 16));
    dataPanel.add(sumLabel);
    
    okField = new JTextField();
    //okField.setBounds(size[0]*5/30, 32, size[0]/10, 50);
    okField.setBounds(WIDTH*5/30, 35, WIDTH/10, 50);
    okField.setHorizontalAlignment(SwingConstants.CENTER);
    okField.setForeground(new Color(50, 205, 50));
    okField.setFont(new Font("Times New Roman", Font.PLAIN, 50));
    okField.setEditable(false);
    okField.setColumns(10);
    okField.setBackground(new Color(245, 245, 245));
    dataPanel.add(okField);
    
    okLabel = new JLabel("OK：");
    //okLabel.setBounds(size[0]*5/30, 10, size[0]/10, 15);
    okLabel.setBounds(WIDTH*5/30, 15, WIDTH/10, 15);
    okLabel.setForeground(new Color(0, 0, 0));
    okLabel.setFont(new Font("等线", Font.BOLD, 16));
    dataPanel.add(okLabel);
    
    ngField = new JTextField();
    //ngField.setBounds(size[0]*9/30, 32, size[0]/10, 50);
    ngField.setBounds(WIDTH*9/30, 35, WIDTH/10, 50);
    ngField.setHorizontalAlignment(SwingConstants.CENTER);
    ngField.setForeground(new Color(255, 0, 0));
    ngField.setFont(new Font("Times New Roman", Font.PLAIN, 50));
    ngField.setEditable(false);
    ngField.setColumns(10);
    ngField.setBackground(new Color(245, 245, 245));
    dataPanel.add(ngField);
    
    ngLabel = new JLabel("NG：");
    //ngLabel.setBounds(size[0]*9/30, 10, size[0]/10, 15);
    ngLabel.setBounds(WIDTH*9/30, 15, WIDTH/10, 15);
    ngLabel.setForeground(new Color(0, 0, 0));
    ngLabel.setFont(new Font("等线", Font.BOLD, 16));
    dataPanel.add(ngLabel);
    
    timeField = new JTextField();
    //timeField.setBounds(size[0]*13/30, 32, size[0]/10, 50);
    timeField.setBounds(WIDTH*13/30, 35, WIDTH/10, 50);
    timeField.setHorizontalAlignment(SwingConstants.CENTER);
    timeField.setForeground(new Color(128, 0, 128));
    timeField.setFont(new Font("Times New Roman", Font.PLAIN, 50));
    timeField.setEditable(false);
    timeField.setColumns(10);
    timeField.setBackground(new Color(245, 245, 245));
    dataPanel.add(timeField);
    
    tLabel = new JLabel("测试时间(S)：");
    //tLabel.setBounds(size[0]*13/30, 10, size[0]/10, 15);
    tLabel.setBounds(WIDTH*13/30, 15, WIDTH/10, 15);
    tLabel.setForeground(new Color(0, 0, 0));
    tLabel.setFont(new Font("等线", Font.BOLD, 16));
    dataPanel.add(tLabel);
    
    typeField = new JTextField();
    //typeField.setBounds(size[0]/30, size[1]*5/8+75, size[0]*19/60, 60);
    typeField.setBounds(WIDTH/30, HEIGHT*5/8+75, WIDTH*19/60, 60);
    typeField.setText("F517副驾四向");
    typeField.setHorizontalAlignment(SwingConstants.LEFT);
    typeField.setForeground(new Color(0, 0, 139));
    typeField.setFont(new Font("楷体", Font.BOLD, 50));
    typeField.setEditable(false);
    typeField.setColumns(10);
    typeField.setBackground(new Color(245, 245, 245));
    dataPanel.add(typeField);
    
    typeLabel = new JLabel("当前机种：");
    //typeLabel.setBounds(size[0]/30, size[1]*5/8+50, size[0]*19/60, 15);
    typeLabel.setBounds(WIDTH/30, HEIGHT*5/8+50, WIDTH*19/60, 15);
    typeLabel.setForeground(new Color(0, 0, 0));
    typeLabel.setFont(new Font("等线", Font.BOLD, 16));
    dataPanel.add(typeLabel);
    
    statuButt = new JButton("STOP");
    statuButt.addActionListener(e -> {
      if(statuButt.getText().equals("STOP")) {
        statuButt.setText("RUN");
        timer2.start();
      }
      else {
        statuButt.setText("STOP");
        timer2.stop();
        progressBar.setValue(0);
      }
      
    });
    //statuButt.setBounds(size[0]*22/60, size[1]*5/8+75, size[0]*10/60, 60);
    statuButt.setBounds(WIDTH*22/60, HEIGHT*5/8+75, WIDTH*10/60, 60);
    statuButt.setFont(new Font("宋体", Font.BOLD | Font.ITALIC, 60));
    statuButt.setForeground(new Color(0, 0, 0));
    statuButt.setBackground(new Color(255, 255, 0));
    dataPanel.add(statuButt);
    
    statuLabel = new JLabel("运行状态/测试结果：");
    //statuLabel.setBounds(size[0]*22/60, size[1]*5/8+50, size[0]*10/60, 15);
    statuLabel.setBounds(WIDTH*22/60, HEIGHT*5/8+50, WIDTH*10/60, 15);
    statuLabel.setForeground(new Color(0, 0, 0));
    statuLabel.setFont(new Font("等线", Font.BOLD, 16));
    dataPanel.add(statuLabel);
    
    //测试表格显示
    table = completedTable(getTestTable());
    scrollPane = new JScrollPane(table);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    //scrollPane.setBounds(size[0]/30, size[1]/8, size[0]/2, size[1]/2);
    scrollPane.setBounds(WIDTH/30, HEIGHT/8, WIDTH/2, HEIGHT/2);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);
    dataPanel.add(scrollPane);
    
    lblpks = new JLabel("*版权所有：广州番禺旭东阪田电子有限公司(PKS)");
    //lblpks.setBounds(size[0]/30, size[1]*5/8 + 5, size[0]/2, 15);
    lblpks.setBounds(WIDTH/30, HEIGHT*5/8 + 5, WIDTH/2, 15);
    lblpks.setHorizontalAlignment(SwingConstants.LEFT);
    lblpks.setForeground(Color.GRAY);
    lblpks.setFont(new Font("楷体", Font.PLAIN, 12));
    dataPanel.add(lblpks);
    
    nayin = new JCheckBox("捺印");
    nayin.addActionListener(e -> {
      // 由于要触发事件，复选框状态定会改变，故而多加了判断
      JPasswordField pw = new JPasswordField();
      pw.setEchoChar('*');
      JOptionPane.showMessageDialog(null, pw, "请输入捺印密码", JOptionPane.PLAIN_MESSAGE);
      char[] pass = pw.getPassword();
      if (pass.length > 0 && pass.length <= 6) {
        if (String.valueOf(pass).equals("NY2018")) {
          if (nayin.isSelected()) {
            nayin.setSelected(true);
          } else
            nayin.setSelected(false);
        } else {
          JOptionPane.showMessageDialog(null, "密码错误！");
          if (nayin.isSelected()) {
            nayin.setSelected(false);
          } else
            nayin.setSelected(true);
        }
      } else {
        JOptionPane.showMessageDialog(null, "密码长度为6位！");
        if (nayin.isSelected()) {
          nayin.setSelected(false);
        } else
          nayin.setSelected(true);
      }
    });
    nayin.setSelected(true);
    nayin.setBackground(new Color(245, 245, 245));
    nayin.setForeground(new Color(0, 0, 255));
    nayin.setFont(new Font("宋体", Font.PLAIN, 20));
    //nayin.setBounds(size[0]*2/3 + 5, size[1]*5/8+75, 100, 40);
    nayin.setBounds(WIDTH*2/3 + 5, HEIGHT*5/8+75, 100, 40);
    dataPanel.add(nayin);
    
    spotTest = new JCheckBox("点测");
    spotTest.setForeground(new Color(0, 0, 255));
    spotTest.setFont(new Font("宋体", Font.PLAIN, 20));
    spotTest.setBackground(new Color(245, 245, 245));
    //spotTest.setBounds(size[0]*2/3 + 105, size[1]*5/8+75, 100, 40);
    spotTest.setBounds(WIDTH*2/3 + 105, HEIGHT*5/8+75, 100, 40);
    dataPanel.add(spotTest);
    
    text_1 = new JTextField();
    text_1.setBackground(new Color(245, 245, 245));
    text_1.setText("0xF3");
    text_1.setEditable(false);
    text_1.setFont(new Font("宋体", Font.PLAIN, 14));
    text_1.setForeground(new Color(0, 0, 0));
    text_1.setHorizontalAlignment(SwingConstants.CENTER);
    //text_1.setBounds(size[0]*17/30, size[1]/8, size[0]/15, size[1]*2/64);
    text_1.setBounds(WIDTH*17/30, HEIGHT/8, WIDTH/15, HEIGHT*2/64);
    dataPanel.add(text_1);
    text_1.setColumns(10);
    
    text_2 = new JTextField();
    text_2.setBackground(new Color(245, 245, 245));
    text_2.setText("0xF4");
    text_2.setHorizontalAlignment(SwingConstants.CENTER);
    text_2.setForeground(Color.BLACK);
    text_2.setFont(new Font("宋体", Font.PLAIN, 14));
    text_2.setEditable(false);
    text_2.setColumns(10);
    //text_2.setBounds(size[0]*17/30, size[1]*11/64, size[0]/15, size[1]*2/64);
    text_2.setBounds(WIDTH*17/30, HEIGHT*11/64, WIDTH/15, HEIGHT*2/64);
    dataPanel.add(text_2);
    
    text_3 = new JTextField();
    text_3.setBackground(new Color(245, 245, 245));
    text_3.setText("0x00");
    text_3.setHorizontalAlignment(SwingConstants.CENTER);
    text_3.setForeground(Color.BLACK);
    text_3.setFont(new Font("宋体", Font.PLAIN, 14));
    text_3.setEditable(false);
    text_3.setColumns(10);
    //text_3.setBounds(size[0]*17/30, size[1]*14/64, size[0]/15, size[1]*2/64);
    text_3.setBounds(WIDTH*17/30, HEIGHT*14/64, WIDTH/15, HEIGHT*2/64);
    dataPanel.add(text_3);
    
    text_4 = new JTextField();
    text_4.setBackground(new Color(245, 245, 245));
    text_4.setText("0x00");
    text_4.setHorizontalAlignment(SwingConstants.CENTER);
    text_4.setForeground(Color.BLACK);
    text_4.setFont(new Font("宋体", Font.PLAIN, 14));
    text_4.setEditable(false);
    text_4.setColumns(10);
    //text_4.setBounds(size[0]*17/30, size[1]*17/64, size[0]/15, size[1]*2/64);
    text_4.setBounds(WIDTH*17/30, HEIGHT*17/64, WIDTH/15, HEIGHT*2/64);
    dataPanel.add(text_4);
    
    text_5 = new JTextField();
    text_5.setBackground(new Color(245, 245, 245));
    text_5.setText("0x00");
    text_5.setHorizontalAlignment(SwingConstants.CENTER);
    text_5.setForeground(Color.BLACK);
    text_5.setFont(new Font("宋体", Font.PLAIN, 14));
    text_5.setEditable(false);
    text_5.setColumns(10);
    //text_5.setBounds(size[0]*17/30, size[1]*20/64, size[0]/15, size[1]*2/64);
    text_5.setBounds(WIDTH*17/30, HEIGHT*20/64, WIDTH/15, HEIGHT*2/64);
    dataPanel.add(text_5);
    
    text_6 = new JTextField();
    text_6.setBackground(new Color(245, 245, 245));
    text_6.setText("0x00");
    text_6.setHorizontalAlignment(SwingConstants.CENTER);
    text_6.setForeground(Color.BLACK);
    text_6.setFont(new Font("宋体", Font.PLAIN, 14));
    text_6.setEditable(false);
    text_6.setColumns(10);
    //text_6.setBounds(size[0]*17/30, size[1]*23/64, size[0]/15, size[1]*2/64);
    text_6.setBounds(WIDTH*17/30, HEIGHT*23/64, WIDTH/15, HEIGHT*2/64);
    dataPanel.add(text_6);
    
    text_7 = new JTextField();
    text_7.setBackground(new Color(245, 245, 245));
    text_7.setText("0x00");
    text_7.setHorizontalAlignment(SwingConstants.CENTER);
    text_7.setForeground(Color.BLACK);
    text_7.setFont(new Font("宋体", Font.PLAIN, 14));
    text_7.setEditable(false);
    text_7.setColumns(10);
    //text_7.setBounds(size[0]*17/30, size[1]*26/64, size[0]/15, size[1]*2/64);
    text_7.setBounds(WIDTH*17/30, HEIGHT*26/64, WIDTH/15, HEIGHT*2/64);
    dataPanel.add(text_7);
    
    text_8 = new JTextField();
    text_8.setBackground(new Color(245, 245, 245));
    text_8.setText("0x00");
    text_8.setHorizontalAlignment(SwingConstants.CENTER);
    text_8.setForeground(Color.BLACK);
    text_8.setFont(new Font("宋体", Font.PLAIN, 14));
    text_8.setEditable(false);
    text_8.setColumns(10);
    //text_8.setBounds(size[0]*17/30, size[1]*29/64, size[0]/15, size[1]*2/64);
    text_8.setBounds(WIDTH*17/30, HEIGHT*29/64, WIDTH/15, HEIGHT*2/64);
    dataPanel.add(text_8);
    
    text_9 = new JTextField();
    text_9.setBackground(new Color(245, 245, 245));
    text_9.setText("0x00");
    text_9.setHorizontalAlignment(SwingConstants.CENTER);
    text_9.setForeground(Color.BLACK);
    text_9.setFont(new Font("宋体", Font.PLAIN, 14));
    text_9.setEditable(false);
    text_9.setColumns(10);
    //text_9.setBounds(size[0]*17/30, size[1]*32/64, size[0]/15, size[1]*2/64);
    text_9.setBounds(WIDTH*17/30, HEIGHT*32/64, WIDTH/15, HEIGHT*2/64);
    dataPanel.add(text_9);
    
    text_10 = new JTextField();
    text_10.setBackground(new Color(245, 245, 245));
    text_10.setText("0x00");
    text_10.setHorizontalAlignment(SwingConstants.CENTER);
    text_10.setForeground(Color.BLACK);
    text_10.setFont(new Font("宋体", Font.PLAIN, 14));
    text_10.setEditable(false);
    text_10.setColumns(10);
    //text_10.setBounds(size[0]*17/30, size[1]*35/64, size[0]/15, size[1]*2/64);
    text_10.setBounds(WIDTH*17/30, HEIGHT*35/64, WIDTH/15, HEIGHT*2/64);
    dataPanel.add(text_10);
    
    text_11 = new JTextField();
    text_11.setBackground(new Color(245, 245, 245));
    text_11.setText("0x0A");
    text_11.setHorizontalAlignment(SwingConstants.CENTER);
    text_11.setForeground(Color.BLACK);
    text_11.setFont(new Font("宋体", Font.PLAIN, 14));
    text_11.setEditable(false);
    text_11.setColumns(10);
    //text_11.setBounds(size[0]*17/30, size[1]*38/64, size[0]/15, size[1]*2/64);
    text_11.setBounds(WIDTH*17/30, HEIGHT*38/64, WIDTH/15, HEIGHT*2/64);
    dataPanel.add(text_11);
    
    JLabel picture = new JLabel("广州番禺旭东阪田电子有限公司(PKS)");
    picture.setFont(new Font("楷体", Font.BOLD, 20));
    ImageIcon img = new ImageIcon(this.getClass().getResource("/Kyokuto.png"));
    picture.setIcon(img);
    //picture.setBounds(size[0]*2/3, 35, size[0]/3 - 10, 50);
    picture.setBounds(WIDTH*2/3, 35, WIDTH/3 - 10, 50);
    dataPanel.add(picture);
    
    initCountAndPieChart();
    
    //实时刷新时间
    timer1 = new Timer(1000, event -> {
      Date date = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss     ");
      timeLabel.setText(sdf.format(date));
    });
    timer1.start();
    timer2 = new Timer(20, event -> {
      progressBar.setValue(progressValue);
      progressValue += 1;
      if(progressValue > 100) progressValue = 0;
      timeCount += 20;
      timeField.setText(calculate(timeCount));
      flushText();
    });
    //timer2.start();
  }
  /**
   * 创建JTable方法
   * 
   * @return
   */
  public JTable getTestTable() {
    Vector<Object> rowNum = null, colNum = null;
    // 创建列对象
    colNum = new Vector<>();
    colNum.add("");
    colNum.add("步骤");
    colNum.add("测试项目");
    colNum.add("上限");
    colNum.add("下限");
    colNum.add("测试值");
    colNum.add("单位");
    colNum.add("测试结果");
    colNum.add("备注");
    colNum.add("Column1");
    colNum.add("Column2");
    colNum.add("Column3");
    colNum.add("Column4");

    // 创建行对象
    rowNum = new Vector<>();
    List<F517Data> tableList = F517Tools.getAllByDB(); // 从数据库中获取f517表的内容
    for (Iterator<F517Data> i = tableList.iterator(); i.hasNext();) {
      F517Data rd = i.next();
      Vector<String> vt = new Vector<>();
      vt.add("");
      vt.add(rd.getSteps());
      vt.add(rd.getTestItem());
      vt.add(rd.getUpperLimit());
      vt.add(rd.getLowerLimit());
      vt.add(rd.getTestValue());
      vt.add(rd.getUint());
      vt.add(rd.getTestResult());

      rowNum.add(vt);
    }

    JTable table = new JTable(rowNum, colNum);

    return table;
  }

  /**
   * 提供设置JTable方法
   * 
   * @param table
   * @return
   */
  public JTable completedTable(JTable table) {

    //table.setOpaque(false); //设置表透明
    table.setBackground(new Color(245, 245, 245));  //设置表格背景颜色
    JTableHeader jTableHeader = table.getTableHeader(); // 获取表头
    // 设置表头名称字体样式
    jTableHeader.setFont(new Font("", Font.PLAIN, 14));
    // 设置表头名称字体颜色
    jTableHeader.setForeground(Color.BLACK);
    jTableHeader.setBackground(new Color(245, 245, 245));  //设置表头背景颜色
    // 表头不可拖动
    jTableHeader.setReorderingAllowed(false);
    // 列大小不可改变
    jTableHeader.setResizingAllowed(false);

    // 设置列宽
    TableColumn colNull = table.getColumnModel().getColumn(0);
    TableColumn colTestitem = table.getColumnModel().getColumn(2);
    TableColumn colMaxvalue = table.getColumnModel().getColumn(3);
    TableColumn colMinvalue = table.getColumnModel().getColumn(4);
    TableColumn colTestvalue = table.getColumnModel().getColumn(5);
    TableColumn colTestResult = table.getColumnModel().getColumn(7);
    colNull.setPreferredWidth(20);
    colTestitem.setPreferredWidth(150);
    colMaxvalue.setPreferredWidth(120);
    colMinvalue.setPreferredWidth(120);
    colTestvalue.setPreferredWidth(120);
    colTestResult.setPreferredWidth(120);

    table.setEnabled(false); // 内容不可编辑
    DefaultTableCellRenderer r = new DefaultTableCellRenderer(); // 设置
    r.setHorizontalAlignment(JLabel.CENTER); // 单元格内容
    table.setDefaultRenderer(Object.class, r); // 居中显示

    table.setRowHeight(27); // 设置行高
    // 增加一行空白行
    // AbstractTableModel tableModel = (AbstractTableModel) table.getModel();
    DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
    tableModel.addRow(new Object[] { "*", "", "", "", "", "", "", "", "", "", "", "", "" });
    // table.setGridColor(Color.PINK); //设置网格颜色
    table.setForeground(Color.BLACK); // 设置文字颜色
    table.setFont(new Font("", Font.PLAIN, 14));
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);// 关闭表格列自动调整

    return table;
  }
  /**
   * table渲染色，测试结果为"PASS"则设为绿色，"NG"为红色
   */
  public void setTableCellRenderer() {
    if (myRenderer == null) {
      myRenderer = new MyTableCellRenderer();
      table.getColumnModel().getColumn(7).setCellRenderer(myRenderer);
    } else
      table.getColumnModel().getColumn(7).setCellRenderer(myRenderer);
  }
  /**
   * 刷新饼图的方法
   * 
   * @param ok
   * @param ng
   */
  public void setPieChart(int ok, int ng) {
    piePlot = myPieChart.getPiePlot();
    piePlot.setDataset(MyPieChart.getDataSet(ok, ng));
    piePlot.setSectionPaint("良品", new Color(0, 204, 51));
    piePlot.setSectionPaint("不良", Color.RED);
  }
  /**
   * 初始化饼图和测试数据
   */
  public void initCountAndPieChart() {
    RecordData rd = RecordTools.getDataByDate(LocalDate.now().toString());
    if(rd != null) {
      okCount = rd.getOk();
      ngCount = rd.getNg();
      sumCount = rd.getSum();
      timeCount = 0;
    }
    else {
      okCount = 0;
      ngCount = 0;
      sumCount = 0;
      timeCount = 0;
    }
    okField.setText(okCount + "");
    ngField.setText(ngCount + "");
    sumField.setText(sumCount + "");
    timeField.setText(timeCount + "");
    setPieChart(okCount, ngCount);
  }
  /**
   * 初始化串口1
   */
  public void initCOM1() {
    if(portList.contains("COM1") && COM1 == null) {
      try {
        COM1 = SerialPortTools.getPort(1);
      } catch (SerialPortParamFail | NotASerialPort | NoSuchPort | PortInUse e) {
        JOptionPane.showMessageDialog(null, "COM1:" + e.toString());
      }
      com1Butt.setSelected(true);
      try {
        SerialPortTools.add(COM1, arg0 -> {
          switch (arg0.getEventType()) {
          case SerialPortEvent.BI:  //10 通讯中断
          case SerialPortEvent.OE:  // 7 溢位（溢出）错误
          case SerialPortEvent.FE:  // 9 帧错误
          case SerialPortEvent.PE:  // 8 奇偶校验错误
          case SerialPortEvent.CD:  // 6 载波检测
          case SerialPortEvent.CTS:  // 3 清除待发送数据
          case SerialPortEvent.DSR:  // 4 待发送数据准备好了
          case SerialPortEvent.RI:  // 5 振铃指示
          case SerialPortEvent.OUTPUT_BUFFER_EMPTY:  // 2 输出缓冲区已清空
            JOptionPane.showMessageDialog(null, "COM1错误：" + arg0.toString());
            break;
          case SerialPortEvent.DATA_AVAILABLE: {
            try {
              data = SerialPortTools.read_byte(COM1);
            } catch (ReadDataFromSerialFail e) {
              JOptionPane.showMessageDialog(null, "COM1:" + e.toString());
            } catch (InputStreamCloseFail e) {
              JOptionPane.showMessageDialog(null, "COM1:" + e.toString());
            }
            bytes[rxCounter] = data;
            rxCounter++;
            switch(rxCounter) {
            case 1:
              if(data != FIRST_TEXT)  rxCounter = 0;
              break;
            case 2:
              if(data != SECOND_TEXT) rxCounter = 0;
              break;
            case 11:
              rxCounter = 0;
              if(data == END_TEXT)  hasData = true;
              break;
            default:break;
            }
          }
            break;
          }
        });
      } catch (TooManyListeners e) {
        JOptionPane.showMessageDialog(null, "COM1:" + e.toString());
      }
    }
    else {
      JOptionPane.showMessageDialog(null, "未发现串口1！");
      com1Butt.setSelected(false);
    }
  }
  /**
   * 初始化串口2
   */
  public void initCOM2() {
    if(portList.contains("COM2") && COM2 == null) {
      try {
        COM2 = SerialPortTools.getPort(2);
      } catch (SerialPortParamFail | NotASerialPort | NoSuchPort | PortInUse e) {
        JOptionPane.showMessageDialog(null, "COM2:" + e.toString());
      }
      com2Butt.setSelected(true);
      try {
        SerialPortTools.add(COM1, arg0 -> {
          switch (arg0.getEventType()) {
          case SerialPortEvent.BI:  //10 通讯中断
          case SerialPortEvent.OE:  // 7 溢位（溢出）错误
          case SerialPortEvent.FE:  // 9 帧错误
          case SerialPortEvent.PE:  // 8 奇偶校验错误
          case SerialPortEvent.CD:  // 6 载波检测
          case SerialPortEvent.CTS:  // 3 清除待发送数据
          case SerialPortEvent.DSR:  // 4 待发送数据准备好了
          case SerialPortEvent.RI:  // 5 振铃指示
          case SerialPortEvent.OUTPUT_BUFFER_EMPTY:  // 2 输出缓冲区已清空
            JOptionPane.showMessageDialog(null, "COM2错误：" + arg0.toString());
            break;
          case SerialPortEvent.DATA_AVAILABLE: {
            //有数据到达
          }
            break;
          }
        });
      } catch (TooManyListeners e) {
        JOptionPane.showMessageDialog(null, "COM2:" + e.toString());
      }
    }
    else {
      JOptionPane.showMessageDialog(null, "未发现串口2！");
      com2Butt.setSelected(false);
    }
  }
  /**
   * 初始化串口
   */
  public void initPort() {
    initCOM1();
    initCOM2();
    /*
    ArrayList<String> portList = SerialPortTools.findPort();
    if(portList.contains("COM1") && COM1 == null) {
      try {
        COM1 = SerialPortTools.getPort(1);
      } catch (SerialPortParamFail | NotASerialPort | NoSuchPort | PortInUse e) {
        JOptionPane.showMessageDialog(null, "COM1:" + e.toString());
      }
      com1Butt.setSelected(true);
      try {
        SerialPortTools.add(COM1, arg0 -> {
          switch (arg0.getEventType()) {
          case SerialPortEvent.BI:  //10 通讯中断
          case SerialPortEvent.OE:  // 7 溢位（溢出）错误
          case SerialPortEvent.FE:  // 9 帧错误
          case SerialPortEvent.PE:  // 8 奇偶校验错误
          case SerialPortEvent.CD:  // 6 载波检测
          case SerialPortEvent.CTS:  // 3 清除待发送数据
          case SerialPortEvent.DSR:  // 4 待发送数据准备好了
          case SerialPortEvent.RI:  // 5 振铃指示
          case SerialPortEvent.OUTPUT_BUFFER_EMPTY:  // 2 输出缓冲区已清空
            JOptionPane.showMessageDialog(null, "COM1错误：" + arg0.toString());
            break;
          case SerialPortEvent.DATA_AVAILABLE: {
            try {
              data = SerialPortTools.read_byte(COM1);
            } catch (ReadDataFromSerialFail e) {
              JOptionPane.showMessageDialog(null, "COM1:" + e.toString());
            } catch (InputStreamCloseFail e) {
              JOptionPane.showMessageDialog(null, "COM1:" + e.toString());
            }
            bytes[rxCounter] = data;
            rxCounter++;
            switch(rxCounter) {
            case 1:
              if(data != FIRST_TEXT)  rxCounter = 0;
              break;
            case 2:
              if(data != SECOND_TEXT) rxCounter = 0;
              break;
            case 11:
              rxCounter = 0;
              if(data == END_TEXT)  hasData = true;
              break;
            default:break;
            }
          }
            break;
          }
        });
      } catch (TooManyListeners e) {
        JOptionPane.showMessageDialog(null, "COM1:" + e.toString());
      }
    }
    else {
      JOptionPane.showMessageDialog(null, "未发现串口1！");
      com1Butt.setSelected(false);
    }
    if(portList.contains("COM2") && COM2 == null) {
      try {
        COM2 = SerialPortTools.getPort(2);
      } catch (SerialPortParamFail | NotASerialPort | NoSuchPort | PortInUse e) {
        JOptionPane.showMessageDialog(null, "COM2:" + e.toString());
      }
      com2Butt.setSelected(true);
      try {
        SerialPortTools.add(COM1, arg0 -> {
          switch (arg0.getEventType()) {
          case SerialPortEvent.BI:  //10 通讯中断
          case SerialPortEvent.OE:  // 7 溢位（溢出）错误
          case SerialPortEvent.FE:  // 9 帧错误
          case SerialPortEvent.PE:  // 8 奇偶校验错误
          case SerialPortEvent.CD:  // 6 载波检测
          case SerialPortEvent.CTS:  // 3 清除待发送数据
          case SerialPortEvent.DSR:  // 4 待发送数据准备好了
          case SerialPortEvent.RI:  // 5 振铃指示
          case SerialPortEvent.OUTPUT_BUFFER_EMPTY:  // 2 输出缓冲区已清空
            JOptionPane.showMessageDialog(null, "COM2错误：" + arg0.toString());
            break;
          case SerialPortEvent.DATA_AVAILABLE: {
            //有数据到达
          }
            break;
          }
        });
      } catch (TooManyListeners e) {
        JOptionPane.showMessageDialog(null, "COM2:" + e.toString());
      }
    }
    else {
      JOptionPane.showMessageDialog(null, "未发现串口2！");
      com2Butt.setSelected(false);
    }  
    //*/    
  }
  /**
   * 计算测试时间显示
   * @param num
   */
  public String calculate(int num) {
    
    int thousand =  (num / 1000);
    int hundred =  (num%1000/100);
    int ten = (num%100/10);
    
    return "" + thousand + "." + hundred + ten;
  }
  /**
   * 刷新文本显示
   */
  public void flushText() {
    text_3.setText(String.format("0x%02X", bytes[2]));
    text_4.setText(String.format("0x%02X", bytes[3]));
    text_5.setText(String.format("0x%02X", bytes[4]));
    text_6.setText(String.format("0x%02X", bytes[5]));
    text_7.setText(String.format("0x%02X", bytes[6]));
    text_8.setText(String.format("0x%02X", bytes[7]));
    text_9.setText(String.format("0x%02X", bytes[8]));
    text_10.setText(String.format("0x%02X", bytes[9]));
  }
  /**
   * 退出系统方法
   */
  private void close() {
    int num = JOptionPane.showConfirmDialog(null, "确认退出系统？", "提示", JOptionPane.YES_NO_OPTION);
    //如果确认键按下
    if(num == JOptionPane.YES_OPTION) {
      System.exit(0);
    }
  }
}
