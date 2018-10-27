package loyer.dataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * 测试数据工具类
 * @author hw076
 *
 */
public class TestTools {

  private TestTools() {} //不允许其他类创建本类实例
  
  public static List<TestData> getAllByDB() {
    
    List<TestData> list = new ArrayList<>();
    String sql = "select * from f517";
    try {
      ResultSet rs = DBHelper.search(sql, null);
      while(rs.next()) {
        String steps = rs.getString(1);
        String testItem = rs.getString(2);
        String upperLimit = rs.getString(3);
        String lowerLimit = rs.getString(4);
        String testValue = rs.getString(5);
        String unit = rs.getString(6);
        String testResult = rs.getString(7);
        
        list.add(new TestData(steps, testItem, upperLimit, lowerLimit, testValue, unit, testResult));
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, "测试数据表读取失败：" + e.getLocalizedMessage());
    } finally {
      DBHelper.close();
    }
    return list;
    
  }
}
