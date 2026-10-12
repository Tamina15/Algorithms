package com.tmn.voronoi;

public class RBTree {

    double x, y;
    RBTree root = null;

    RBTree rbPrevious;
    RBTree rbNext;

    RBTree rbLeft;
    RBTree rbRight;

    RBTree rbParent;

    boolean rbRed;

    Site site;
    CircleEvent circleEvent;
    Edge edge;

    public RBTree() {
        root = null;
        rbPrevious = null;
        rbNext = null;
        rbLeft = null;
        rbRight = null;
        rbParent = null;
        rbRed = false;
    }

    public void rbInsertSuccessor(RBTree node, RBTree successor) {
        RBTree parent;
        if (node != null) {
            successor.rbPrevious = node;
            successor.rbNext = node.rbNext;
            if (node.rbNext != null) {
                node.rbNext.rbPrevious = successor;
            }
            node.rbNext = successor;
            if (node.rbRight != null) {
                node = node.rbRight;
                while (node.rbLeft != null) {
                    node = node.rbLeft;
                }
                node.rbLeft = successor;
            } else {
                node.rbRight = successor;
            }
            parent = node;
        } else if (root != null) {
            node = getFirst(root);
            successor.rbPrevious = null;
            successor.rbNext = node;
            node.rbPrevious = successor;
            node.rbLeft = successor;
            parent = node;
        } else {
            successor.rbPrevious = successor.rbNext = null;
            this.root = successor;
            parent = null;
        }
        successor.rbLeft = successor.rbRight = null;
        successor.rbParent = parent;
        successor.rbRed = true;

        RBTree grandpa, uncle;
        node = successor;
        while (parent != null && parent.rbRed) {
            grandpa = parent.rbParent;
            if (parent == grandpa.rbLeft) {
                uncle = grandpa.rbRight;
                if (uncle != null && uncle.rbRed) {
                    parent.rbRed = uncle.rbRed = false;
                    grandpa.rbRed = true;
                    node = grandpa;
                } else {
                    if (node == parent.rbRight) {
                        rbRotateLeft(parent);
                        node = parent;
                        parent = node.rbParent;
                    }
                    parent.rbRed = false;
                    grandpa.rbRed = true;
                    rbRotateRight(grandpa);
                }
            } else {
                uncle = grandpa.rbLeft;
                if (uncle != null && uncle.rbRed) {
                    parent.rbRed = uncle.rbRed = false;
                    grandpa.rbRed = true;
                    node = grandpa;
                } else {
                    if (node == parent.rbLeft) {
                        rbRotateRight(parent);
                        node = parent;
                        parent = node.rbParent;
                    }
                    parent.rbRed = false;
                    grandpa.rbRed = true;
                    rbRotateLeft(grandpa);
                }
            }
            parent = node.rbParent;
        }
        root.rbRed = false;
    }

    public void rbRemoveNode(RBTree node) {
        if (node.rbNext != null) {
            node.rbNext.rbPrevious = node.rbPrevious;
        }
        if (node.rbPrevious != null) {
            node.rbPrevious.rbNext = node.rbNext;
        }
        node.rbNext = node.rbPrevious = null;
        RBTree parent = node.rbParent, left = node.rbLeft, right = node.rbRight, next;
        if (left == null) {
            next = right;
        } else if (right == null) {
            next = left;
        } else {
            next = getFirst(right);
        }
        if (parent != null) {
            if (parent.rbLeft == node) {
                parent.rbLeft = next;
            } else {
                parent.rbRight = next;
            }
        } else {
            this.root = next;
        }
        boolean isRed;
        if (left != null && right != null) {
            isRed = next.rbRed;
            next.rbRed = node.rbRed;
            next.rbLeft = left;
            left.rbParent = next;
            if (next != right) {
                parent = next.rbParent;
                next.rbParent = node.rbParent;
                node = next.rbRight;
                parent.rbLeft = node;
                next.rbRight = right;
                right.rbParent = next;
            } else {
                next.rbParent = parent;
                parent = next;
                node = next.rbRight;
            }
        } else {
            isRed = node.rbRed;
            node = next;
        }
        if (node != null) {
            node.rbParent = parent;
        }
        if (isRed) {
            return;
        }
        if (node != null && node.rbRed) {
            node.rbRed = false;
            return;
        }
        RBTree sibling;
        do {
            if (node == this.root) {
                break;
            }
            if (node == parent.rbLeft) {
                sibling = parent.rbRight;
                if (sibling.rbRed) {
                    sibling.rbRed = false;
                    parent.rbRed = true;
                    this.rbRotateLeft(parent);
                    sibling = parent.rbRight;
                }
                if ((sibling.rbLeft != null && sibling.rbLeft.rbRed) || (sibling.rbRight != null && sibling.rbRight.rbRed)) {
                    if (sibling.rbRight == null || !sibling.rbRight.rbRed) {
                        sibling.rbLeft.rbRed = false;
                        sibling.rbRed = true;
                        this.rbRotateRight(sibling);
                        sibling = parent.rbRight;
                    }
                    sibling.rbRed = parent.rbRed;
                    parent.rbRed = sibling.rbRight.rbRed = false;
                    rbRotateLeft(parent);
                    node = root;
                    break;
                }
            } else {
                sibling = parent.rbLeft;
                if (sibling.rbRed) {
                    sibling.rbRed = false;
                    parent.rbRed = true;
                    this.rbRotateRight(parent);
                    sibling = parent.rbLeft;
                }
                if ((sibling.rbLeft != null && sibling.rbLeft.rbRed) || (sibling.rbRight != null && sibling.rbRight.rbRed)) {
                    if (sibling.rbLeft == null || !sibling.rbLeft.rbRed) {
                        sibling.rbRight.rbRed = false;
                        sibling.rbRed = true;
                        this.rbRotateLeft(sibling);
                        sibling = parent.rbLeft;
                    }
                    sibling.rbRed = parent.rbRed;
                    parent.rbRed = sibling.rbLeft.rbRed = false;
                    rbRotateRight(parent);
                    node = this.root;
                    break;
                }
            }
            sibling.rbRed = true;
            node = parent;
            parent = parent.rbParent;
        } while (!node.rbRed);
        if (node != null) {
            node.rbRed = false;
        }

    }

    public void rbRotateLeft(RBTree node) {
        RBTree p = node, q = node.rbRight, // can't be null
                parent = p.rbParent;
        if (parent != null) {
            if (parent.rbLeft == p) {
                parent.rbLeft = q;
            } else {
                parent.rbRight = q;
            }
        } else {
            this.root = q;
        }
        q.rbParent = parent;
        p.rbParent = q;
        p.rbRight = q.rbLeft;
        if (p.rbRight != null) {
            p.rbRight.rbParent = p;
        }
        q.rbLeft = p;
    }

    public void rbRotateRight(RBTree node) {
        RBTree p = node,
                q = node.rbLeft, // can't be null
                parent = p.rbParent;
        if (parent != null) {
            if (parent.rbLeft == p) {
                parent.rbLeft = q;
            } else {
                parent.rbRight = q;
            }
        } else {
            this.root = q;
        }
        q.rbParent = parent;
        p.rbParent = q;
        p.rbLeft = q.rbRight;
        if (p.rbLeft != null) {
            p.rbLeft.rbParent = p;
        }
        q.rbRight = p;
    }

    public RBTree getFirst(RBTree node) {
        while (node.rbLeft != null) {
            node = node.rbLeft;
        }
        return node;
    }

    public RBTree getLast(RBTree node) {
        while (node.rbRight != null) {
            node = node.rbRight;
        }
        return node;
    }

}
