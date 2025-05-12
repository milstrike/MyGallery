package dev.mil.system.mygallery;

import javax.swing.*;

public class MyGalleryMenuBar extends JMenuBar {
    public MyGalleryMenuBar(JFrame parent) {
        JMenu helpMenu = new JMenu("Help");

        JMenuItem userGuideItem = new JMenuItem("User Guide");
        userGuideItem.addActionListener(e -> {
            new HelpDialog(parent).setVisible(true);
        });

        JMenuItem aboutItem = new JMenuItem("About MyGallery");
        aboutItem.addActionListener(e -> {
            new AboutDialog(parent).setVisible(true);
        });


        helpMenu.add(userGuideItem);
        helpMenu.add(aboutItem);
        add(helpMenu);
    }
}

