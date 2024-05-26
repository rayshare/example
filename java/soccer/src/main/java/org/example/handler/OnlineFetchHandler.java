package org.example.handler;

import org.example.frame.OnlineFrame;
import org.example.jc.TodayData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class OnlineFetchHandler extends BaseEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OnlineFetchHandler.class);
    private static volatile boolean opened = false;

    private static synchronized void lock() {
        opened = true;
    }

    private static synchronized void release() {
        opened = false;
    }

    @Override
    public String handle(ButtonEvent buttonEvent) {
        if (opened) {
            return null;
        }
        //获取数据
        SwingUtilities.invokeLater(() -> {
            TodayData todayData = new TodayData();
            log.info("Start create frame");
            OnlineFrame frame = new OnlineFrame(buttonEvent.getMainFrame(), todayData.getData().size() + "场", todayData);
            frame.setPreferredSize(new Dimension(1100, 460));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
            lock();
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    release();
                }
            });
            log.info("frame started");
        });
        return null;
    }
}
