package org.example;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame {

    static {
        FlatDarculaLaf.setup();
    }

    public MainFrame() {
        setLayout(new BorderLayout());
        // 创建组件
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 10));
        JTextField textField1 = new JTextField(12);
        JTextField textField2 = new JTextField(12);
        JTextField textField3 = new JTextField(12);
        String[] comboItems = {"Option 1", "Option 2", "Option 3"};
        JComboBox<String> comboBox = new JComboBox<>(comboItems);
        JButton submit = new JButton("Submit");
        JButton clear = new JButton("Clear");

        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true); // 自动换行
        textArea.setWrapStyleWord(true); // 只在单词边界换行
        // 创建 JScrollPane 包裹 JTextArea
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(textField1);
        panel.add(textField2);
        panel.add(textField3);
        panel.add(comboBox);
        panel.add(submit);
        panel.add(clear);

        add(panel, BorderLayout.NORTH);
        setTitle("Title");
        add(scrollPane, BorderLayout.CENTER);
        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.EAST);
    }
}
