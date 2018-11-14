package loyer.client;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import loyer.dataBase.ProductData;
import loyer.dataBase.ProductTools;
import loyer.dataBase.UserData;
import loyer.dataBase.UserTools;

public class SignIn {

  private JFrame frame;
  private JLabel pks;
  private JComboBox<String> productType;
  private JLabel productLabel;
  private UserData common;
  private UserData admin;
  private ArrayList<ProductData> products;
  private JComboBox<String> userField;
  private JLabel userLabel;
  private JPasswordField pwdField;
  private JLabel pwdLabel;
  private JButton logInButt;
  private JButton exitButt;
  private boolean hasLogIn = false;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          SignIn window = new SignIn();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public SignIn() {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    
    try {
      //将界面风格设置成和系统一置
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
      JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
    }//*/
    
    frame = new JFrame();
    frame.setTitle("登录");
    frame.setBounds(100, 100, 440, 355);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    ((JPanel) frame.getContentPane()).setOpaque(false);
    
    Toolkit tk = Toolkit.getDefaultToolkit();
    //全局添加键盘监听事件
    tk.addAWTEventListener(new AWTEventListener() {
      
      @Override
      public void eventDispatched(AWTEvent event) {
        if(!hasLogIn) {
        //当有键按下，且为ENTER键时，点击登录按钮
          if(((KeyEvent) event).getID() == KeyEvent.KEY_PRESSED && ((KeyEvent) event).getKeyChar() == KeyEvent.VK_ENTER) {
            logInButt.doClick();
          }
        }
      }
    }, AWTEvent.KEY_EVENT_MASK);
    //替换窗口的咖啡图标
    Image img = tk.getImage(frame.getClass().getResource("/Kyokuto.png"));
    frame.setIconImage(img);
    frame.getContentPane().setLayout(null);
    
    
    pks = new JLabel("*版权所有：广州番禺旭东阪田电子有限公司");
    pks.setBounds(0, 291, 402, 15);
    pks.setForeground(Color.GRAY);
    pks.setFont(new Font("楷体", Font.PLAIN, 12));
    pks.setHorizontalAlignment(SwingConstants.RIGHT);
    frame.getContentPane().add(pks);
    
    productType = new JComboBox<String>();
    productType.setFont(new Font("楷体", Font.BOLD, 18));
    productType.setBackground(new Color(240, 240, 240));
    //productType.setBackground(new Color(255, 255, 240));
    productType.setBounds(40, 44, 362, 35);
    productType.setEditable(true);
    frame.getContentPane().add(productType);
    //从数据库加载产品型号
    products = ProductTools.getTypeByDB();
    for(ProductData product : products) {
      productType.addItem(product.getName());
    }
    
    productLabel = new JLabel("产品型号:");
    productLabel.setFont(new Font("等线", Font.PLAIN, 14));
    productLabel.setBounds(40, 17, 94, 22);
    frame.getContentPane().add(productLabel);
    
    userField = new JComboBox<String>();
    userField.setFont(new Font("宋体", Font.PLAIN, 17));
    userField.setEditable(true);
    userField.setBackground(new Color(240, 240, 240));
    //userField.setBackground(new Color(255, 255, 240));
    userField.setBounds(40, 117, 362, 27);
    frame.getContentPane().add(userField);
    
    userLabel = new JLabel("用户名:");
    userLabel.setFont(new Font("等线", Font.PLAIN, 14));
    userLabel.setBounds(40, 89, 94, 22);
    frame.getContentPane().add(userLabel);
    
    pwdField = new JPasswordField();
    pwdField.setFont(new Font("宋体", Font.PLAIN, 17));
    pwdField.setEchoChar('*');
    pwdField.setBounds(40, 184, 362, 27);
    frame.getContentPane().add(pwdField);
    //从数据库加载用户名、密码
    common = UserTools.getUserByID(1);
    admin = UserTools.getUserByID(2);
    userField.addActionListener(event -> {
      if(userField.getEditor().getItem().equals(common.getUserName())) {
        pwdField.setText(common.getPassword());
      }
      else
        pwdField.setText("");
    });
    userField.addItem(common.getUserName());
    userField.addItem(admin.getUserName());
    
    pwdLabel = new JLabel("密码:");
    pwdLabel.setFont(new Font("等线", Font.PLAIN, 14));
    pwdLabel.setBounds(40, 155, 94, 22);
    frame.getContentPane().add(pwdLabel);
    
    logInButt = new JButton("用户登录");
    logInButt.setOpaque(false);
    logInButt.setForeground(Color.BLACK);
    logInButt.setFont(new Font("等线", Font.BOLD, 16));
    logInButt.setBackground(new Color(224, 255, 255));
    logInButt.setBounds(198, 231, 204, 40);
    frame.getContentPane().add(logInButt);
    //给登录按钮添加事件
    logInButt.addActionListener(event -> getSignInEvent());
    
    exitButt = new JButton("退出系统");
    exitButt.setOpaque(false);
    exitButt.setForeground(SystemColor.controlShadow);
    exitButt.setFont(new Font("等线", Font.PLAIN, 14));
    exitButt.setBackground(new Color(224, 255, 255));
    exitButt.setBounds(40, 231, 112, 40);
    frame.getContentPane().add(exitButt);
    //给推出按钮添加事件
    exitButt.addActionListener(event -> System.exit(0));
  }
  /**
   * 登录事件
   */
  private void getSignInEvent() {
    
    boolean isCommon = userField.getEditor().getItem().equals(common.getUserName());
    boolean isCommonPWD = String.valueOf(pwdField.getPassword()).equals(common.getPassword());
    boolean isAdmin = userField.getEditor().getItem().equals(admin.getUserName());
    boolean isAdminPWD = String.valueOf(pwdField.getPassword()).equals(admin.getPassword());
    //验证用户名和密码
    if((isCommon && isCommonPWD) || (isAdmin && isAdminPWD)) {
      String type = (String) productType.getEditor().getItem();  //获取产品型号
      if(type == null || type.length() < 2) {
        JOptionPane.showMessageDialog(null, "产品型号不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
      }
      //根据产品型号选择测试程序
      switch(type) {
      
      case "F517副驾四向" :
        //这里调用F517副驾四向测试程序
        hasLogIn = true; //登录成功
        frame.dispose(); //关闭登录界面
        
        break;
        
      default:break;
      }
    }
    else
      JOptionPane.showMessageDialog(null, "用户名或密码不正确！", "错误", JOptionPane.ERROR_MESSAGE);
  }
}
