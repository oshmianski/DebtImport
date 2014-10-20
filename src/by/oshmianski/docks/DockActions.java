package by.oshmianski.docks;

import by.oshmianski.docks.Setup.DockSimple;
import by.oshmianski.docks.Setup.DockingContainer;
import by.oshmianski.loaders.LoadImportData;
import by.oshmianski.loaders.Loader;
import by.oshmianski.ui.utils.ActionButton;
import by.oshmianski.utils.IconContainer;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: oshmianski
 * Date: 15.09.13
 * Time: 22:14
 */
public class DockActions extends DockSimple {
    private DockingContainer dockingContainer;

    private LoadImportData loader;

    private JButton bTestStart;
    private JButton bTestStop;
    private JButton bImportStart;
    private JButton bImportStop;

    public DockActions(final DockingContainer dockingContainer) {
        super("DockActions", IconContainer.getInstance().loadImage("actions.png"), "Действия");

        this.dockingContainer = dockingContainer;

        bTestStart = new ActionButton("Старт", null, new Dimension(90, 30), "Запусть тест импорта");
        bTestStop = new ActionButton("Стоп", null, new Dimension(90, 30), "Остановить тест импорта");
        bImportStart = new ActionButton("Старт", null, new Dimension(90, 30), "Запустить импорт");
        bImportStop = new ActionButton("Стоп", null, new Dimension(90, 30), "Остановить импорт");

        bTestStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getDockingContainer().getUIProcessor().isHeaderCorrect()) {
                    loader = dockingContainer.getLoader();
                    loader.setTest(true);
                    loader.execute();
                }
            }
        });

        bTestStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loader.cancel();
            }
        });

        bImportStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getDockingContainer().getUIProcessor().isHeaderCorrect()) {
                    loader = dockingContainer.getLoader();
                    loader.setTest(false);
                    loader.execute();
                }
            }
        });

        bImportStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loader.cancel();
            }
        });

        bTestStop.setEnabled(false);
//        bImportStart.setEnabled(false);
        bImportStop.setEnabled(false);

        FormLayout layout = new FormLayout(
                "5px, center:100px, 5px, left:pref:grow, 5px", // columns
                "5px, 30px, 30px, 5px, 30px, 30px, 5px");      // rows

        PanelBuilder builder = new PanelBuilder(layout);
//        builder.setDefaultDialogBorder();

        // Obtain a reusable constraints object to place components in the grid.
        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Тест", cc.xyw(2, 2, 3));
        builder.add(bTestStart, cc.xy(2, 3));
        builder.add(bTestStop, cc.xy(4, 3));
        builder.addSeparator("Импорт", cc.xyw(2, 5, 3));
        builder.add(bImportStart, cc.xy(2, 6));
        builder.add(bImportStop, cc.xy(4, 6));

        panel.add(builder.getPanel());
    }

    public void setButtomTestStartEnable(boolean enabled) {
        bTestStart.setEnabled(enabled);
    }

    public void setButtomTestStopEnable(boolean enabled) {
        bTestStop.setEnabled(enabled);
    }

    public void setButtomImportStartEnable(boolean enabled) {
        bImportStart.setEnabled(enabled);
    }

    public void setButtomImportStopEnable(boolean enabled) {
        bImportStop.setEnabled(enabled);
    }

    private DockingContainer getDockingContainer() {
        return dockingContainer;
    }

    private boolean isCanImport() {
        return getDockingContainer().getUIProcessor().isCanImport();
    }
}
