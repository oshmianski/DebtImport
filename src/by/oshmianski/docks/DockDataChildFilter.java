package by.oshmianski.docks;

import by.oshmianski.docks.Setup.DockSimple;
import by.oshmianski.utils.IconContainer;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: oshmianski
 * Date: 15.09.13
 * Time: 22:14
 */
public class DockDataChildFilter extends DockSimple {
    public DockDataChildFilter() {
        super("DockDataChildFilter", IconContainer.getInstance().loadImage("funnel.png"), "Фильтр расшифровки");

        panel.setBorder(BorderFactory.createEmptyBorder(2, 3, 2, 0));
    }
}
