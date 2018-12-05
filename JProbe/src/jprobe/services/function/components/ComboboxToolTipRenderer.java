package jprobe.services.function.components;

import java.awt.Component;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;

public class ComboboxToolTipRenderer extends DefaultListCellRenderer {
    List<String> tooltips;

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                        int index, boolean isSelected, boolean cellHasFocus) {

        JComponent comp = (JComponent) super.getListCellRendererComponent(list,
                value, index, isSelected, cellHasFocus);

        if (-1 < index && null != value && null != tooltips && index<tooltips.size()) {
            list.setToolTipText(tooltips.get(index));
        }
        return comp; 	
    }

    public void setTooltips(List<String> tooltips) {
        this.tooltips = tooltips;
    }
}