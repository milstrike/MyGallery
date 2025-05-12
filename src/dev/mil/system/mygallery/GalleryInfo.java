package dev.mil.system.mygallery;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;

public class GalleryInfo extends JPanel {
    private JLabel thumbnail;
    private JTextArea infoText;
    private JScrollPane scrollPane;

    public GalleryInfo() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel thumbnailWrapper = new JPanel(new BorderLayout());
        thumbnailWrapper.setMaximumSize(new Dimension(Short.MAX_VALUE, 200));
        thumbnailWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        thumbnail = new JLabel();
        thumbnail.setHorizontalAlignment(JLabel.CENTER);
        thumbnailWrapper.add(thumbnail, BorderLayout.CENTER);

        infoText = new JTextArea();
        infoText.setEditable(false);
        infoText.setLineWrap(true);
        infoText.setWrapStyleWord(true);
        infoText.setFont(UIManager.getFont("Label.font"));
        infoText.setBorder(null);
        infoText.setBackground(null);
        infoText.setAlignmentX(Component.LEFT_ALIGNMENT);

        scrollPane = new JScrollPane(infoText);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        scrollPane.setBorder(null);

        add(thumbnailWrapper);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(scrollPane);
    }

    public void updateInfo(File file) {
        ImageIcon icon = new ImageIcon(file.getPath());
        Image image = icon.getImage();
        int originalWidth = image.getWidth(null);
        int originalHeight = image.getHeight(null);

        int targetWidth = 260;
        int scaledHeight = (int)((double) originalHeight / originalWidth * targetWidth);
        Image scaledImage = image.getScaledInstance(targetWidth, scaledHeight, Image.SCALE_SMOOTH);

        thumbnail.setIcon(new ImageIcon(scaledImage));

        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(file.getName()).append("\n");
        sb.append("Location: ").append(file.getAbsolutePath()).append("\n");
        sb.append("Size: ").append(file.length()).append(" bytes\n");
        sb.append("Dimensions: ").append(originalWidth).append("x").append(originalHeight).append("\n");

        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sb.append("Created: ").append(sdf.format(attr.creationTime().toMillis()));
        } catch (Exception e) {
            sb.append("Created: Unknown");
        }

        infoText.setText(sb.toString());
        infoText.setCaretPosition(0);
    }
}
