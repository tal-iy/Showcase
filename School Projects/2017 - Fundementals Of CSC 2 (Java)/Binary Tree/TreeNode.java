package binarytree;

/**
 *
 * @author v.shydlonok
 * @param <T>
 */
public class TreeNode<T>
{
    private T element;
    private TreeNode left;
    private TreeNode right;
    
    public TreeNode()
    {
        element = null;
        left = null;
        right = null;
    }
    
    public TreeNode(T element)
    {
        this.element = element;
        left = null;
        right = null;
    }
    
    public TreeNode(T element, TreeNode left, TreeNode right)
    {
        this.element = element;
        this.left = left;
        this.right = right;
    }

    public T getElement() {
        return element;
    }

    public TreeNode getLeft() {
        return left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setElement(T element) {
        this.element = element;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }
    
    public int numChildren()
    {
        int children = 0;
        
        if (left != null)
            children = 1 + left.numChildren();
        if (right != null)
            children += 1 + right.numChildren();
        
        return children;
    }
}
