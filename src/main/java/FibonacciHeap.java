import java.util.*;

public class FibonacciHeap {
    private int size;
    private Node min;

    public FibonacciHeap() {
        this.size = 0;
        this.min = null;
    }

    public boolean isEmpty() {
        return min == null;
    }

    public int size() {
        return size;
    }

    public Integer getMin() {
        return min != null ? min.getKey() : null;
    }

    public void clear() {
        min = null;
        size = 0;
    }

    public Node insert(Integer key) {
        Node newNode = new Node(key);
        mergeLists(min, newNode);
        size++;
        return newNode;
    }

    public void mergeHeaps(FibonacciHeap heap2) {
        if(heap2.isEmpty() || heap2 == null) return;
        min = mergeLists(min, heap2.min);
        size += heap2.size;
        heap2.clear();
    }

    public void consolidate() {
        int arraySize = ((int) Math.floor(Math.log(size) / Math.log(2))) + 1;
        List<Node> degreeList = new ArrayList<>(Collections.nCopies(arraySize, null));

        List<Node> rootList = new ArrayList<>();
        Node currentRoot = min;
        if (currentRoot != null) {
            do {
                rootList.add(currentRoot);
                currentRoot = currentRoot.getRight();
            } while (currentRoot != min);
        }

        for(Node currentTree:rootList) {
            int degree = currentTree.getDegree();

            while(degreeList.get(degree) != null) {
                Node existingTree = degreeList.get(degree);

                if(currentTree.getKey() > existingTree.getKey()) {
                    Node temp = currentTree;
                    currentTree = existingTree;
                    existingTree = temp;
                }

                link(existingTree, currentTree);
                degreeList.set(degree, null);
                degree++;
            }
            degreeList.set(degree, currentTree);
        }

        min = null;
        for (Node node : degreeList) {
            if (node != null) {
                min = mergeLists(min, node);
            }
        }
    }

    public Integer extractMin() {

    }

    public void decreaseKey() {

    }

    public void cut() {

    }

    public void cascadingCut() {

    }

    private void link(Node child, Node parent) {
        child.getLeft().setRight(child.getRight());
        child.getRight().setLeft(child.getLeft());
        child.setLeft(child);
        child.setRight(child);
        child.setParent(parent);

        parent.setChild(mergeLists(parent.getChild(), child));
        parent.setDegree(parent.getDegree() + 1);
        child.setMark(false);
    }

    private Node mergeLists(Node a, Node b) {
        if (a == null) return b;
        if (b == null) return a;

        Node aRight = a.getRight();
        Node bRight = b.getRight();

        a.setRight(bRight);
        bRight.setLeft(a);
        b.setRight(aRight);
        aRight.setLeft(b);

        return a.getKey() < b.getKey() ? a : b;
    }

}
