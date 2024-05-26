package org.example.frame;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.example.jc.TodayData;
import org.example.util.GridUtil;
import org.example.util.Meta;
import org.jdesktop.swingx.JXCollapsiblePane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OnlineFrame extends JFrame {
    static {
        FlatDarculaLaf.setup();
    }

    private MainFrame mainFrame;

    private boolean hasClearMain = false;

    public OnlineFrame(MainFrame owner, String title, TodayData todayData) {
        //设置模态框
        mainFrame = owner;
        setTitle("Online");
        setLayout(new BorderLayout(5, 5));

        if (!todayData.getSuccess()) {
            return;
        }

        // 创建组件
        JPanel panel = new JPanel();
        JScrollPane jScrollPane = new JScrollPane(panel);
        jScrollPane.setBorder(BorderFactory.createTitledBorder(title));
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollBar verticalScrollBar = jScrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(20);  // 每次滚动 20 像素

        panel.setLayout(new GridBagLayout());
        Map<Integer, Pair<Triple<String, String, Pair<String, String>>, List<TodayData.Cell>>> data = todayData.getData();
        List<Map.Entry<Integer, Pair<Triple<String, String, Pair<String, String>>, List<TodayData.Cell>>>> list = data.entrySet().stream().toList();
        for (int i = 0; i < list.size(); i++) {
            Pair<Triple<String, String, Pair<String, String>>, List<TodayData.Cell>> pair = list.get(i).getValue();
            JPanel cp1 = createCollapsePanel(pair);
            if (i == data.size() - 1) {
                panel.add(cp1, GridUtil.floorTopByRow(i, true));
            } else {
                panel.add(cp1, GridUtil.floorTopByRow(i, false));
            }
        }
        add(new JLabel(), BorderLayout.SOUTH);
        add(new JLabel(), BorderLayout.WEST);
        add(new JLabel(), BorderLayout.EAST);
        add(jScrollPane, BorderLayout.CENTER);
    }

    /**
     * 创建折叠面板
     *
     * @param pair 数据
     * @return
     */
    private JPanel createCollapsePanel(Pair<Triple<String, String, Pair<String, String>>, List<TodayData.Cell>> pair) {
        JXCollapsiblePane cp = new JXCollapsiblePane();
        cp.setLayout(new GridLayout(-1, 13, 0, 10));
        cp.setAnimated(false);
        cp.setCollapsed(true);
        createDataGrip(pair.getValue(), cp);

        JPanel jPanel = new JPanel(new GridBagLayout());
        JButton button = new JButton(cp.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION));
        button.setBorderPainted(false);
        formatText(button, pair.getKey());
        jPanel.add(button, GridUtil.floorTopByRow(0, false));
        jPanel.add(cp, GridUtil.floorTopByRow(1, true));
        return jPanel;
    }

    private void formatText(JButton button, Triple<String, String, Pair<String, String>> title) {
        button.setText("");
        button.setLayout(new GridLayout(-1, 3));
        JLabel titleLeft = new JLabel(title.getLeft());
        button.add(titleLeft);

        Pair<String, String> pair = title.getRight();
        JPanel panel = new JPanel(new GridLayout(-1, 5));
        panel.add(new JLabel());
        JLabel rightLabel = new JLabel(pair.getKey());
        rightLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        rightLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        panel.add(rightLabel);
        JLabel midLabel = new JLabel("VS");
        midLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        midLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(midLabel);
        JLabel leftLabel = new JLabel(pair.getValue());
        leftLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftLabel.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(leftLabel);

        panel.setBackground(new Color(0, 0, 0, 0));
        button.add(panel);

        JLabel titleRight = new JLabel(title.getMiddle());
        titleRight.setAlignmentX(Component.RIGHT_ALIGNMENT);
        titleRight.setHorizontalAlignment(SwingConstants.RIGHT);
        button.add(titleRight);
    }

    private void createDataGrip(List<TodayData.Cell> cellList, JXCollapsiblePane cp) {
        List<JPanel> jPanels = drawingCells(cp);
        for (TodayData.Cell cell : cellList) {
            Integer i = cell.getIndex();
            if (i < jPanels.size()) {
                JPanel panel = jPanels.get(i);
                JTextField textField = (JTextField) panel.getComponent(1);
                textField.setText(cell.getText());
                if (!textField.getForeground().equals(Color.gray)) {
                    addValueCopyListener(textField);
                }
            }
        }
    }

    /**
     * 添加只拷贝监听
     *
     * @param textField
     */
    private void addValueCopyListener(JTextField textField) {
        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if(StringUtils.isBlank(textField.getText())) {
                        return;
                    }
                    if (!hasClearMain) {
                        mainFrame.clear(true);
                        hasClearMain = true;
                    }
                    List<JTextField> textFields = mainFrame.getTextFields();
                    JTextField targetTextFiled = null;
                    for (int i = textFields.size() - 1; i >= 0; i--) {
                        if (i == 0) {
                            targetTextFiled = textFields.get(i);
                            break;
                        }
                        JTextField cur = textFields.get(i);
                        JTextField pre = textFields.get(i - 1);
                        boolean blank = Objects.equals(cur.getForeground(), Color.gray) ||
                                StringUtils.isBlank(cur.getText());
                        if (!blank) {
                            break;
                        }
                        if (!Objects.equals(pre.getForeground(), Color.gray) && StringUtils.isNotBlank(pre.getText())) {
                            targetTextFiled = cur;
                            break;
                        }
                    }
                    if (targetTextFiled == null) {
                        mainFrame.clear(true);
                        targetTextFiled = textFields.get(0);
                    }
                    targetTextFiled.setText(textField.getText());
                    targetTextFiled.setForeground(mainFrame.defaultColor);
                }
            }
        });
    }

    private List<JPanel> drawingCells(JXCollapsiblePane cp) {
        List<JPanel> cells = new ArrayList<>();
        Meta[] metaArr = Meta.values();
        for (Meta meta : metaArr) {
            Meta.TripleConsumer<JXCollapsiblePane, List<JPanel>, String[]> callback = meta.getCallback();
            callback.apply(cp, cells, meta.getLabelArr());
        }
        return cells;
    }
}
