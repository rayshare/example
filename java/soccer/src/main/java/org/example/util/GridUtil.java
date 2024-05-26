package org.example.util;

import java.awt.*;

public class GridUtil {

    static final Insets zeroPadding = new Insets(0, 0, 0, 0);

    public static GridBagConstraints full() {
        return new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, zeroPadding, 0, 0);
    }

    /**
     * 占据整行，浮动到最上方
     *
     * @param rowNumber 行数
     * @param isLast    是否是最后一行，不正确设置会导致，组件浮动到中心位置
     * @return
     */
    public static GridBagConstraints floorTopByRow(int rowNumber, boolean isLast) {
        double weighty = isLast ? 1 : 0;
        return new GridBagConstraints(0, rowNumber, 1, 1, 1, weighty,
                GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL,
                zeroPadding, 0, 0);
    }

}
