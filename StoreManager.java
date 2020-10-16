// --== CS400 File Header Information ==--
import java.util.Scanner;

/**
 * Represents the class that runs the entire application. This class contains both front-end and
 * back-end methods
 * 
 * @author Anshul Yadav, Christopher Kennedy
 */
public class StoreManager {
  private RedBlackTree<Store> RBT = new RedBlackTree<>();
  int size = 0;

  /**
   *
   * @param args- String Array input
   */
  public static void main(String[] args) {
    StoreManager newStoreManager = new StoreManager();

    System.out.println(
        "Welcome to the Store Database." + "In This application you can do a bunch of stuff");
    newStoreManager.helper();
    Scanner scan = new Scanner(System.in);
    while (true) {
      System.out.println("Enter the capital letter corresponding to what you'd like to do:");
      String doThis = scan.nextLine().substring(0, 1);
      String toCheck = "ighctpunqIGHCTPUNQ"; // chars we're checking for
      while (!toCheck.contains(doThis)) {
        System.out.println("Sorry! It looks like we didn't recognize the character you entered."
            + "\nTry entering I, R, G, H, C, T, P, U, N. If you'd like to see what these do,"
            + "\npress H. Enter the capital letter corresponding to what you'd like to do:");
        doThis = scan.next().substring(0, 1);
      }
      if (doThis.equalsIgnoreCase("Q")) {
        System.out
            .println("Are you sure that you want to quit the application.\n" + "Enter yes/no");
        String quit = scan.next();
        while (!quit.equalsIgnoreCase("yes") && !quit.equalsIgnoreCase("no")) {
          System.out.println("Invalid Input. Please Enter yes/no");
          quit = scan.next();
        }
        if (quit.equalsIgnoreCase("yes")) {
          System.out.println("Thank you for using Store Database");
          break;
        }
        System.out.println("Thank you for continuing to use Store Database");
      }
      newStoreManager.userInput(scan, doThis);
    }
    scan.close();
  }

  /**
   *
   * @param s-      scanner to get the user's input for helper methods.
   * @param string- contains the user char input that prompts an action.
   */
  private void userInput(Scanner s, String string) {
    char input = string.charAt(0);
    Store store; // a local reference for various variables
    switch (input) {
      case 'i':
      case 'I':
        store = askUserForStore(s);
        RBT.insert(store);
        System.out.println("This " + store.storeName + " store was added successfully");
        size++;
        break;

      // case 'r':
      // case 'R':
      // Store store = HelperOutStore(s);
      // RBT.remove(store);
      case 'g':// get method
      case 'G':
        store = getStore(HelperOutStore(s));
        if (store == null) {
          System.out.println("We could not find the store you were looking for."
              + "\nPlease enter the Parameters correctly");
        } else {
          System.out.println("Type of Store: " + store.getStoreType() + "\nNumber of Store: "
              + store.getOutStore().getStoreNumber() + "\nStore Number: "
              + store.getOutStore().getStoreNumber() + "\nStore's Address: "
              + store.getOutStore().getAddress() + "\nStore's Email: "
              + store.getOutStore().getStoreEmail() + "\nNumber of Employees: "
              + store.getInStore().getEmployeeNumber() + "\nName Of Manager: "
              + store.getInStore().getManagerName() + "\nTotalCost of Store: "
              + store.getInStore().getTotalCost() + "\nYearly Profit $: "
              + store.getInStore().getYearlyProfit());
        }
        break;
      case 'h':
      case 'H':
        helper();
        break;
      case 'c':
      case 'C':
        store = getStore(HelperOutStore(s));
        if (store == null) {
          System.out.println("We could not find the store you were looking for."
              + "\nPlease enter the Parameters correctly");
        } else {
          System.out.println("The following store DOES exist in our database");
        }
        break;
      case 't':
      case 'T':
        // RBT.printNode(RBT.root);
        break;
      case 'p':
      case 'P':
        System.out.println(RBT.toString());
        break;
      case 'u':
      case 'U':
        System.out
            .println("Please Enter the Integer corresponding to the update you want to make:");
        System.out.println(
            "Enter 1 to update ManagerName: " + "\nEnter 2 to update the Number of Employees"
                + "\nEnter 3 to update Total Cost of a store"
                + "\nEnter 4 to update Yearly Profits of a store");
        int updateInt = s.nextInt();
        while (updateInt < 1 || updateInt > 4) {
          System.out.println("Enter a correct Integer." + "\nEnter 1 to update ManagerName: "
              + "\nEnter 2 to update the Number of Employees"
              + "\nEnter 3 to update Total Cost of a store"
              + "\nEnter 4 to update Yearly Profits of a store");
          updateInt = s.nextInt();
        }
        System.out.println("Enter the following details for the specific store to update");
        store = getStore(HelperOutStore(s));
        System.out.println("Enter the new details:");
        String update = s.next();
        update(updateInt, store, update);
        System.out.println("The value of the store has been updated.");
        break;
      case 'n':
      case 'N':
        System.out.println("Their are " + size + " stores in our Database.");
        break;
    }
  }

  /**
   * This method takes in 3 parameters to update a Store's InStore. These changes do not make a
   * difference to the overall position of the store in the tree.
   * 
   * @param updateInt-dictates the variable to be updated. 1-ManagerName, 2-NumberOfEmployees
   *                           3-TotalCost, 4-YearlyProfits
   * @param initialStore-      refers to the store that needs to be updated
   * @param update-            The value to be updated
   * @return The updated store
   */
  public Store update(int updateInt, Store initialStore, String update) {
    Store finalStore;// store with updates made
    if (updateInt == 1) {
      initialStore.getInStore().setManagerName(update);
    } else if (updateInt == 2) {
      initialStore.getInStore().setEmployeeNumber(Integer.parseInt(update));
    } else if (updateInt == 3) {
      initialStore.getInStore().setTotalCost(Integer.parseInt(update));
    } else if (updateInt == 4) {
      initialStore.getInStore().setYearlyProfit(Double.parseDouble(update));
    }
    finalStore = updateHelper(initialStore, RBT.root);
    return finalStore;
  }

  /**
   *
   * @param updateStore- Store containing updated values
   * @param subtree-     Tree where the node containing old Store is to be updated
   * @return - The updated Store
   */
  private Store updateHelper(Store updateStore, RedBlackTree.Node<Store> subtree) {
    int compare = updateStore.compareTo(subtree.data);
    if (compare == 0) {
      subtree.data = updateStore;
      return subtree.data;
    } else if (compare < 0) {
      if (subtree.leftChild == null) {
        return null;
      } else
        return updateHelper(updateStore, subtree.leftChild);
    } else {
      if (subtree.rightChild == null) {
        return null;
      } else
        return updateHelper(updateStore, subtree.rightChild);
    }

  }

  /**
   * method for printing out the various commands the user can perform.
   */
  public void helper() {
    System.out.println("Start by entering the letter corresponding to the following commands"
        + "\nEnter i to insert a new Store:" + "\nEnter r to remove a store:"
        + "\nEnter g to get a particular Store:" + "\nEnter h to display this menu:"
        + "\nEnter c to check for a particular store:"
        + "\nEnter t to look at the tree representation of stores:"
        + "\nEnter q to quit the application:"
        + "\nEnter p to get inline representation of the stores in the database:"
        + "\nEnter u to update the values of the store:"
        + "\nEnter n to to get the number of Stores stored:");
  }

  /**
   * This method calls the recursive helper method getStoreHelper() that returns the Store stored at
   * the node provided by the StoreValue
   * 
   * @param StoreValue- integer value to be given back
   * @return Store- stored at the input, null otherwise
   */
  public Store getStore(int StoreValue) {
    return getStoreHelper(StoreValue, RBT.root);
  }

  /**
   * Recursive helper method that returns the value of node (a Store), given its StoreValue and a
   * reference to the subtree
   * 
   * @param StoreValue- int used to locate the Store in the subtree
   * @param subtree-    subtree where the store is looked for
   * @return Store- stored at the position, null otherwise
   */
  private Store getStoreHelper(int StoreValue, RedBlackTree.Node<Store> subtree) {
    int compare = StoreValue - subtree.data.storeValue();
    if (compare == 0) {
      return subtree.data;
    } else if (compare < 0) {
      if (subtree.leftChild == null) {
        return null;
      } else
        return getStoreHelper(StoreValue, subtree.leftChild);
    } else {
      if (subtree.rightChild == null) {
        return null;
      } else
        return getStoreHelper(StoreValue, subtree.rightChild);
    }
  }

  /**
   * This method asks user for 3 inputs that is used to evaluate a store's StoreValue. This value is
   * used to input stores into the RedBlackTree
   * 
   * @param s- represents the scanner used to take user's input
   * @return StoreValue of this store
   */
  private int HelperOutStore(Scanner s) {
    System.out.println("Enter the email of the Store: ");
    String email = s.nextLine();

    System.out.println("Enter the address of the Store: ");
    String address = s.nextLine();

    System.out.println("Enter the Store Number of the Store: ");
    int number = s.nextInt();
    s.nextLine();

    return email.trim().length() + address.trim().length() + number;
  }

  @Override
  /**
   * Returns the String representation of the StoreManager object
   */
  public String toString() {
    return RBT.toString();
  }

  /**
   * This method asks the user for various inputs that are used to create stores for the
   * RedBlackTree
   * 
   * @param s- represents the scanner that is used to that inputs.
   * @return The store object created
   */
  private Store askUserForStore(Scanner s) {
    System.out.println("Enter the name of this store (i.e. Target, Applebees, BestBuy, etc.):");
    String storeName = s.nextLine().trim();

    System.out.println("Enter the address of this " + storeName + ":");
    String address = s.nextLine().trim();

    System.out.println("Enter the email corresponding to this " + storeName + ":");
    String email = s.next().trim();
    s.nextLine();

    System.out.println("Enter the StoreNumber corresponding to this " + storeName + ":");
    int storeNumber = s.nextInt();
    while (storeNumber < 0) {
      System.out.println("Please Enter a positive Store Number");
      storeNumber = s.nextInt();
    }

    s.nextLine();

    System.out.println("Enter the Name of the Manager corresponding to this " + storeName + ":");
    String manager = s.nextLine().trim();

    System.out
        .println("Enter the amount of inventory to the nearest dollar of this " + storeName + ":");
    int totalInventory = s.nextInt();
    while (totalInventory < 0) {
      System.out.println("Please Enter a positive Total Cost");
      totalInventory = s.nextInt();
    }

    System.out.println("Enter the Number Of Employees in this " + storeName + ":");
    int employees = s.nextInt();
    s.nextLine();
    while (employees < 0) {
      System.out.println("Please Enter a positive Employee Number");
      employees = s.nextInt();
      s.nextLine();
    }

    System.out.println("Enter the yearly Profit in $ of this " + storeName + ":");
    System.out.println("Enter your amount as a decimal number. For example, 800.0.");
    double profit = Double.parseDouble(s.next().trim());

    s.nextLine();

    Store.InStore inToRet = new Store.InStore(manager, totalInventory, employees, profit);
    Store.OutStore outToRet = new Store.OutStore(address, email, storeNumber);
    Store toRet = new Store(inToRet, outToRet, storeName);
    return toRet;
  }

  

}
