package org.example.frame;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.apache.commons.lang3.StringUtils;
import org.example.handler.BaseEventHandler;
import org.example.handler.ButtonEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class MainFrame extends JFrame {

    private final List<JTextField> textFields;

    private JTextArea textArea;

    public final Color defaultColor;

    static final String placeHolderPrefix = "Param: ";

    static {
        FlatDarculaLaf.setup();
    }

    public MainFrame(Function<ButtonEvent, String> consumer) {
        setTitle("Calc");
        setLayout(new BorderLayout());
        // 创建组件
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 8, 10, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JComboBox<String> comboBox = new JComboBox<>(BaseEventHandler.mapping.keySet().toArray(String[]::new));
        JButton submit = new JButton("Execute");
        JButton clear = new JButton("Clear");

        textArea = new JTextArea();
        textArea.setLineWrap(true); // 自动换行
        textArea.setEditable(false);
        textArea.setFont(new Font(textArea.getFont().getFontName(), textArea.getFont().getStyle(), 16));
        textArea.setWrapStyleWord(true); // 只在单词边界换行
        // 创建 JScrollPane 包裹 JTextArea
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Console"));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        List<JTextField> list = new ArrayList<>(21);
        defaultColor = new JTextField().getForeground();
        for (int i = 0; i < 21; i++) {
            JTextField textField = new JTextField(placeHolderPrefix + (i + 1));
            textField.setForeground(Color.GRAY);
            addPlaceHolder(textField, i + 1);
            list.add(textField);
            panel.add(textField);
        }
        textFields = Collections.unmodifiableList(list);
        panel.add(comboBox);
        panel.add(submit);
        panel.add(clear);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.EAST);
        add(new JPanel(), BorderLayout.SOUTH);

        clear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clear(e.getClickCount() == 2);
            }
        });

        MainFrame mainFrame = this;
        submit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Object code = comboBox.getSelectedItem();
                if (code != null) {
                    String ret = consumer.apply(new ButtonEvent(code.toString(), mainFrame));
                    if (StringUtils.isNotBlank(ret)) {
                        textArea.append(ret + "\n");
                    }
                }
            }
        });
    }

    private void addPlaceHolder(JTextField textField, int no) {
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Color foreground = textField.getForeground();
                if (Objects.equals(foreground, Color.GRAY)) {
                    textField.setText(StringUtils.EMPTY);
                    textField.setForeground(defaultColor);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (StringUtils.isBlank(textField.getText())) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeHolderPrefix + no);
                }
            }
        });
    }

    public void clear(boolean clearTextField) {
        if (clearTextField) {
            for (int i = 0; i < textFields.size(); i++) {
                JTextField textField = textFields.get(i);
                if (!Objects.equals(textField.getForeground(), Color.gray)) {
                    textField.setForeground(Color.gray);
                    textField.setText(placeHolderPrefix + (i + 1));
                }
            }
        }
        textArea.setText(StringUtils.EMPTY);
    }

    public List<JTextField> getTextFields() {
        return textFields;
    }
}
