package org.example.util;

import org.apache.commons.collections4.CollectionUtils;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.List;

public class XmlUtil {
    public static String parse(Element element, String xpath) {
        List<Node> nodes = element.selectNodes(xpath);
        if (CollectionUtils.isNotEmpty(nodes)) {
            return nodes.get(0).getText();
        }
        return null;
    }
}
