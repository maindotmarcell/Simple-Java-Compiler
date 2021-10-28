package com.company;



public class NodeWrapper {
    private TreeNode.Label label;
    private TreeNode node;
    private TreeNode parent;
    private String value;

    public NodeWrapper(TreeNode parent, TreeNode.Label label, String value) {
        this.parent = parent;
        this.label = label;
        this.value = value;
    }

    public TreeNode getParent() {
        return this.parent;
    }

    public TreeNode.Label getLabel() {
        return this.label;
    }

    public TreeNode getNode() {
        return this.node;
    }

    public void setNode(TreeNode node) {
        this.node = node;
    }

    public String getValue() {
        return this.value;
    }

    public TreeNode createNode(Token token) {
        this.node = new TreeNode(this.label, token, this.parent);
        this.parent.addChild(this.node);
        return this.node;
    }
}
