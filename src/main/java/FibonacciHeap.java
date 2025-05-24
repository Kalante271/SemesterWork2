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
        min = mergeLists(min, newNode);
        size++;
        return newNode;
    }

    public void mergeHeaps(FibonacciHeap heap2) {
        if(heap2.isEmpty() || heap2 == null) return;
        min = mergeLists(min, heap2.min);
        size += heap2.size;
        heap2.clear();
    }

    private void consolidate() {
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
        Node extractedMin = min;
        if(extractedMin != null) {
            if(extractedMin.getChild() != null) {
                List<Node> children = new ArrayList<>();
                Node child = extractedMin.getChild();
                do {
                    children.add(child);
                    child.getRight();
                } while (child != extractedMin.getChild());

                for (Node c : children) {
                    c.setParent(null);
                    min = mergeLists(min, c);
                }
            }

            extractedMin.getLeft().setRight(extractedMin.getRight());
            extractedMin.getRight().setLeft(extractedMin.getLeft());
            if(extractedMin.getRight() == extractedMin) {
                min = null;
            } else {
                min = extractedMin.getRight();
                consolidate();
            }
            size--;
        }
        return extractedMin != null ? extractedMin.getKey() : null;
    }

    public void decreaseKey(Node node, Integer newKey) {
        if (newKey.compareTo(node.getKey()) > 0) {
            throw new IllegalArgumentException("New key must be smaller than current key!");
        }
        node.setKey(newKey);
        Node parent = node.getParent();

        if (parent != null && node.getKey().compareTo(parent.getKey()) < 0) {
            cut(node, parent);
            cascadingCut(parent);
        }

        if (node.getKey().compareTo(min.getKey()) < 0) {
            min = node;
        }
    }

    private void cut(Node node, Node parent) {
        if (node.getLeft() == node) {
            parent.setChild(null);
        } else {
            node.getLeft().setRight(node.getRight());
            node.getRight().setLeft(node.getLeft());
            if (parent.getChild() == node) {
                parent.setChild(node.getLeft());
            }
        }
        parent.setDegree(parent.getDegree() - 1);

        if (min != null) {
            node.setLeft(min);
            node.setRight(min.getRight());
            min.getRight().setLeft(node);
            min.setRight(node);
        }
        node.setParent(null);
        node.setMark(false);
    }


    private void cascadingCut(Node node) {
        Node parent = node.getParent();
        if (parent != null) {
            if (!node.isMark()) {
                node.setMark(true);
            } else {
                cut(node, parent);
                cascadingCut(parent);
            }
        }
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
