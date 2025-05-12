package dev.mil.system.mygallery;

import javax.swing.SwingUtilities;

public class MyGallery {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MyGalleryFrame());
    }
}
