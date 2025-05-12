package dev.mil.system.mygallery;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class GalleryContent extends JPanel {
    private JPanel gridPanel;
    private GalleryInfo galleryInfo;
    private File currentDirectory = new File("sample");

    public interface ProgressCallback {
        void onProgress(int current, int total);
    }

    public GalleryContent() {
        setLayout(new BorderLayout());
        gridPanel = new JPanel(new GridLayout(0, 3, 5, 5));
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        add(scrollPane, BorderLayout.CENTER);
        loadImages(currentDirectory);
    }

    public void setGalleryInfo(GalleryInfo info) {
        this.galleryInfo = info;
    }

    public void loadImages(File directory, ProgressCallback progressCallback) {
        gridPanel.removeAll();
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif|bmp)$"));
        this.currentDirectory = directory;

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                final File fileRef = files[i]; 
                final int fileIndex = i;

                ImageIcon icon = new ImageIcon(fileRef.getPath());
                Image image = icon.getImage();
                int width = image.getWidth(null);
                int height = image.getHeight(null);
                int thumbWidth = 100;
                int thumbHeight = (int) ((double) height / width * thumbWidth);
                Image scaled = image.getScaledInstance(thumbWidth, thumbHeight, Image.SCALE_SMOOTH);

                JLabel label = new JLabel(new ImageIcon(scaled));
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));

                label.addMouseListener(new MouseAdapter() {
                    Color originalBg = label.getBackground();
                    boolean isOpaque = label.isOpaque();

                    public void mouseEntered(MouseEvent e) {
                        label.setOpaque(true);
                        label.setBackground(new Color(220, 240, 255));
                        label.repaint();
                    }

                    public void mouseExited(MouseEvent e) {
                        label.setOpaque(isOpaque);
                        label.setBackground(originalBg);
                        label.repaint();
                    }

                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            File[] freshFiles = currentDirectory.listFiles((dir, name) -> name.toLowerCase().matches(".*\\.(jpg|jpeg|png|bmp|gif)$"));
                            if (freshFiles == null) return;

                            List<File> fileList = Arrays.asList(freshFiles);
                            int clickedIndex = fileList.indexOf(fileRef);
                            if (clickedIndex >= 0) {
                                new FullscreenImageViewer(fileList, clickedIndex).setVisible(true);
                            }
                        } else {
                            if (galleryInfo != null) {
                                galleryInfo.updateInfo(fileRef);
                            }
                        }
                    }
                });

                gridPanel.add(label);

                if (progressCallback != null) {
                    progressCallback.onProgress(i + 1, files.length);
                }
            }
        }

        revalidate();
        repaint();
    }

    public void loadImages(File directory) {
        loadImages(directory, null);
    }

    public void updateDirectory(File directory) {
        this.currentDirectory = directory;
        loadImages(directory);
    }

    public File getCurrentDirectory() {
        return currentDirectory;
    }
}
