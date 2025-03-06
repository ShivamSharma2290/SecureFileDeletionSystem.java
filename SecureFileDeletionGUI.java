import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SecureFileDeletionGUI {

    // List to store selected files for batch deletion
    private List<File> selectedFiles = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SecureFileDeletionGUI::new);
    }

    public SecureFileDeletionGUI() {
        // Create the main frame
        JFrame frame = new JFrame("Secure File Deletion System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Create a panel for buttons and labels
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        JLabel instructions = new JLabel("Choose an option to securely delete files:");
        instructions.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(instructions);

        JButton deleteSingleFileButton = new JButton("Delete a Single File");
        deleteSingleFileButton.addActionListener(e -> handleSingleFileDeletion(frame));
        panel.add(deleteSingleFileButton);

        JButton deleteBatchFilesButton = new JButton("Delete Multiple Files");
        deleteBatchFilesButton.addActionListener(e -> handleBatchFileSelection(frame));
        panel.add(deleteBatchFilesButton);

        JButton startBatchDeletionButton = new JButton("Start Batch Deletion");
        startBatchDeletionButton.addActionListener(e -> startBatchDeletion(frame));
        panel.add(startBatchDeletionButton);

        // Add the panel to the frame
        frame.add(panel, BorderLayout.CENTER);

        // Display the frame
        frame.setVisible(true);
    }

    // Handle single file deletion
    private void handleSingleFileDeletion(JFrame frame) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a File to Delete");
        int result = fileChooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String passesInput = JOptionPane.showInputDialog(frame, "Enter the number of overwrite passes (default is 3):");
            int passes = passesInput != null && passesInput.matches("\\d+") ? Integer.parseInt(passesInput) : 3;

            boolean success = SecureFileDeletion.overwriteFile(file.getAbsolutePath(), passes);
            JOptionPane.showMessageDialog(frame,
                    success ? "File deleted successfully!" : "Failed to delete the file.",
                    "Result",
                    success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        }
    }

    // Handle multiple file selection
    private void handleBatchFileSelection(JFrame frame) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setDialogTitle("Select Files for Batch Deletion");

        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            for (File file : files) {
                if (!selectedFiles.contains(file)) {
                    selectedFiles.add(file);
                }
            }
            JOptionPane.showMessageDialog(frame,
                    "Selected " + files.length + " files for batch deletion.",
                    "Batch Selection",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Start batch deletion process
    private void startBatchDeletion(JFrame frame) {
        if (selectedFiles.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "No files selected for batch deletion.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String passesInput = JOptionPane.showInputDialog(frame, "Enter the number of overwrite passes (default is 3):");
        int passes = passesInput != null && passesInput.matches("\\d+") ? Integer.parseInt(passesInput) : 3;

        List<String> filePaths = new ArrayList<>();
        for (File file : selectedFiles) {
            filePaths.add(file.getAbsolutePath());
        }

        SecureFileDeletion.deleteFilesInBatch(filePaths, passes);
        JOptionPane.showMessageDialog(frame,
                "Batch deletion completed.",
                "Result",
                JOptionPane.INFORMATION_MESSAGE);

        // Clear the list after deletion
        selectedFiles.clear();
    }
}