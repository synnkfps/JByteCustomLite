package me.synnk.Loaders;

import me.synnk.Interface.FrameRegisters;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TransferHandle extends TransferHandler {
    @Override
    public boolean canImport(TransferSupport support) {
        // Check if the dropped data contains files
        return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    @SuppressWarnings("unchecked")
    private List<File> getFileListFromTransferable(Transferable transferable) throws Exception {
        DataFlavor[] flavors = transferable.getTransferDataFlavors();
        for (DataFlavor flavor : flavors) {
            if (flavor.equals(DataFlavor.javaFileListFlavor)) {
                return (List<File>) transferable.getTransferData(flavor);
            }
        }
        throw new Exception("Unsupported data flavor: javaFileListFlavor not found");
    }

    @Override
    public boolean importData(TransferSupport support) {
        // Handle the imported files
        try {
            Transferable transferable = support.getTransferable();
            List<File> files = getFileListFromTransferable(transferable);

            for (File file : files) {
                String filePath = file.getAbsolutePath();
                System.out.println("Imported file path: " + filePath);
                FrameRegisters.fileLoaded(new File(filePath));
            }

            return true; // Import successful
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Import failed
        }
    }
}
