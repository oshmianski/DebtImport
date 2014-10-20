package by.oshmianski.docks;

import by.oshmianski.docks.Setup.DockSimple;
import by.oshmianski.utils.IconContainer;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: oshmianski
 * Date: 15.09.13
 * Time: 22:14
 */
public class DockInfo extends DockSimple {
    private JProgressBar progress;
    private JLabel errorRows;
    private JLabel warningRows;
    private JLabel okRows;
    private JLabel allRows;
    private JLabel allRows2Import;
    private JLabel allRowsImported;
    private JLabel progressLabel;
    private int errorRowsCount;
    private int warningRowsCount;
    private int okRowsCount;
    private int allRowsCount;
    private int allRows2ImportCount;
    private int allRowsImportedCount;

    public DockInfo() {
        super("DockInfo", IconContainer.getInstance().loadImage("info.png"), "Информация");

        allRows = new JLabel("0");
        allRows2Import = new JLabel("0");
        allRowsImported = new JLabel("0");
        okRows = new JLabel("0");
        warningRows = new JLabel("0");
        errorRows = new JLabel("0");
        progress = new JProgressBar();
        progress.setStringPainted(true);
        progressLabel = new JLabel("Описание процесса");
        progressLabel.setFont(new Font("Tahoma", Font.PLAIN, 9));
        progressLabel.setForeground(Color.DARK_GRAY);

        FormLayout layout = new FormLayout(
                "5px, right:pref:grow, 5px", // columns
                "20px, 15px, 20px, 15px, 20px, 15px, 20px, 15px, 20px, 15px, 20px, 15px, 20px, 10px, 30px");      // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        // Obtain a reusable constraints object to place components in the grid.
        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Строк для импорта", cc.xyw(1, 1, 3));
        builder.add(allRows2Import, cc.xy(2, 2));
        builder.addSeparator("Прочитано строк", cc.xyw(1, 3, 3));
        builder.add(allRows, cc.xy(2, 4));
        builder.addSeparator("Импортировано объектов", cc.xyw(1, 5, 3));
        builder.add(allRowsImported, cc.xy(2, 6));
        builder.addSeparator("OK", cc.xyw(1, 7, 3));
        builder.add(okRows, cc.xy(2, 8));
        builder.addSeparator("Warning", cc.xyw(1, 9, 3));
        builder.add(warningRows, cc.xy(2, 10));
        builder.addSeparator("Error", cc.xyw(1, 11, 3));
        builder.add(errorRows, cc.xy(2, 12));
        builder.addSeparator("Процесс", cc.xyw(1, 13, 3));
        builder.add(progressLabel, cc.xyw(1, 14, 3));
        builder.add(progress, cc.xyw(1, 15, 3));

        panel.add(builder.getPanel());
    }

    public void progressSetMaximum(int count) {
        progress.setMaximum(count);
    }

    public void progressSetValue(int value) {
        progress.setValue(value);
    }

    public void countClearAll() {
        allRows2Import.setText("0");
        allRowsImported.setText("0");
        allRows.setText("0");
        okRows.setText("0");
        warningRows.setText("0");
        errorRows.setText("0");

        allRows2ImportCount = 0;
        allRowsImportedCount = 0;
        allRowsCount = 0;
        okRowsCount = 0;
        warningRowsCount = 0;
        errorRowsCount = 0;
    }

    public void countIncAll() {
        allRowsCount++;
        allRows.setText(Integer.toString(allRowsCount));
    }

    public void countIncOk() {
        okRowsCount++;
        okRows.setText(Integer.toString(okRowsCount));
    }

    public void countIncImported() {
        allRowsImportedCount++;
        allRowsImported.setText(Integer.toString(allRowsImportedCount));
    }

    public void countIncWarning() {
        warningRowsCount++;
        warningRows.setText(Integer.toString(warningRowsCount));
    }

    public void countIncError() {
        errorRowsCount++;
        errorRows.setText(Integer.toString(errorRowsCount));
    }

    public boolean isError() {
        return Integer.valueOf(errorRows.getText()) > 0;
    }

    public boolean isWarning() {
        return Integer.valueOf(warningRows.getText()) > 0;
    }

    public void setCountAll2Import(int count) {
        allRows2Import.setText(Integer.toString(count));
    }

    public void setProgressLabelText(String text){
        progressLabel.setText(text);
    }
}
