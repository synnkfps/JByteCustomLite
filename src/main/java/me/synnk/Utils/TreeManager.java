package me.synnk.Utils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeManager extends JTree {
    public static DefaultMutableTreeNode treeify(Object[] values) {
        DefaultMutableTreeNode root = null;
        DefaultMutableTreeNode subRoot = null;
        for (Object value : values) {
            if (root == null) {
                root = new DefaultMutableTreeNode(value);
            } else if (subRoot == null){
                subRoot = new DefaultMutableTreeNode(value);
                root.add(subRoot);
            } else {
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(value);
                subRoot.add(child);
                subRoot = child;
            }
        }

        return root;
    }

}
