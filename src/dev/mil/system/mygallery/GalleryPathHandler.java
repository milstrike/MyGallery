package dev.mil.system.mygallery;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GalleryPathHandler extends JPanel {
    private JTextField pathField;
    private JButton browseButton;
    private GalleryContent galleryContent;

    public interface DirectorySelectionListener {
        void onDirectorySelected(File selectedDir);
    }

    private DirectorySelectionListener directorySelectionListener;

    public void setDirectorySelectionListener(DirectorySelectionListener listener) {
        this.directorySelectionListener = listener;
    }

    public GalleryPathHandler(GalleryContent content) {
        this.galleryContent = content;
        setLayout(new BorderLayout(5, 0));

        File initialDir = readSavedPath();
        pathField = new JTextField(initialDir.getAbsolutePath());
        galleryContent.updateDirectory(initialDir);

        pathField.setEditable(false);
        browseButton = new JButton("Browse");

        browseButton.addActionListener((ActionEvent e) -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedDir = chooser.getSelectedFile();
                pathField.setText(selectedDir.getAbsolutePath());
                savePath(selectedDir);

                if (directorySelectionListener != null) {
                    directorySelectionListener.onDirectorySelected(selectedDir);
                }
            }
        });

        add(pathField, BorderLayout.CENTER);
        add(browseButton, BorderLayout.EAST);
    }
    
    
    private File readSavedPath() {
        File conf = new File("gallerypath.conf");
        if (conf.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(conf))) {
                String path = reader.readLine();
                File file = new File(path);
                if (file.exists() && file.isDirectory()) {
                    return file;
                }
            } catch (IOException ignored) {}
        }
        return new File("sample"); 
    }

    private void savePath(File path) {
        try (FileWriter writer = new FileWriter("gallerypath.conf")) {
            writer.write(path.getAbsolutePath());
        } catch (IOException ignored) {}
    }

    
}
