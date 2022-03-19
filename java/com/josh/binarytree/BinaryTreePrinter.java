package com.josh.binarytree;

import java.io.PrintStream;

public class BinaryTreePrinter {
    private BinaryTreeModel tree;

    BinaryTreePrinter(BinaryTreeModel tree) {
        this.tree = tree;
    }

    public String traversePreOrder(BinaryTreeModel root) {
        if (root == null)
            return "";
        StringBuilder sb = new StringBuilder();
        sb.append(root.getValue());
        String pointerForRight = "└───";
        String pointerForLeft = (root.getRight() != null) ? "├───" : "└───";
        traverseNodes(sb, "", pointerForLeft, root.getLeft(), root.getRight() != null);
        traverseNodes(sb, "", pointerForRight, root.getRight(), false);
        return sb.toString();
    }

    public void traverseNodes(StringBuilder sb, String padding, String pointer, BinaryTreeModel node,
            boolean hasRightSibling) {
        if (node != null) {
            sb.append("\n");
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.getValue());

            StringBuilder paddingBuilder = new StringBuilder(padding);
            if (hasRightSibling) {
                paddingBuilder.append("|   ");
            } else {
                paddingBuilder.append("   ");
            }
            String paddingForBoth = paddingBuilder.toString();
            String pointerForRight = "└───";
            String pointerForLeft = (node.getRight() != null) ? "├───" : "└───";
            traverseNodes(sb, paddingForBoth, pointerForLeft, node.getLeft(), node.getRight() != null);
            traverseNodes(sb, paddingForBoth, pointerForRight, node.getRight(), false);
        }
    }

    public void print(PrintStream os) {
        os.print(traversePreOrder(tree));
    }
}
