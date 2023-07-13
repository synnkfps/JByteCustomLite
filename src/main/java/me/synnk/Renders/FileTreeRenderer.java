package me.synnk.Renders;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class FileTreeRenderer {

    private static JTree matrix;
    public static class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
        public CustomTreeCellRenderer(JTree dir) {
            matrix = dir;
        }

        @Override
        public Dimension getPreferredSize() {
            // Override preferred size to allow for a larger width
            Dimension preferredSize = super.getPreferredSize();
            preferredSize.width = Math.max(preferredSize.width, matrix.getWidth()); // Adjust the desired width as needed
            return preferredSize;
        }
    }
}
