package com.josh.binarytree;

public class BinaryTreeModel {
    private Object value;
    private BinaryTreeModel left;
    private BinaryTreeModel right;

    public BinaryTreeModel(Object value) {
        this.value = value;
    }

    public BinaryTreeModel getLeft() {
        return left;
    }

    public BinaryTreeModel getRight() {
        return right;
    }

    public Object getValue() {
        return value;
    }

    public void setLeft(BinaryTreeModel left) {
        this.left = left;
    }

    public void setRight(BinaryTreeModel right) {
        this.right = right;
    }
}
