package org.example.handler;

import org.example.frame.MainFrame;

public class ButtonEvent {
    private String code;
    private MainFrame mainFrame;

    public ButtonEvent(String code, MainFrame mainFrame) {
        this.code = code;
        this.mainFrame = mainFrame;
    }

    public String getCode() {
        return code;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public String toString() {
        return "ButtonEvent{" +
                "code='" + code + '\'' +
                '}';
    }
}
