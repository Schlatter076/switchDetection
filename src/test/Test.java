package test;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import loyer.dataBase.RecordTools;
import loyer.exception.InputStreamCloseFail;
import loyer.exception.NoSuchPort;
import loyer.exception.NotASerialPort;
import loyer.exception.OutputStreamCloseFail;
import loyer.exception.PortInUse;
import loyer.exception.ReadDataFromSerialFail;
import loyer.exception.SendToPortFail;
import loyer.exception.SerialPortParamFail;
import loyer.exception.TooManyListeners;
import loyer.serial.SerialPortTools;

public class Test {
  
  private static final byte FIRST_TEXT = (byte) 0xf3;
  private static final byte SECOND_TEXT = (byte) 0xf4;
  private static final byte END_TEXT = 0x0a;
  private static final int BUFFER_SIZE = 11;
  private static byte[] bytes = new byte[BUFFER_SIZE];
  private static byte data = 0;
  private static int rxCounter = 0;
  private static Timer timer1;
  private static boolean hasData = false;

  public static void main(String[] args) throws SerialPortParamFail, NotASerialPort, NoSuchPort, PortInUse, SendToPortFail, OutputStreamCloseFail, TooManyListeners, InterruptedException {

    byte b = 1;
    b &= ~(1<<0);
    b |= (1<<1);
    System.out.println(b);
    System.out.println(String.format("0x%02x", (byte)-13));
    //String[] str = {"F517", "3", "2", "1", "20", LocalDate.now().toString()};
    //RecordTools.updateData(str);
    //String[] str = {"F517", "1", "0", "1", "20", LocalDate.now().toString()};
    //RecordTools.insertData(str);
    /*UserData common = UserTools.getUserByID(1);
    UserData admin = UserTools.getUserByID(2);
    System.out.println(common.getId() + "::" + common.getUserName() + "::" + common.getPassword());
    System.out.println(admin.getId() + "::" + admin.getUserName() + "::" + admin.getPassword());
    
    List<TestData> list = TestTools.getAllByDB();
    for(TestData data : list) {
      System.out.println(data.getTestItem() + "::" + data.getUint());
    }//*/
  }
  public static void usartTest() throws SendToPortFail, OutputStreamCloseFail, SerialPortParamFail, NotASerialPort, NoSuchPort, PortInUse, TooManyListeners, InterruptedException {
    ArrayList<String> portList = SerialPortTools.findPort();
    for(String port : portList) {
      System.out.println(port);
    }
    SerialPort COM1 = SerialPortTools.getPort(1);
    //SerialPort COM1 = SerialPortTools.getPort("COM1", 9600, 8, 1, 0);
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
          JOptionPane.showMessageDialog(null, e.toString());
        } catch (InputStreamCloseFail e) {
          JOptionPane.showMessageDialog(null, e.toString());
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
    timer1 = new Timer(500, (event) -> {
      if(hasData) {
        hasData = false;
        String str = SerialPortTools.bytesToHex(bytes);
        System.out.println(str);
      }
    });
    timer1.start();
    byte[] datas = {(byte) 0xf3, (byte) 0xf4, 0x00, 0x00, (byte) 0xff, 0x00, 0x00, 0x00, 0x00, 0x01, 0x0a};
    SerialPortTools.write(COM1, datas);
    
    Thread.sleep(500);
    String str1 = "f3 f4 00 00 ff 00 00 00 00 01 0a";
    SerialPortTools.write(COM1, SerialPortTools.toByteArray(str1));
    
    while(true);
  }
  
}
