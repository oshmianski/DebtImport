package by.oshmianski.docks;

import by.oshmianski.docks.Setup.DockSimple;
import by.oshmianski.docks.Setup.DockingContainer;
import by.oshmianski.loaders.LoadTemplateImport;
import by.oshmianski.loaders.Loader;
import by.oshmianski.objects.TemplateImport;
import by.oshmianski.ui.utils.ActionButton;
import by.oshmianski.utils.IconContainer;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.DefaultEventComboBoxModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: oshmianski
 * Date: 15.09.13
 * Time: 22:14
 */
public class DockHeader extends DockSimple {
    private DockingContainer dockingContainer;

    private EventList<TemplateImport> templateImports = new BasicEventList<TemplateImport>();

    private JTextField fileField;
    private JTextField startFrom;
    private JTextField col2Description;
    private JButton bSelectTemplate;
    private JLabel loadLabel;
    private JComboBox template;

    private Loader loaderTI;

    public DockHeader(DockingContainer dockingContainer) {
        super("DockHeader", IconContainer.getInstance().loadImage("tools.png"), "Настройка импорта");

        this.dockingContainer = dockingContainer;

        loadLabel = new JLabel(IconContainer.getInstance().loadImage("loader.gif"));
        loadLabel.setVisible(false);

        bSelectTemplate = new ActionButton("", IconContainer.getInstance().loadImage("repeat.png"), new Dimension(20, 20), "Загрузка шаблонов");
        bSelectTemplate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loaderTI = new LoadTemplateImport(getDockingContainer().getUIProcessor());
                loaderTI.execute();
            }
        });

        JButton bOpenFile = new ActionButton("...", null, new Dimension(20, 20), "Выбрать файл импорта");

        DefaultEventComboBoxModel<TemplateImport> comboBoxModel = new DefaultEventComboBoxModel<TemplateImport>(templateImports);
        template = new JComboBox(comboBoxModel);
        fileField = new JTextField();
        startFrom = new JTextField("2");
        col2Description = new JTextField("A");

        final JFileChooser fileChooser = new JFileChooser();

        bOpenFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setFileFilter(new ExtFileFilter("xlsx", "*.xlsx - MS Excel 2007"));

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    fileField.setText(file.getAbsolutePath());
                }
            }
        });

        FormLayout layout = new FormLayout(
                "5px, right:60px, 5px, 300px, 5px, 20px, 5px, 30px, 5px, right:130px, 5px, 60px, 5px", // columns
                "5px, 30px, 5px, 30px, pref");      // rows

        PanelBuilder builder = new PanelBuilder(layout);
//        builder.setDefaultDialogBorder();

        // Obtain a reusable constraints object to place components in the grid.
        CellConstraints cc = new CellConstraints();

//        builder.addSeparator("Импорт", cc.xyw(1, 1, 6));
        builder.addLabel("Шаблон", cc.xy(2, 2));
        builder.add(template, cc.xy(4, 2));
        builder.add(bSelectTemplate, cc.xy(6, 2));
        builder.add(loadLabel, cc.xy(8, 2));
        builder.addLabel("Начинать со строки", cc.xy(10, 2));
        builder.add(startFrom, cc.xy(12, 2));
        builder.addLabel("Файл", cc.xy(2, 4));
        builder.add(fileField, cc.xy(4, 4));
        builder.add(bOpenFile, cc.xy(6, 4));
        builder.addLabel("Колонка в описание", cc.xy(10, 4));
        builder.add(col2Description, cc.xy(12, 4));

        panel.add(builder.getPanel());
    }

    public JTextField getFileField() {
        return fileField;
    }

    private static class ExtFileFilter extends FileFilter {
        String ext;
        String description;

        public ExtFileFilter(String ext, String descr) {
            this.ext = ext;
            description = descr;
        }

        public boolean accept(File f) {
            if (f != null) {
                if (f.isDirectory()) {
                    return true;
                }
                String extension = getExtension(f);
                if (extension == null)
                    return (ext.length() == 0);
                return ext.equals(extension);
            }
            return false;
        }

        public String getExtension(File f) {
            if (f != null) {
                String filename = f.getName();
                int i = filename.lastIndexOf('.');
                if (i > 0 && i < filename.length() - 1) {
                    return filename.substring(i + 1).toLowerCase();
                }
            }
            return null;
        }

        public String getDescription() {
            return description;
        }
    }

    public void setTemplateImports(EventList<TemplateImport> templateImports) {
        this.templateImports.clear();
        this.templateImports.addAll(templateImports);
    }

    public void startLoadTI() {
        bSelectTemplate.setEnabled(false);
        loadLabel.setVisible(true);
    }

    public void stopLoadTI() {
        bSelectTemplate.setEnabled(true);
        loadLabel.setVisible(false);
    }

    public DockingContainer getDockingContainer() {
        return dockingContainer;
    }

    public TemplateImport getTemplateImport(){
        return (TemplateImport)template.getSelectedItem();
    }

    public void dispose(){
        System.out.println("DockHeader clear...");
        templateImports.dispose();
        System.out.println("DockHeader clear...OK");
    }

    public JTextField getStartFrom() {
        return startFrom;
    }

    public JTextField getCol2Description() {
        return col2Description;
    }
}
