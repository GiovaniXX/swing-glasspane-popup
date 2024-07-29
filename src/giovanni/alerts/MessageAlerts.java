package giovanni.alerts;

import java.awt.Component;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import net.miginfocom.swing.MigLayout;
import giovanni.popup.GlassPanePopup;
import giovanni.popup.component.PopupCallbackAction;

public class MessageAlerts {

    private static MessageAlerts instance;

    public static final int DEFAULT_OPTION = -1;

    public static final int YES_NO_OPTION = 0;

    public static final int YES_NO_CANCEL_OPTION = 1;

    public static final int OK_CANCEL_OPTION = 2;

    public static final int YES_OPTION = 0;

    public static final int NO_OPTION = 1;

    public static final int CANCEL_OPTION = 2;

    public static final int OK_OPTION = 0;

    public static final int CLOSED_OPTION = -1;

    public static MessageAlerts getInstance() {
        if (instance == null) {
            instance = new MessageAlerts();
        }
        return instance;
    }

    public void showMessage(String title, String message) {
        showMessage(title, message, MessageType.DEFAULT);
    }

    public void showMessage(String title, String message, MessageType messageType) {
        showMessage(title, message, messageType, -1, null);
    }

    public void showMessage(String title, String message, MessageType messageType, int option, PopupCallbackAction callback) {
        AlertsOption alertsOption = AlertsOption.getAlertsOption(messageType);
        GlassPanePopup.showPopup(new SimpleAlerts(createSimpleMessage(alertsOption, title, message), alertsOption, option, callback));
    }

    private Component createSimpleMessage(AlertsOption option, String title, String message) {
        JPanel panel = new JPanel((LayoutManager) new MigLayout("wrap,insets 0,center", "center"));
        panel.setOpaque(false);
        JLabel labelTitle = new JLabel(title);
        JTextPane textPane = new JTextPane();
        textPane.setOpaque(false);
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, 1);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        textPane.setEditable(false);
        textPane.setText(message);
        textPane.putClientProperty("FlatLaf.style", "border:5,25,5,25;[light]foreground:lighten(@foreground,30%);[dark]foreground:darken(@foreground,30%)");
        labelTitle.putClientProperty("FlatLaf.style", "font:bold +5");
        if (option.baseColor != null) {
            labelTitle.setForeground(option.baseColor);
        }
        panel.add(labelTitle);
        JScrollPane scrollPane = new JScrollPane(textPane);
        if (option.effectOption != null) {
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
        }
        scrollPane.setHorizontalScrollBarPolicy(31);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        applyScrollStyle(scrollPane.getVerticalScrollBar());
        applyScrollStyle(scrollPane.getHorizontalScrollBar());
        panel.add(scrollPane);
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
        return panel;
    }

    private void applyScrollStyle(JScrollBar scrollBar) {
        scrollBar.setUnitIncrement(10);
        scrollBar.putClientProperty("FlatLaf.style", "width:10;trackArc:999;thumbInsets:0,3,0,3;trackInsets:0,3,0,3;");
    }

    public enum MessageType {
        SUCCESS, ERROR, WARNING, DEFAULT;
    }
}
