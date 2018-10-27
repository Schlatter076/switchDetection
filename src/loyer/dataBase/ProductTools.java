package loyer.dataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class ProductTools {

  private ProductTools() {}  //不允许其他类创建本类实例
  
  /**
   * 从数据库获取产品型号
   * @return
   */
  public static ArrayList<ProductData> getTypeByDB() {
    ArrayList<ProductData> list = new ArrayList<>();
    String sql = "select * from product_type";
    ResultSet rs = DBHelper.search(sql, null);
    try {
      while(rs.next()) {
        String name = rs.getString(1);
        list.add(new ProductData(name));
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, "产品型号加载失败：" + e.getLocalizedMessage());
    } finally {
      DBHelper.close();
    }
    return list;
  }
}
