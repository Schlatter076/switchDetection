package test;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
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
  static byte[] trans = {(byte) 0xf3, (byte) 0xf4, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x0a};
  static int i = 0;
  /**电压表返回数据起始字节1*/
  private static final byte VFIRST_TEXT = (byte) 0xaa;
  /**电压表返回数据起始字节2*/
  private static final byte VSECOND_TEXT = 0x55;
  /**电压表返回数据校验字节*/
  private static int sum = 0;
  /**电压表返回数据校验和高位*/
  private static byte sumH = 0;
  /**电压表返回数据校验和低位*/
  private static byte sumL = 0;
  /**电压表接收缓冲区大小*/
  private static final int VBUFFER_SIZE = 20;
  /**电压表接收字节数组*/
  private static byte[] vBytes = new byte[VBUFFER_SIZE];
  /**电压表接收单个字节*/
  private static byte vData = 0;
  /**电压表返回指令部分总长度*/
  private static int vLEN = 0;
  /**电压表接收字节计数器*/
  private static int vRxCounter = 0;
  /**电压表接收完整数据包标志位*/
  private static boolean vHasData = false;
  /**电压表校验和累加临时数据*/
  private static int vTemp= 0;
  private static Timer timer2;

  public static void main(String[] args) throws SerialPortParamFail, NotASerialPort, NoSuchPort, PortInUse, SendToPortFail, OutputStreamCloseFail, TooManyListeners, InterruptedException {

    
    //usartTest();
    vol_meter();
    
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
        if(!hasData) {
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
          case BUFFER_SIZE:
            rxCounter = 0;
            if(data == END_TEXT)  hasData = true;
            break;
          default:break;
          }
        } 
      }
        break;
      }
    });
    timer1 = new Timer(500, (event) -> {
      
      trans[2] |= (1 << i);
      i++;
      if(i >= 9)  {
        i = 0;
        trans[2] = 0;
      }
      
      try {
        SerialPortTools.write(COM1, trans);
      } catch (SendToPortFail e) {
        e.printStackTrace();
      } catch (OutputStreamCloseFail e) {
        e.printStackTrace();
      }
      
      if(hasData) {
        hasData = false;
        String str = SerialPortTools.bytesToHex(bytes);
        System.out.println(str);
      }
    });
    timer1.start();
    //byte[] datas = {(byte) 0xf3, (byte) 0xf4, 0x00, 0x00, (byte) 0xff, 0x00, 0x00, 0x00, 0x00, 0x01, 0x0a};
    //SerialPortTools.write(COM1, datas);
    
    //Thread.sleep(500);
    //String str1 = "f3 f4 00 00 ff 00 00 00 00 01 0a";
    //SerialPortTools.write(COM1, SerialPortTools.toByteArray(str1));
    
    while(true);
  }
  
  public static void vol_meter() throws SerialPortParamFail, NotASerialPort, NoSuchPort, PortInUse, TooManyListeners {
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
        //有数据到达
        if(!vHasData) {
          try {
            vData = SerialPortTools.read_byte(COM1);
          } catch (ReadDataFromSerialFail | InputStreamCloseFail e) {
            JOptionPane.showMessageDialog(null, "COM1:" + e.toString());
          }
          vBytes[vRxCounter] = vData;
          vRxCounter++;
          switch(vRxCounter) {
          case 1 : 
            if(vData != VFIRST_TEXT) vRxCounter = 0;
            break;
          case 2 :
            if(vData != VSECOND_TEXT) vRxCounter = 0;
            break;
          case 3 :
            vLEN = vData;
            break;
          case VBUFFER_SIZE : {
            vRxCounter = 0;
            //校验结束字节，如果为真，则置位标志位
            for(int i = 0; i < vLEN; i++) {
              vTemp = vBytes[i + 2];
              if(vTemp < 0)  vTemp += 256;
              sum += vTemp;
            }
            sumL = (byte) (sum & 0xff);  //取校验和低位
            sumH = (byte) ((sum >> 8) & 0xff);  //取校验和高位
            sum = 0;
            if(sumL == vBytes[vLEN + 3] && sumH == vBytes[vLEN + 2]) {
              vHasData = true;  //如果校验位没问题，则表示收到完整的数据包
            }
          }
            break;
          default:break;
          }
        }
      }
        break;
      }
    });
    timer2 = new Timer(500, (event) -> {
      
      if(vHasData) {
        vHasData = false;
        String str = SerialPortTools.bytesToHex(vBytes);
        //vBytes = null;
        System.out.println(str);
      }
    });
    timer2.start();
    
    while(true);
  }
   
}
