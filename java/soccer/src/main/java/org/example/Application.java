package org.example;

import org.example.frame.MainFrame;
import org.example.handler.BaseEventHandler;

import javax.swing.*;
import java.awt.*;

public class Application {
    public static void main(String[] args) {
        BaseEventHandler baseEventHandler = new BaseEventHandler();
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(baseEventHandler::handle);
            frame.setPreferredSize(new Dimension(830, 460));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
