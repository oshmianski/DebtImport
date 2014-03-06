package by.oshmianski.docks;

import by.oshmianski.category.datachild.*;
import by.oshmianski.docks.Setup.DockSimple;
import by.oshmianski.docks.Setup.DockingContainer;
import by.oshmianski.filter.CM.FilterPanelChild;
import by.oshmianski.main.AppletWindow;
import by.oshmianski.models.AddressModel;
import by.oshmianski.models.AddressParserModel;
import by.oshmianski.models.DataChildModel;
import by.oshmianski.objects.Address;
import by.oshmianski.objects.DataChildItem;
import by.oshmianski.objects.DataMainItem;
import by.oshmianski.objects.addressParser.AddressItem;
import by.oshmianski.objects.addressParser.AddressParser;
import by.oshmianski.objects.addressParser.AddressParserItem;
import by.oshmianski.objects.addressParser.AddressParserOperation;
import by.oshmianski.ui.utils.*;
import by.oshmianski.ui.utils.niceScrollPane.NiceScrollPane;
import by.oshmianski.utils.IconContainer;
import by.oshmianski.utils.MyLog;
import ca.odell.glazedlists.*;
import ca.odell.glazedlists.matchers.Matchers;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import ca.odell.glazedlists.swing.TreeTableSupport;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: oshmianski
 * Date: 15.09.13
 * Time: 22:14
 */
public class DockAddressParser extends DockSimple {
    private DockingContainer dockingContainer;
    private EventList<AddressParserItem> items = new BasicEventList<AddressParserItem>();
    private EventList<AddressItem> itemsA = new BasicEventList<AddressItem>();

    private DefaultEventTableModel model;
    private DefaultEventTableModel modelA;
    private EventList<AddressParserItem> entries;
    private SortedList<AddressParserItem> sortedEntries;
    private JTable table;
    private JTable tableA;
    private JLabel realStr;
    private JLabel realStrExcluded;
    private JLabel realStr_;

    private final static String dockTitle = "Матрица адреса";

    public DockAddressParser(DockingContainer dockingContainer) {
        super("DockAddressParser", IconContainer.getInstance().loadImage("layers.png"), dockTitle);

        this.dockingContainer = dockingContainer;

        table = new BetterJTable(null, true);
        tableA = new BetterJTable(null, true);

        realStr = new JLabel("");
        realStr.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        realStr.setForeground(Color.BLUE);
        realStr.setFont(new Font("Tahoma", Font.PLAIN, 10));
        realStrExcluded = new JLabel("");
        realStrExcluded.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        realStrExcluded.setForeground(Color.BLUE);
        realStrExcluded.setFont(new Font("Tahoma", Font.PLAIN, 10));
        realStr_ = new JLabel("");
        realStr_.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        realStr_.setForeground(Color.BLUE);
        realStr_.setFont(new Font("Tahoma", Font.PLAIN, 10));

        JScrollPane sp;
        JScrollPane spA;

        items.getReadWriteLock().writeLock().lock();

        try {
            entries = GlazedListsSwing.swingThreadProxyList(items);

            sortedEntries = new SortedList<AddressParserItem>(entries);

            model = new DefaultEventTableModel(sortedEntries, new AddressParserModel());

            table.setModel(model);

            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.getColumnModel().getColumn(0).setPreferredWidth(50);
            table.getColumnModel().getColumn(1).setPreferredWidth(130);
            table.getColumnModel().getColumn(2).setPreferredWidth(85);
            table.getColumnModel().getColumn(3).setPreferredWidth(150);
            table.getColumnModel().getColumn(4).setPreferredWidth(50);
            table.getColumnModel().getColumn(5).setPreferredWidth(50);
            table.getColumnModel().getColumn(6).setPreferredWidth(60);
            table.getColumnModel().getColumn(7).setPreferredWidth(75);
            table.getColumnModel().getColumn(8).setPreferredWidth(75);
            table.getColumnModel().getColumn(9).setPreferredWidth(150);
            table.getColumnModel().getColumn(10).setPreferredWidth(150);
            table.getColumnModel().getColumn(11).setPreferredWidth(300);

            table.getColumnModel().getColumn(0).setCellRenderer(new ColorRenderer(Color.BLUE, false));
            table.getColumnModel().getColumn(1).setCellRenderer(new ColorRenderer(Color.BLACK, false));
            table.getColumnModel().getColumn(2).setCellRenderer(new BooleanRenderer(Color.BLUE, false));
            table.getColumnModel().getColumn(3).setCellRenderer(new NotBooleanRenderer(Color.BLACK, false));
            table.getColumnModel().getColumn(4).setCellRenderer(new ColorRenderer(Color.BLUE, false));
            table.getColumnModel().getColumn(5).setCellRenderer(new ColorRenderer(Color.BLACK, false));
            table.getColumnModel().getColumn(6).setCellRenderer(new ColorRenderer(Color.BLUE, false));
            table.getColumnModel().getColumn(7).setCellRenderer(new ColorRenderer(Color.BLACK, false));
            table.getColumnModel().getColumn(8).setCellRenderer(new ColorRenderer(Color.BLUE, false));
            table.getColumnModel().getColumn(9).setCellRenderer(new ColorRenderer(Color.BLACK, false));
            table.getColumnModel().getColumn(10).setCellRenderer(new ColorRenderer(Color.BLUE, false));
            table.getColumnModel().getColumn(11).setCellRenderer(new ColorRenderer(Color.BLACK, false));

            table.setRowHeight(20);
            table.setShowHorizontalLines(true);
            table.setShowVerticalLines(true);
            table.setIntercellSpacing(new Dimension(1, 1));
            table.setGridColor(AppletWindow.DATA_TABLE_GRID_COLOR);
            table.setSelectionBackground(new Color(217, 235, 245));
            table.setSelectionForeground(Color.BLACK);

            modelA = new DefaultEventTableModel(itemsA, new AddressModel());

            tableA.setModel(modelA);
            tableA.setRowHeight(20);
            tableA.setShowHorizontalLines(true);
            tableA.setShowVerticalLines(true);
            tableA.setIntercellSpacing(new Dimension(1, 1));
            tableA.setGridColor(AppletWindow.DATA_TABLE_GRID_COLOR);
            tableA.setSelectionBackground(new Color(217, 235, 245));
            tableA.setSelectionForeground(Color.BLACK);

            tableA.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tableA.getColumnModel().getColumn(0).setPreferredWidth(150);
            tableA.getColumnModel().getColumn(1).setPreferredWidth(150);
            tableA.getColumnModel().getColumn(2).setPreferredWidth(300);

            tableA.getColumnModel().getColumn(0).setCellRenderer(new ColorRenderer(Color.BLUE, false));
            tableA.getColumnModel().getColumn(1).setCellRenderer(new ColorRenderer(Color.BLACK, false));
            tableA.getColumnModel().getColumn(2).setCellRenderer(new ColorRenderer(Color.BLUE, false));

            sp = new NiceScrollPane(table);
            spA = new NiceScrollPane(tableA);

            sp.setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);
            sp.getViewport().setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);
            spA.setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);
            spA.getViewport().setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);

            JPanel pTables = new JPanel(new BorderLayout());
            pTables.setBackground(AppletWindow.REPORT_PANEL_BACKGROUND);

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp, spA);
            splitPane.setResizeWeight(0.5);
            splitPane.setOneTouchExpandable(true);
            splitPane.setContinuousLayout(true);
            splitPane.setBorder(BorderFactory.createEmptyBorder());

            pTables.add(splitPane);
            pTables.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(2, 2, 2, 2),
                    BorderFactory.createLineBorder(new Color(0x969696))
            ));

            panel.add(pTables);

            FormLayout layout = new FormLayout(
                    "5px, right:100px, 5px, left:pref, 5px", // columns
                    "2px, 13px, 1px, 13px, 1px, 13px, 2px");      // rows

            PanelBuilder builder = new PanelBuilder(layout);

            // Obtain a reusable constraints object to place components in the grid.
            CellConstraints cc = new CellConstraints();

            builder.addLabel("realStr:", cc.xy(2, 2));
            builder.add(realStr, cc.xy(4, 2));
            builder.addLabel("realStrExcluded:", cc.xy(2, 4));
            builder.add(realStrExcluded, cc.xy(4, 4));
            builder.addLabel("realStr_:", cc.xy(2, 6));
            builder.add(realStr_, cc.xy(4, 6));

            panel.add(builder.getPanel(), BorderLayout.NORTH);

        } catch (Exception e) {
            MyLog.add2Log(e);
        } finally {
            items.getReadWriteLock().writeLock().unlock();
        }
    }

    public void clearItems() {
        items.clear();
        itemsA.clear();
    }

    public void setAddressParserItems(DataMainItem dataMainItem) {
        Address address;
        AddressItem addressItem;

        if (dataMainItem.getAddressParser() != null) {
            ArrayList<AddressParserItem> addressParserItems = dataMainItem.getAddressParser().getParserItems();

            setTitleText(dockTitle + " :: " + dataMainItem.getDescription());

            setAddress(dataMainItem.getAddressParser());

            items.getReadWriteLock().writeLock().lock();
            try {
                if (addressParserItems == null || addressParserItems.size() == 0) {
                    items.clear();
                } else {
                    items.clear();
                    items.addAll(addressParserItems);
                }
            } catch (Exception e) {
                MyLog.add2Log(e);
            } finally {
                items.getReadWriteLock().writeLock().unlock();
            }

            itemsA.getReadWriteLock().writeLock().lock();
            try {
                if (addressParserItems == null || addressParserItems.size() == 0) {
                    itemsA.clear();
                } else {
                    itemsA.clear();

                    address = dataMainItem.getAddressParser().getAddress();

                    addressItem = new AddressItem("index", address.getIndex(), address.getIndexOperation());
                    itemsA.add(addressItem);

                    addressItem = new AddressItem("country", address.getCountry(), address.getCountryOperation());
                    itemsA.add(addressItem);

                    addressItem = new AddressItem("region", address.getRegion(), address.getRegionOperation());
                    itemsA.add(addressItem);

                    addressItem = new AddressItem("district", address.getDistrict(), address.getDistrictOperation());
                    itemsA.add(addressItem);

                    addressItem = new AddressItem("unit", address.getUnit(), address.getUnitOperation());
                    itemsA.add(addressItem);

                    addressItem = new AddressItem("cityType", address.getCityType(), address.getCityTypeOperation());
                    itemsA.add(addressItem);

                    addressItem = new AddressItem("city", address.getCity(), address.getCityOperation());
                    itemsA.add(addressItem);

                    addressItem = new AddressItem("streetType", address.getStreetType(), address.getStreetTypeOperation());
                    itemsA.add(addressItem);

                    addressItem = new AddressItem("street", address.getStreet(), address.getStreetOperation());
                    itemsA.add(addressItem);

                    addressItem = new AddressItem("house", address.getHouse(), address.getHouseOperation());
                    itemsA.add(addressItem);

                    addressItem = new AddressItem("building", address.getBuilding(), address.getBuildingOperation());
                    itemsA.add(addressItem);

                    addressItem = new AddressItem("flat", address.getFlat(), address.getFlatOperation());
                    itemsA.add(addressItem);

                    addressItem = new AddressItem("isProcessFull", address.isProcessedFull() + "", AddressParserOperation.UNKNOWN);
                    itemsA.add(addressItem);

                    addressItem = new AddressItem("isProcessFullNotService", address.isProcessedFullNotService() + "", AddressParserOperation.UNKNOWN);
                    itemsA.add(addressItem);

                }
            } catch (Exception e) {
                MyLog.add2Log(e);
            } finally {
                itemsA.getReadWriteLock().writeLock().unlock();
            }
        }
    }

    public void dispose() {
        System.out.println("DockAddressParser clear...");

        if (model != null) model = null;
        if (sortedEntries != null) sortedEntries.dispose();
        if (entries != null) entries.dispose();
        if (items != null) items.dispose();
        if (itemsA != null) itemsA.dispose();

        System.out.println("DockAddressParser clear...OK");
    }

    public void setAddress(AddressParser addressParser) {
        realStr.setText(addressParser.getRealStr());
        realStrExcluded.setText(addressParser.getRealStrExclusion());
        realStr_.setText(addressParser.getRealStr_());
    }
}
