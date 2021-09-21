public class ParseTree {

	private TreeNode root;

	public ParseTree() {
		this.root = null;
	}

	public ParseTree(TreeNode root) {
		this.root = root;
	}

	public TreeNode getRoot() {
		return this.root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	private String spaces(int num) {
		String s = "";

		for (int i = 0; i < num - 1; i++)
			s += "| ";

		s += "|-";

		return s;
	}

	private String stringify(TreeNode current, int depth) {

		String s = current.toString() + "\n";
		if (current.getChildren().size() > 0) {
			for (int i = 0; i < current.getChildren().size() - 1; i++) {
				s += spaces(depth + 1) + stringify(current.getChildren().get(i), depth + 1);
			}
			s += spaces(depth + 1) + stringify(current.getChildren().get(current.getChildren().size() - 1), depth + 1);
		}

		return s;

	}

	@Override
	public String toString() {
		if (null == root)
			return "EMPTY TREE";
		return stringify(root, 0);
	}
}
