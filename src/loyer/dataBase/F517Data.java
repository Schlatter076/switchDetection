package loyer.dataBase;

/**
 * 测试数据表f517实体
 * @author hw076
 *
 */
public class F517Data {

  private String steps;
  private String testItem;
  private String upperLimit;
  private String lowerLimit;
  private String testValue;
  private String uint;
  private String testResult;
  
  
  public F517Data() {
    super();
  }
  public F517Data(String steps, String testItem, String upperLimit, String lowerLimit, String testValue, String uint,
      String testResult) {
    super();
    this.steps = steps;
    this.testItem = testItem;
    this.upperLimit = upperLimit;
    this.lowerLimit = lowerLimit;
    this.testValue = testValue;
    this.uint = uint;
    this.testResult = testResult;
  }
  
  public String getSteps() {
    return steps;
  }
  public void setSteps(String steps) {
    this.steps = steps;
  }
  public String getTestItem() {
    return testItem;
  }
  public void setTestItem(String testItem) {
    this.testItem = testItem;
  }
  public String getUpperLimit() {
    return upperLimit;
  }
  public void setUpperLimit(String upperLimit) {
    this.upperLimit = upperLimit;
  }
  public String getLowerLimit() {
    return lowerLimit;
  }
  public void setLowerLimit(String lowerLimit) {
    this.lowerLimit = lowerLimit;
  }
  public String getTestValue() {
    return testValue;
  }
  public void setTestValue(String testValue) {
    this.testValue = testValue;
  }
  public String getUint() {
    return uint;
  }
  public void setUint(String uint) {
    this.uint = uint;
  }
  public String getTestResult() {
    return testResult;
  }
  public void setTestResult(String testResult) {
    this.testResult = testResult;
  }
  
}
