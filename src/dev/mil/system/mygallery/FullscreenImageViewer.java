package dev.mil.system.mygallery;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.io.File;

public class FullscreenImageViewer extends JFrame {
    private JLabel imageLabel;
    private List<File> imageFiles;
    private int currentIndex;

    public FullscreenImageViewer(List<File> imageFiles, int startIndex) {
        this.imageFiles = imageFiles;
        this.currentIndex = startIndex;

        setTitle(imageFiles.get(startIndex).getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setBackground(Color.BLACK);
        imageLabel.setOpaque(true);
        add(imageLabel, BorderLayout.CENTER);

        JButton leftButton = createNavButton("←");
        JButton rightButton = createNavButton("→");
        JButton closeButton = new JButton("Close");

        leftButton.addActionListener(e -> showImage(currentIndex - 1));
        rightButton.addActionListener(e -> showImage(currentIndex + 1));
        closeButton.addActionListener(e -> dispose());

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.add(leftButton, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.add(rightButton, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(closeButton);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        showImage(currentIndex - 1);
                        return true;
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        showImage(currentIndex + 1);
                        return true;
                    } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        dispose();
                        return true;
                    }
                }
                return false;
            }
        });


        setFocusable(true);
        requestFocusInWindow();

        showImage(currentIndex);
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setMargin(new Insets(20, 20, 20, 20));
        return button;
    }

    private void showImage(int index) {
        if (index < 0 || index >= imageFiles.size()) return;
        currentIndex = index;
        File file = imageFiles.get(currentIndex);
        setTitle(file.getName());

        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
        Image image = icon.getImage();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int imgW = image.getWidth(null);
        int imgH = image.getHeight(null);
        int screenW = screenSize.width;
        int screenH = screenSize.height;

        double scale = Math.min((double)screenW / imgW, (double)screenH / imgH);
        int newW = (int)(imgW * scale);
        int newH = (int)(imgH * scale);

        Image scaled = image.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaled));
    }
}
