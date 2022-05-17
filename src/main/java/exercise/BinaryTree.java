package exercise;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class BinaryTree {
    private static final Set<String> trees = new HashSet<>();
    private final String name;
    private BinaryTree left;
    private BinaryTree right;

    public BinaryTree(String name, BinaryTree left, BinaryTree right) {
        if (name == null) throw new NullPointerException("Node name must not be null");
        this.name = name;
        this.left = left;
        this.right = right;
    }

    // For unit test only
    void setLeft(BinaryTree left) {
        this.left = left;
    }

    // For unit test only
    void setRight(BinaryTree right) {
        this.right = right;
    }

    public static boolean EQ(BinaryTree tree1, BinaryTree tree2) {
        if (tree1 == null && tree2 == null) return true;
        if (tree1 == null || tree2 == null) return false;
        if (!tree1.getName().equals(tree2.getName())) return false;
        return EQ(tree1.getLeft(), tree2.getLeft()) && EQ(tree1.getRight(), tree2.getRight());
    }

    public String getName() {
        return name;
    }

    public BinaryTree getLeft() {
        return left;
    }

    public BinaryTree getRight() {
        return right;
    }

    /**
     * @param tree input tree to serialize
     * @param s    stack to hold current thread objects, to validate there are no cycle nodes
     * @return a string representation of BinaryTree
     */
    private static String serializeTreeInternal(BinaryTree tree, Stack<BinaryTree> s) {
        if (tree == null)
            return "#";
        if (!trees.contains(tree.toString()))
            s.push(tree);

        while (!s.isEmpty()) {
            BinaryTree t = s.pop();
            if (t != null) {
                if (!trees.add(t.toString())) throw new BinaryTreeSerializationException();
                s.push(t.left);
                s.push(t.right);
            }
        }
        return "{" + tree.getName() + "," + serializeTreeInternal(tree.left, s) + "," + serializeTreeInternal(tree.right, s) + "}";
    }


    /**
     * @return a string representation of BinaryTree
     */
    public String serializeTree() {
        return serializeTreeInternal(this, new Stack<>());
    }


    /**
     * One path on the input string
     *
     * @param str      byte array holding the string's bytes
     * @param position current position in the given byte array
     * @return a binaryTree
     */
    private static BinaryTree deserializeTreeInternal(byte[] str, int position) {
        String s = new String(str, StandardCharsets.UTF_8).replaceAll("[{}]", "");
        return deserializeHelper(new LinkedList<>(Arrays.asList(s.split(","))));
    }


    private static BinaryTree deserializeHelper(LinkedList<String> namesList) {
        if (namesList.isEmpty()) return null;
        String nameTree = namesList.poll();
        if (nameTree.equals("#"))
            return null;
        return new BinaryTree(nameTree, deserializeHelper(namesList), deserializeHelper(namesList));
    }


    /**
     * @param str byte array holding the string's bytes
     * @return a binaryTree
     */
    public static BinaryTree deserializeTree(String str) {
    //    if (str == null) throw new NullPointerException("Can't deserialize null");
        return deserializeTreeInternal(str.getBytes(), 0);
    }

}
