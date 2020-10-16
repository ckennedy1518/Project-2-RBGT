// --== CS400 File Header Information ==--
// Name: Anshul Yadav
// Email: ayadav8@wisc.edu
// Team: HG
// TA: Na Li
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

/**
 * 
 * @param storeEmail
 */
public class Store implements Comparable<Store> {

  InStore inStore;
  OutStore outStore;
  String storeName;

  public Store(InStore inStore, OutStore outStore, String storeName) {
    this.inStore = inStore;
    this.outStore = outStore;
    this.storeName = storeName;
  }

  public InStore getInStore() {
    return inStore;
  }
  
  public String getStoreType() {
    return storeName;
  }

  public void setStoreType(String storeType) {
    this.storeName = storeType;
  }

  public void setInStore(InStore inStore) {
    this.inStore = inStore;
  }

  public OutStore getOutStore() {
    return outStore;
  }

  public void setOutStore(OutStore outStore) {
    this.outStore = outStore;
  }

  @Override
  public int compareTo(Store store) {
    int StoreValue = this.outStore.storeNumber - store.outStore.storeNumber
        + this.outStore.storeEmail.trim().length() - store.outStore.storeEmail.trim().length()
        + this.outStore.address.trim().length() - store.outStore.address.trim().length();
    return StoreValue;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Store) {
      return ((this.compareTo((Store) o) == 0));
    } else
      return false;
  }

  @Override
  public String toString() {
    return this.storeName + " " + this.outStore.storeNumber;
  }

  public int storeValue() {
    return this.outStore.storeNumber + this.outStore.address.length()
        + this.outStore.storeEmail.length();
  }

  public static class InStore {
    private String managerName;
    private int TotalCost;
    private int employeeNumber;
    private double yearlyProfit;

    public InStore(String managerName, int totalCost, int employeeNumber, double yearlyProfit) {
      this.managerName = managerName;
      this.TotalCost = totalCost;
      this.employeeNumber = employeeNumber;
      this.yearlyProfit = yearlyProfit;
    }

    public String getManagerName() {
      return managerName;
    }

    public void setManagerName(String managerName) {
      this.managerName = managerName;
    }

    public int getTotalCost() {
      return TotalCost;
    }

    public void setTotalCost(int totalCost) {
      TotalCost = totalCost;
    }

    public int getEmployeeNumber() {
      return employeeNumber;
    }

    public void setEmployeeNumber(int employeeNumber) {
      this.employeeNumber = employeeNumber;
    }

    public double getYearlyProfit() {
      return yearlyProfit;
    }

    public void setYearlyProfit(double yearlyProfit) {
      this.yearlyProfit = yearlyProfit;
    }
  }
  public static class OutStore {
    private int storeNumber;
    private String address;
    private String storeEmail;

    public OutStore(String address, String storeEmail, int storeNumber) {
      this.address = address;
      this.storeEmail = storeEmail;
      this.storeNumber = storeNumber;
    }

    public int getStoreNumber() {
      return storeNumber;
    }

    public String getAddress() {
      return address;
    }

    public String getStoreEmail() {
      return storeEmail;
    }

    public void setStoreNumber(int storeNumber) {
      this.storeNumber = storeNumber;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public void setStoreEmail(String storeEmail) {
      this.storeEmail = storeEmail;
    }
  }
}
