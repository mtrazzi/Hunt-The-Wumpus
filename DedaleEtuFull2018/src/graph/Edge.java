package graph;

public class Edge<L,R> {

	  private  L left;
	  private  R right;

	  public Edge(L left, R right) {
	    this.left = left;
	    this.right = right;
	  }

	  public L getLeft() { return left; }
	  public R getRight() { return right;}
}
