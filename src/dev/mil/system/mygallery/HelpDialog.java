package dev.mil.system.mygallery;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class HelpDialog extends JDialog {
    private final JTextArea explanationArea;
    private final Map<String, String> helpTopics;

    public HelpDialog(JFrame parent) {
        super(parent, "Help - MyGallery", true);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        helpTopics = new LinkedHashMap<>();

        helpTopics.put("Gallery View",
            "What it is:\nDisplays a scrollable grid of image thumbnails loaded from the selected folder.\n\n" +
            "How to use:\nClick an image to view details, or double-click to view in fullscreen.");

        helpTopics.put("Browse Button",
            "What it is:\nLets you change the current image collection folder.\n\n" +
            "How to use:\nClick 'Browse', select a folder. The gallery updates and the folder path is saved.");

        helpTopics.put("Image Info Panel",
            "What it is:\nShows detailed information about a selected image (name, path, size, dimensions, created date).\n\n" +
            "How to use:\nClick any image once to display its details on the right panel.");

        helpTopics.put("Fullscreen Viewer",
            "What it is:\nDisplays the selected image in fullscreen with navigation.\n\n" +
            "How to use:\nDouble-click an image, or right-click and choose 'View Fullscreen'. Use arrow keys or side buttons to navigate.");

        helpTopics.put("Persistent Folder",
            "What it is:\nRemembers the last browsed folder between app sessions.\n\n" +
            "How it works:\nThe selected folder is saved in 'gallerypath.conf' and automatically loaded at startup.");

        helpTopics.put("Supported Formats",
            "What it is:\nThe image types MyGallery can handle.\n\n" +
            "Supported:\nJPG, JPEG, PNG, BMP, GIF");

        JList<String> topicList = new JList<>(helpTopics.keySet().toArray(new String[0]));
        topicList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroll = new JScrollPane(topicList);
        listScroll.setPreferredSize(new Dimension(180, 0));

        explanationArea = new JTextArea();
        explanationArea.setEditable(false);
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);
        explanationArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        explanationArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane explanationScroll = new JScrollPane(explanationArea);
        explanationScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        topicList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = topicList.getSelectedValue();
                if (selected != null) {
                    explanationArea.setText(helpTopics.get(selected));
                    explanationArea.setCaretPosition(0);
                }
            }
        });

        topicList.setSelectedIndex(0);

        add(listScroll, BorderLayout.WEST);
        add(explanationScroll, BorderLayout.CENTER);
    }
}
