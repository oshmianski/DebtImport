package by.oshmianski.docks;

import by.oshmianski.docks.Setup.DockSimple;
import by.oshmianski.docks.Setup.DockingContainer;
import by.oshmianski.loaders.LoadTemplateImport;
import by.oshmianski.loaders.Loader;
import by.oshmianski.main.AppletWindow;
import by.oshmianski.objects.CellHeader;
import by.oshmianski.objects.TemplateImport;
import by.oshmianski.ui.utils.ActionButton;
import by.oshmianski.ui.utils.XTableColumnModel;
import by.oshmianski.ui.utils.niceScrollPane.NiceScrollPane;
import by.oshmianski.utils.IconContainer;
import by.oshmianski.utils.MyLog;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import ca.odell.glazedlists.swing.DefaultEventComboBoxModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
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
    private EventList<CellHeader> cellHeaders = new BasicEventList<CellHeader>();

    private JTextField fileField;
    private JTextField startFrom;
    private JTextField endTo;
    private JTextField col2Description;
    private JButton bSelectTemplate;
    private JLabel loadLabel;
    private JComboBox template;
    private JComboBox headers;

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

        cellHeaders.add(new CellHeader("", ""));

        DefaultEventComboBoxModel<TemplateImport> comboBoxModel = new DefaultEventComboBoxModel<TemplateImport>(templateImports);
        DefaultEventComboBoxModel<CellHeader> cellHeaderComboBoxModel = new DefaultEventComboBoxModel<CellHeader>(cellHeaders);

        template = new JComboBox(comboBoxModel);
        headers = new JComboBox(cellHeaderComboBoxModel);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AutoCompleteSupport supportOrg = AutoCompleteSupport.install(headers, cellHeaders);
                supportOrg.setFilterMode(TextMatcherEditor.CONTAINS);
                supportOrg.setStrict(true);
            }
        });

        fileField = new JTextField();
        startFrom = new JTextField("2");
        endTo = new JTextField("");
        col2Description = new JTextField("A");

        final JFileChooser fileChooser = new JFileChooser();

        bOpenFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setFileFilter(new ExtFileFilter("xlsx", "*.xlsx - MS Excel 2007"));

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    fileField.setText(file.getAbsolutePath());

                    OPCPackage pkg = null;
                    XSSFWorkbook wb = null;

                    try {
                        // XSSFWorkbook, File
                        pkg = OPCPackage.open(file, PackageAccess.READ);
                        wb = new XSSFWorkbook(pkg);
                        Sheet sheet1 = wb.getSheetAt(0);
                        Row row = sheet1.getRow(0);
                        int lastCol = row.getLastCellNum();

                        cellHeaders.clear();

                        for (int cn = 0; cn < lastCol; cn++) {
                            Cell c = row.getCell(cn, Row.RETURN_BLANK_AS_NULL);

                            if (c == null) {
                                cellHeaders.add(new CellHeader(CellReference.convertNumToColString(cn), ""));
                            } else {
                                cellHeaders.add(new CellHeader(CellReference.convertNumToColString(cn), c.getStringCellValue()));
                            }
                        }
                    } catch (Exception ex) {
                        MyLog.add2Log(ex);
                    } finally {
                        try {
                            if (pkg != null) {
                                pkg.close();
                            }
                        } catch (Exception e1) {
                            MyLog.add2Log(e1);
                        }
                    }
                }
            }
        });

        FormLayout layout = new FormLayout(
                "5px, right:60px, 5px, 300px, 5px, 20px, 5px, 30px, 5px, right:130px, 5px, 60px, 5px, right:130px, 5px, 60px, 5px", // columns
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
        builder.addLabel("Закончить строкой", cc.xy(14, 2));
        builder.add(endTo, cc.xy(16, 2));
        builder.addLabel("Файл", cc.xy(2, 4));
        builder.add(fileField, cc.xy(4, 4));
        builder.add(bOpenFile, cc.xy(6, 4));
        builder.addLabel("Колонка в описание", cc.xy(10, 4));
        builder.add(headers, cc.xyw(12, 4, 5));

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

        SortedList<TemplateImport> templateImportSortedList = new SortedList<TemplateImport>(templateImports, GlazedLists.chainComparators(GlazedLists.beanPropertyComparator(TemplateImport.class, "num")));
        for (TemplateImport templateImport : templateImportSortedList){
            this.templateImports.add(templateImport);
        }
        templateImportSortedList.dispose();
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

    public TemplateImport getTemplateImport() {
        return (TemplateImport) template.getSelectedItem();
    }

    public void dispose() {
        System.out.println("DockHeader clear...");
        templateImports.dispose();
        cellHeaders.dispose();
        System.out.println("DockHeader clear...OK");
    }

    public JTextField getStartFrom() {
        return startFrom;
    }

    public JTextField getEndTo() {
        return endTo;
    }

    public String getCol2Description() {
        if (headers.getSelectedItem() == null) return "";
        return ((CellHeader) headers.getSelectedItem()).getColTitle();
    }

    public EventList<CellHeader> getCellHeaders() {
        return cellHeaders;
    }
}
