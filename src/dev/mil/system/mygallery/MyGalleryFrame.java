package dev.mil.system.mygallery;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MyGalleryFrame extends JFrame {
    private JDialog loadingDialog;
    private JLabel progressLabel;
    private JProgressBar progressBar;

    private void showLoadingDialog(int totalCount) {
        loadingDialog = new JDialog(this, "Loading Images", false);
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        progressLabel = new JLabel("Loading images...");
        panel.add(progressLabel, BorderLayout.NORTH);

        progressBar = new JProgressBar(0, totalCount);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        panel.add(progressBar, BorderLayout.CENTER);

        loadingDialog.getContentPane().add(panel);
        loadingDialog.setSize(350, 120);
        loadingDialog.setLocationRelativeTo(this);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDialog.setResizable(false);
        SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true));
    }

    public MyGalleryFrame() {
        setTitle("MyGallery");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel rootPanel = new JPanel(null);
        rootPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        GalleryContent galleryContent = new GalleryContent();
        GalleryInfo galleryInfo = new GalleryInfo();
        GalleryPathHandler galleryPathHandler = new GalleryPathHandler(galleryContent);
        galleryPathHandler.setDirectorySelectionListener(selectedDir -> {
            File[] files = selectedDir.listFiles((dir, name) -> name.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif|bmp)$"));
            int totalCount = files != null ? files.length : 0;

            showLoadingDialog(totalCount);

            SwingWorker<Void, Void> reloadWorker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    galleryContent.loadImages(selectedDir, (current, total) -> {
                        SwingUtilities.invokeLater(() -> {
                            progressBar.setValue(current);
                            progressLabel.setText("Loading " + current + " of " + total + "...");
                        });
                    });
                    return null;
                }

                @Override
                protected void done() {
                    loadingDialog.dispose();
                }
            };
            reloadWorker.execute();
        });

        galleryPathHandler.setPreferredSize(new Dimension(9999, 40));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        leftPanel.add(galleryContent, BorderLayout.CENTER);
        leftPanel.add(galleryPathHandler, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        rightPanel.add(galleryInfo, BorderLayout.CENTER);

        galleryContent.setGalleryInfo(galleryInfo);

        leftPanel.setBounds(0, 0, (int) (800 * 0.65) - 10, 540);
        rightPanel.setBounds((int) (800 * 0.65), 0, (int) (800 * 0.35) - 10, 570);

        rootPanel.add(leftPanel);
        rootPanel.add(rightPanel);
        add(rootPanel);

        File[] files = galleryContent.getCurrentDirectory().listFiles((dir, name) -> name.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif|bmp)$"));
        int totalCount = files != null ? files.length : 0;

        showLoadingDialog(totalCount);
        SwingWorker<Void, Void> loader = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                galleryContent.loadImages(galleryContent.getCurrentDirectory(), (current, total) -> {
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(current);
                        progressLabel.setText("Loading " + current + " of " + total + "...");
                    });
                });
                return null;
            }

            @Override
            protected void done() {
                loadingDialog.dispose();
            }
        };
        loader.execute();

        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
        
        setJMenuBar(new MyGalleryMenuBar(this));
    }
}
