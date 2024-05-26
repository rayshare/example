package org.example.util;

import org.jdesktop.swingx.JXCollapsiblePane;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 取值元数据
 */
public enum Meta {
    WDL("胜平负", 0,
            new String[]{"胜", "平", "负"},
            new String[]{"h", "d", "a"},
            Meta::drawingWdl),
    HWDL("让球胜平负", 1,
            new String[]{"胜", "平", "负", "让球"},
            new String[]{"h", "d", "a", "goalLine"},
            Meta::drawingHwdl),
    WSCORE("比分胜", 2,
            new String[]{"1:0", "2:0", "2:1", "3:0", "3:1", "3:2", "4:0", "4:1", "4:2", "5:0", "5:1", "5:2", "胜其他"},
            new String[]{"s01s00", "s02s00", "s02s01", "s03s00", "s03s01", "s03s02", "s04s00", "s04s01", "s04s02", "s05s00", "s05s01", "s05s02", "s1sh"},
            Meta::drawingWScore),
    DSCORE("比分平", 3,
            new String[]{"0:0", "1:1", "2:2", "3:3", "平其他"},
            new String[]{"s00s00", "s01s01", "s02s02", "s03s03", "s1sd"},
            Meta::drawingDScore),
    LSCORE("比分负", 4,
            new String[]{"0:1", "0:2", "1:2", "0:3", "1:3", "2:3", "0:4", "1:4", "2:4", "0:5", "1:5", "2:5", "负其他"},
            new String[]{"s00s01", "s00s02", "s01s02", "s00s03", "s01s03", "s02s03", "s00s04", "s01s04", "s02s04", "s00s05", "s01s05", "s02s05", "s1sa"},
            Meta::drawingLScore),
    GOALS("进球数", 5,
            new String[]{"0球", "1球", "2球", "3球", "4球", "5球", "6球", "7+球"},
            new String[]{"s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7",},
            Meta::drawingGoals),
    AWDL("半全场", 6,
            new String[]{"胜胜", "胜平", "胜负", "平胜", "平平", "平负", "负胜", "负平", "负负"},
            new String[]{"hh", "hd", "ha", "dh", "dd", "da", "ah", "ad", "aa",},
            Meta::drawingAwdl),
    ;
    private String desc;
    //单元格行数，从0开始
    private Integer rowNumber;
    //label名称
    private String[] labelArr;
    //远程取值属性
    private String[] propertyArr;

    private TripleConsumer<JXCollapsiblePane, List<JPanel>, String[]> callback;

    Meta(String desc, Integer rowNumber, String[] labelArr, String[] propertyArr, TripleConsumer<JXCollapsiblePane, List<JPanel>, String[]> callback) {
        this.desc = desc;
        this.rowNumber = rowNumber;
        this.labelArr = labelArr;
        this.propertyArr = propertyArr;
        this.callback = callback;
    }

    public static void drawingAwdl(JXCollapsiblePane cp, List<JPanel> cells, String[] labels) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints full = GridUtil.full();
        //半场胜平负
        for (int i = 1; i <= 13; i++) {
            JPanel panel = new JPanel(gridBagLayout);
            cells.add(panel);
            JTextField textField = new JTextField();
            textField.setEditable(false);
            String labelText = "";
            if (i > 9) {
                panel.setVisible(false);
                cp.add(textField);
            } else {
                labelText = labels[i - 1];
            }
            GridBagConstraints gbc = (GridBagConstraints) full.clone();
            JLabel label = new JLabel(" " + labelText);
            label.setForeground(Color.gray);
            panel.add(label, gbc);
            gbc.gridy = 1;
            panel.add(textField, gbc);
            cp.add(panel);
        }
    }

    public static void drawingGoals(JXCollapsiblePane cp, List<JPanel> cells, String[] labels) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints full = GridUtil.full();
        //总进球数
        for (int i = 1; i <= 13; i++) {
            JPanel panel = new JPanel(gridBagLayout);
            cells.add(panel);
            JTextField textField = new JTextField();
            textField.setEditable(false);
            String labelText = "";
            if (i > 8) {
                panel.setVisible(false);
            } else {
                labelText = labels[i - 1];
            }
            GridBagConstraints gbc = (GridBagConstraints) full.clone();
            JLabel label = new JLabel(" " + labelText);
            label.setForeground(Color.gray);
            panel.add(label, gbc);
            gbc.gridy = 1;
            panel.add(textField, gbc);
            cp.add(panel);
        }
    }

    public static void drawingLScore(JXCollapsiblePane cp, List<JPanel> cells, String[] labels) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints full = GridUtil.full();
        //比分负
        for (int i = 1; i <= 13; i++) {
            JPanel panel = new JPanel(gridBagLayout);
            cells.add(panel);
            JTextField textField = new JTextField();
            textField.setEditable(false);
            GridBagConstraints gbc = (GridBagConstraints) full.clone();
            JLabel label = new JLabel(" " + labels[i - 1]);
            label.setForeground(Color.gray);
            panel.add(label, gbc);
            gbc.gridy = 1;
            panel.add(textField, gbc);
            cp.add(panel);
        }
    }

    public static void drawingDScore(JXCollapsiblePane cp, List<JPanel> cells, String[] labels) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints full = GridUtil.full();
        //比分平
        for (int i = 1; i <= 13; i++) {
            JPanel panel = new JPanel(gridBagLayout);
            cells.add(panel);
            JTextField textField = new JTextField();
            textField.setEditable(false);
            String labelText = "";
            if (i > 5) {
                panel.setVisible(false);
            } else {
                labelText = labels[i - 1];
            }
            GridBagConstraints gbc = (GridBagConstraints) full.clone();
            JLabel label = new JLabel(" " + labelText);
            label.setForeground(Color.gray);
            panel.add(label, gbc);
            gbc.gridy = 1;
            panel.add(textField, gbc);
            cp.add(panel);
        }
    }

    public static void drawingWScore(JXCollapsiblePane cp, List<JPanel> cells, String[] labels) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints full = GridUtil.full();
        //比分胜
        for (int i = 1; i <= 13; i++) {
            JPanel panel = new JPanel(gridBagLayout);
            cells.add(panel);
            JTextField textField = new JTextField();
            textField.setEditable(false);
            GridBagConstraints gbc = (GridBagConstraints) full.clone();
            JLabel label = new JLabel(" " + labels[i - 1]);
            label.setForeground(Color.gray);
            panel.add(label, gbc);
            gbc.gridy = 1;
            panel.add(textField, gbc);
            cp.add(panel);
        }
    }

    public static void drawingHwdl(JXCollapsiblePane cp, List<JPanel> cells, String[] labels) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints full = GridUtil.full();
        //让球胜平负
        for (int i = 1; i <= 13; i++) {
            JPanel panel = new JPanel(gridBagLayout);
            cells.add(panel);
            JTextField textField = new JTextField();
            textField.setEditable(false);
            String labelText = "";
            if (i > 4) {
                panel.setVisible(false);
            } else if (i == 4) {
                textField.setForeground(Color.GRAY);
                textField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            }
            if (i <= 4) {
                labelText = labels[i - 1];
            }
            GridBagConstraints gbc = (GridBagConstraints) full.clone();
            JLabel label = new JLabel(" " + labelText);
            label.setForeground(Color.gray);
            panel.add(label, gbc);
            gbc.gridy = 1;
            panel.add(textField, gbc);
            cp.add(panel);
        }
    }

    public static void drawingWdl(JXCollapsiblePane cp, List<JPanel> cells, String[] labels) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints full = GridUtil.full();
        //胜平负
        for (int i = 1; i <= 13; i++) {
            JPanel panel = new JPanel(gridBagLayout);
            cells.add(panel);
            JTextField textField = new JTextField();
            textField.setEditable(false);
            String labelText = "";
            if (i > 3) {
                panel.setVisible(false);
            } else {
                labelText = labels[i - 1];
            }
            GridBagConstraints gbc = (GridBagConstraints) full.clone();
            JLabel label = new JLabel(" " + labelText);
            label.setForeground(Color.gray);
            panel.add(label, gbc);
            gbc.gridy = 1;
            panel.add(textField, gbc);
            cp.add(panel);
        }
    }

    public String getDesc() {
        return desc;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public String[] getLabelArr() {
        return labelArr;
    }

    public String[] getPropertyArr() {
        return propertyArr;
    }

    public TripleConsumer<JXCollapsiblePane, List<JPanel>, String[]> getCallback() {
        return callback;
    }

    @FunctionalInterface
    public interface TripleConsumer<P1, P2, P3> {
        void apply(P1 p1, P2 p2, P3 p3);
    }
}
