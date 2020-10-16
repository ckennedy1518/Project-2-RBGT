// --== CS400 File Header Information ==--
// Name: Christopher Kennedy
// Email: ckennedy22@wisc.edu
// Team: HG
// TA: Na Li
// Lecturer: Gary Dahl
// Notes to Grader: N/a

import java.util.LinkedList;

/**
 * Binary Search Tree implementation with a Node inner class for representing the nodes within a
 * binary search tree. You can use this class' insert method to build a binary search tree, and its
 * toString method to display the level order (breadth first) traversal of values in that tree.
 */
public class RedBlackTree<T extends Comparable<T>> {
  /**
   * This class represents a node holding a single value within a binary tree the parent, left, and
   * right child references are always be maintained.
   */
  protected static class Node<T> {
    public T data;
    public Node<T> parent; // null for root node
    public Node<T> leftChild;
    public Node<T> rightChild;
    public boolean isBlack;

    public Node(T data) {
      this.data = data;
      isBlack = false;
    }

    /**
     * @return true when this node has a parent and is the left child of that parent, otherwise
     *         return false
     */
    public boolean isLeftChild() {
      return parent != null && parent.leftChild == this;
    }

    /**
     * This method performs a level order traversal of the tree rooted at the current node. The
     * string representations of each data value within this tree are assembled into a comma
     * separated string within brackets (similar to many implementations of java.util.Collection).
     * 
     * @return string containing the values of this tree in level order
     */
    @Override
    public String toString() { // display subtree in order traversal
      String output = "[";
      LinkedList<Node<T>> q = new LinkedList<>();
      q.add(this);
      while (!q.isEmpty()) {
        Node<T> next = q.removeFirst();
        if (next.leftChild != null)
          q.add(next.leftChild);
        if (next.rightChild != null)
          q.add(next.rightChild);
        output += next.data.toString();
        if (!q.isEmpty())
          output += ", ";
      }
      return output + "]";
    }
  }

  protected Node<T> root; // reference to root node of tree, null when empty

  /**
   * Performs a naive insertion into a binary search tree: adding the input data value to a new node
   * in a leaf position within the tree. After this insertion, no attempt is made to restructure or
   * balance the tree. This tree will not hold null references, nor duplicate data values.
   * 
   * @param data to be added into this binary search tree
   * @throws NullPointerException     when the provided data argument is null
   * @throws IllegalArgumentException when the tree already contains data
   */
  public void insert(T data) throws NullPointerException, IllegalArgumentException {
    // null references cannot be stored within this tree
    if (data == null)
      throw new NullPointerException("This RedBlackTree cannot store null references.");
    Node<T> newNode = new Node<>(data);
    if (root == null) {
      root = newNode;
    } // add first node to an empty tree
    else {
      insertHelper(newNode, root); // recursively insert into subtree
    }
  }

  /**
   * This recursive helper method resolves the red child under red parent red black tree property
   * violations that are introduced by inserting new nodes into a red black tree.
   */
  private void enforceRBTreePropertiesAfterInsert(Node<T> n) {
    if (!root.isBlack) // root node should always be black
      root.isBlack = true;

    if (n.parent.isBlack) { // do nothing, coloring correct
    } else { // operations needed
      if (n.parent.parent != null) {// grandparent node exists
        Node<T> uncle = uncleHelper(n);
        if (uncle == null) {
          if ((n.isLeftChild() && n.parent.isLeftChild())
              || !n.isLeftChild() && !n.parent.isLeftChild()) {
            case2(n); // case two scenario with null uncle
          } else { // case three
            Node<T> nodeToCall = n.parent;
            rotate(n, n.parent);
            enforceRBTreePropertiesAfterInsert(nodeToCall); // recursively call, should be case 2
                                                            // now
          }
        } else {
          if (!n.parent.isBlack && !uncle.isBlack) {
            // case 1 violation, parent and parents sibling are red
            case1(n);
          } else if ((n.isLeftChild() && !uncle.isLeftChild())
              || (!n.isLeftChild() && uncle.isLeftChild())) {
            // case two violation, parent's sibling is opposite child
            case2(n);
          } else { // case three violation, parent's sibling black and child same side as sibling
            Node<T> nodeToCall = n.parent;
            rotate(n, n.parent);
            enforceRBTreePropertiesAfterInsert(nodeToCall); // recursively call, should be case 2
                                                            // now
          }
        }
      }
    }
  }

  /**
   * This private helper method completes teh actions for a case one scenario in an unballanced rbt.
   * 
   * @param n - the node case one will be performed on
   */
  private void case1(Node<T> n) {
    Node<T> uncle = uncleHelper(n);
    n.parent.isBlack = true;
    uncle.isBlack = true;
    n.parent.parent.isBlack = false;
    enforceRBTreePropertiesAfterInsert(n.parent); // resolve parent/GP node problems
  }

  /**
   * This private helper method completes the actions for a case two scenario in an unballanced rbt.
   * 
   * @param n - the node case two will be performed on
   */
  private void case2(Node<T> n) {
    n.parent.isBlack = true;
    n.parent.parent.isBlack = false;
    rotate(n.parent, n.parent.parent);
  }

  /**
   * This private helper method returns the node that is the parent's sibling.
   * 
   * @param n - the child node whose uncle will be returned
   * @return the child's parent's sibling
   */
  private Node<T> uncleHelper(Node<T> n) {
    if (n.parent.isLeftChild())
      return n.parent.parent.rightChild;
    else
      return n.parent.parent.leftChild;
  }

  /**
   * Recursive helper method to find the subtree with a null reference in the position that the
   * newNode should be inserted, and then extend this tree by the newNode in that position.
   * 
   * @param newNode is the new node that is being added to this tree
   * @param subtree is the reference to a node within this tree which the newNode should be inserted
   *                as a descenedent beneath
   * @throws IllegalArgumentException when the newNode and subtree contain equal data references (as
   *                                  defined by Comparable.compareTo())
   */
  private void insertHelper(Node<T> newNode, Node<T> subtree) {
    int compare = newNode.data.compareTo(subtree.data);
    // do not allow duplicate values to be stored within this tree
    if (compare == 0)
      throw new IllegalArgumentException("This RedBlackTree already contains that value.");
    // store newNode within left subtree of subtree
    else if (compare < 0) {
      if (subtree.leftChild == null) { // left subtree empty, add here
        subtree.leftChild = newNode;
        newNode.parent = subtree;
        enforceRBTreePropertiesAfterInsert(newNode); // ensure properties are correct
        // otherwise continue recursive search for location to insert
      } else
        insertHelper(newNode, subtree.leftChild);
    }
    // store newNode within the right subtree of subtree
    else {
      if (subtree.rightChild == null) { // right subtree empty, add here
        subtree.rightChild = newNode;
        newNode.parent = subtree;
        enforceRBTreePropertiesAfterInsert(newNode); // ensure properties are correct
        // otherwise continue recursive search for location to insert
      } else
        insertHelper(newNode, subtree.rightChild);
    }
  }

  /**
   * This method performs a level order traversal of the tree. The string representations of each
   * data value within this tree are assembled into a comma separated string within brackets
   * (similar to many implementations of java.util.Collection, like java.util.ArrayList, LinkedList,
   * etc).
   * 
   * @return string containing the values of this tree in level order
   */
  @Override
  public String toString() {
    return root.toString();
  }

  /**
   * Performs the rotation operation on the provided nodes within this BST. When the provided child
   * is a leftChild of the provided parent, this method will perform a right rotation (sometimes
   * called a left-right rotation). When the provided child is a rightChild of the provided parent,
   * this method will perform a left rotation (sometimes called a right-left rotation). When the
   * provided nodes are not related in one of these ways, this method will throw an
   * IllegalArgumentException.
   * 
   * @param child  is the node being rotated from child to parent position (between these two node
   *               arguments)
   * @param parent is the node being rotated from parent to child positi audrey wuz here on (between
   *               these two node arguments)
   * @throws IllegalArgumentException when the provided child and parent node references are not
   *                                  initially (pre-rotation) related that way
   */
  private void rotate(Node<T> child, Node<T> parent) throws IllegalArgumentException {
    if (child.parent.equals(parent) && child.isLeftChild()) { // right rotation
      Node<T> childsRightChild = child.rightChild;
      if (parent.equals(root)) { // parent is root node
        root = child;
        child.parent = null;
        child.rightChild = parent;
        parent.parent = child;
        parent.leftChild = childsRightChild;
        if (childsRightChild != null)
          childsRightChild.parent = parent;
      } else if (parent.isLeftChild()) { // parent is a left child
        parent.parent.leftChild = child;
        child.parent = parent.parent;
        child.rightChild = parent;
        parent.parent = child;
        parent.leftChild = childsRightChild;
        if (childsRightChild != null)
          childsRightChild.parent = parent;
      } else { // parent is a right child
        parent.parent.rightChild = child;
        child.parent = parent.parent;
        child.rightChild = parent;
        parent.parent = child;
        parent.leftChild = childsRightChild;
        if (childsRightChild != null)
          childsRightChild.parent = parent;
      }
    } else if (child.parent.equals(parent)) { // left rotation
      Node<T> childsLeftChild = child.leftChild;
      if (parent.equals(root)) {
        root = child;
        child.parent = null;
        child.leftChild = parent;
        parent.parent = child;
        parent.rightChild = childsLeftChild;
        if (childsLeftChild != null)
          childsLeftChild.parent = parent;
      }
      if (parent.isLeftChild()) {
        parent.parent.leftChild = child;
        child.parent = parent.parent;
        child.leftChild = parent;
        parent.parent = child;
        parent.rightChild = childsLeftChild;
        if (childsLeftChild != null)
          childsLeftChild.parent = parent;
      } else {
        parent.parent.rightChild = child;
        child.parent = parent.parent;
        child.leftChild = parent;
        parent.parent = child;
        parent.rightChild = childsLeftChild;
        if (childsLeftChild != null)
          childsLeftChild.parent = parent;
      }
    } else { // parent is not related to child
      throw new IllegalArgumentException();
    }
  }

  /**
   * This recursive helper method returns the "smallest" node in the subtree.
   * 
   * @param root - the root node of the subtree
   * @return - the left most node of the subtree
   */
  private Node<T> leftMostNodeOfSubtree(Node<T> root) {
    if (root.leftChild == null) // this is the smallest node
      return root;
    else // iterate through left
      return leftMostNodeOfSubtree(root.leftChild);
  }

  /**
   * This recursive method removes a node from the tree while maintaining RBT properties.
   * 
   * @param toRem - the node that is being removed.
   * @return - the node that is removed, or null if the node doesn't exist in the RBT.
   */
  public Node<T> remove(Node<T> toRem) {
    if (!contains(toRem)) // node exists in tree?
      return null;

    Node<T> toReturn = toRem;
    if ((toRem.leftChild != null) && (toRem.rightChild != null)) { // toRem has two children
      removeTwoChildren(toRem);
    } else if ((toRem.leftChild != null) && (toRem.rightChild == null)) { // only left child
      toRem.leftChild.isBlack = true; // child node should be black
      toRem.leftChild.parent = toRem.parent; // move references to "go around" toRem
      if (toRem.equals(root))
        root = toRem.leftChild;
      else if (toRem.isLeftChild())
        toRem.parent.leftChild = toRem.leftChild;
      else
        toRem.parent.rightChild = toRem.leftChild;
    } else if ((toRem.leftChild == null) && (toRem.rightChild != null)) { // only right child
      toRem.rightChild.isBlack = true;
      toRem.rightChild.parent = toRem.parent;
      if (toRem.equals(root))
        root = toRem.rightChild;
      else if (toRem.isLeftChild())
        toRem.parent.leftChild = toRem.rightChild;
      else
        toRem.parent.rightChild = toRem.rightChild;
    } else if ((toRem.leftChild == null) && (toRem.rightChild == null) && !toRem.isBlack) {
      // this means toRem has no children and is red
      if (toRem.isLeftChild())
        toRem.parent.leftChild = null; // set parents child reference to null
      else // toRem is a right child
        toRem.parent.rightChild = null;
    } else { // toRem has no children and is black
      removeBlackChildNoChildren(toRem);
    }
    return toReturn;
  }

  /**
   * This private helper method ensures that removing a black node with no children from the RBT
   * goes smoothly.
   * 
   * @param toRem - the node that is being removed
   */
  private void removeBlackChildNoChildren(Node<T> toRem) {
    boolean left = toRem.isLeftChild(); // toRem is leftChild
    boolean right = !toRem.isLeftChild(); // toRem is rightChild
    boolean neiceLeftBlackOrNull;
    boolean neiceRightBlackOrNull;
    Node<T> doubleBlack = toRem; // this concept will be kept track of as we go along
    if ((left && toRem.parent.rightChild.leftChild == null)
        || (left && toRem.parent.rightChild.leftChild.isBlack)
        || (right && toRem.parent.leftChild.leftChild == null)
        || (right && toRem.parent.leftChild.leftChild.isBlack)) // sibling's lc black or null
      neiceLeftBlackOrNull = true;
    else
      neiceLeftBlackOrNull = false;
    if ((left && toRem.parent.rightChild.rightChild != null)
        || (left && toRem.parent.rightChild.rightChild.isBlack)
        || (right && toRem.parent.leftChild.rightChild != null)
        || (right && toRem.parent.leftChild.rightChild.isBlack)) // sibling's rc black or null
      neiceRightBlackOrNull = true;
    else
      neiceRightBlackOrNull = false;


    if ((left && !toRem.parent.rightChild.isBlack) || (right && !toRem.parent.leftChild.isBlack)) {
      // toRem's sibling is red, case 1
      blackChildNoChildrenCase1(toRem);
    } else if ((left && neiceLeftBlackOrNull && neiceRightBlackOrNull)
        || (right && neiceLeftBlackOrNull && neiceRightBlackOrNull)) {
      // toRem's sibling is black and sibling's children are both black
      blackChildNoChildrenCase2(toRem);
    } else if ((left && !neiceRightBlackOrNull) || (right && !neiceLeftBlackOrNull)) {
      // toRem's sibling is black and opposite side child is red
      blackChildNoChildrenCase4(toRem); // makes more sense to check for case4 first
    } else { // toRem's sibling is black and same side child is red & opposite side is black
      blackChildNoChildrenCase3(toRem);
    }
  }

  /**
   * This private helper method deals with if the node we're removing's sibling is red. This is
   * accomplished by rotating toRem's sibling and parent then color swapping them. This order of the
   * tree will be handled with a different case.
   * 
   * @param toRem - the node we're removing
   */
  private void blackChildNoChildrenCase1(Node<T> toRem) {
    if (toRem.isLeftChild()) {
      rotate(toRem.parent.rightChild, toRem.parent);
      if (toRem.parent.isBlack) { // swap color
        toRem.parent.isBlack = false;
        toRem.parent.rightChild.isBlack = true;
      } else {
        toRem.parent.isBlack = true;
        toRem.parent.rightChild.isBlack = false;
      }
    } else { // toRem is the right child
      rotate(toRem.parent.leftChild, toRem.parent);
      if (toRem.parent.isBlack) { // swap color
        toRem.parent.isBlack = false;
        toRem.parent.leftChild.isBlack = true;
      } else {
        toRem.parent.isBlack = true;
        toRem.parent.leftChild.isBlack = false;
      }
    }
    remove(toRem); // call again, new order tree can help
  }

  /**
   * This method deals with the case of the black child with no children whose black sibling's
   * children are black or null. This is dealt with depending on the parent's color.
   * 
   * @param toRem - the node that is being removed
   */
  private void blackChildNoChildrenCase2(Node<T> toRem) {
    if (!toRem.parent.isBlack) { // parent is red
      toRem.parent.isBlack = true;
      if (toRem.isLeftChild()) {
        toRem.parent.leftChild = null;
        toRem.parent.rightChild.isBlack = false;
      } else {
        toRem.parent.rightChild = null;
        toRem.parent.leftChild.isBlack = false;
      } // done!
    } else { // parent is black
      if (toRem.isLeftChild()) // reassign color of sibling
        toRem.parent.rightChild.isBlack = false;
      else
        toRem.parent.leftChild.isBlack = false;
      // moveDoubleBlackUp(toRem.parent);
    }
  }

  /**
   * This private helper method deals with the case of the black child with no children whose black
   * sibling's same side child is red and opposite side child is black. This is resolved by a color
   * swap and rotate and then case 4.
   * 
   * @param toRem - the node being removed
   */
  private void blackChildNoChildrenCase3(Node<T> toRem) {
    if (toRem.isLeftChild()) {
      toRem.parent.rightChild.isBlack = false; // change color of sibling/neice nodes
      toRem.parent.rightChild.leftChild.isBlack = true;
      rotate(toRem.parent.rightChild.leftChild, toRem.parent.rightChild);
      // rotate neice with sibling
    } else { // toRem is right child
      toRem.parent.leftChild.isBlack = false;
      toRem.parent.leftChild.rightChild.isBlack = true;
      rotate(toRem.parent.leftChild.rightChild, toRem.parent.leftChild);
    }
    blackChildNoChildrenCase4(toRem);
  }

  /**
   * This private helper method deals with the case of the black child with no children whose
   * sibling's opposite side child is red.
   * 
   * @param toRem - the node being removed
   */
  private void blackChildNoChildrenCase4(Node<T> toRem) {
    if (toRem.isLeftChild())
      rotate(toRem.parent.rightChild, toRem.parent); // rotate sibling and parent
    else // toRem is righht child
      rotate(toRem.parent.leftChild, toRem.parent);
    if (toRem.parent.isBlack)
      toRem.parent.parent.isBlack = true;
    else
      toRem.parent.parent.isBlack = false;
  }

  /**
   * This private helper method goes through case three (two children) to remove a node from the
   * RBT.
   * 
   * @param toRem - the node that is being removed
   */
  private void removeTwoChildren(Node<T> toRem) {
    Node<T> replace = leftMostNodeOfSubtree(toRem.rightChild);
    replace.parent = toRem.parent;
    replace.leftChild = toRem.leftChild;
    replace.rightChild = toRem.rightChild;
    toRem = replace;
    if (toRem.isBlack && !replace.isBlack) // recolor so no tree imbalances
      replace.isBlack = true;
    else if (!toRem.isBlack && replace.isBlack)
      replace.isBlack = false;
    if (toRem.equals(root)) // reassign root node
      root = replace;
    remove(replace); // this node at the bottom of the tree must be removed
  }

  /**
   * This method checks if a node is in the tree by comparing the string value of it's data with
   * those of the existing nodes in the tree.
   * 
   * @param node - the node we're seeing if it contains
   * @return - true if the node is in the tree, false if it is not
   * @throws - IllegalArgumentException if the node is null
   */
  public boolean contains(Node<T> node) throws IllegalArgumentException {
    if (node == null)
      throw new IllegalArgumentException("Cannot search for a null node");
    String treeString = root.toString();
    treeString = treeString.substring(1, treeString.length() - 2); // get rid of []
    String[] toParse = treeString.split(",");
    for (String compare : toParse) {
      if (compare.equals(node.data.toString()))
        return true;
    }
    return false;
  }
}
