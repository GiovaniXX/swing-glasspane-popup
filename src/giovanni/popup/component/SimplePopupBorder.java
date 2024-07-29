package giovanni.popup.component;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import net.miginfocom.swing.MigLayout;
import giovanni.popup.GlassPanePopup;

public class SimplePopupBorder extends GlassPaneChild {

    private final Component component;

    private JPanel panelTitle;

    private final String title;

    private final String[] action;

    private SimplePopupBorderOption option;

    public SimplePopupBorder(Component component, String title, SimplePopupBorderOption option) {
        this(component, title, option, (String[]) null, (PopupCallbackAction) null);
    }

    public SimplePopupBorder(Component component, String title) {
        this(component, title, (String[]) null, (PopupCallbackAction) null);
    }

    public SimplePopupBorder(Component component, String title, String[] action, PopupCallbackAction callbackAction) {
        this(component, title, new SimplePopupBorderOption(), action, callbackAction);
    }

    public SimplePopupBorder(Component component, String title, SimplePopupBorderOption option, String[] action, PopupCallbackAction callbackAction) {
        this.component = component;
        this.title = title;
        this.option = option;
        this.action = action;
        this.callbackAction = callbackAction;
        if (component instanceof GlassPaneChild) {
            ((GlassPaneChild) component).callbackAction = callbackAction;
        }
        init();
    }

    private void init() {
        setLayout((LayoutManager) new MigLayout("wrap,fillx,insets 15 0 15 0", "[fill," + this.option.width + "]"));
        this.panelTitle = new JPanel((LayoutManager) new MigLayout("insets 2 25 2 25,fill"));
        if (this.title != null) {
            this.panelTitle.add(createTitle(this.title), "push");
        }
        this.panelTitle.add(createCloseButton(), "trailing");
        add(this.panelTitle);
        if (this.option.useScroll) {
            JScrollPane scrollPane = new JScrollPane(this.component);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            applyScrollStyle(scrollPane.getVerticalScrollBar());
            applyScrollStyle(scrollPane.getHorizontalScrollBar());
            add(scrollPane);
        } else {
            add(this.component);
        }
        if (this.action != null) {
            add(createActionButton(this.action, this.callbackAction));
        }
    }

    private void applyScrollStyle(JScrollBar scrollBar) {
        scrollBar.setUnitIncrement(10);
        scrollBar.putClientProperty("FlatLaf.style", "width:10;trackArc:999;thumbInsets:0,3,0,3;trackInsets:0,3,0,3;");
    }

    protected Component createTitle(String title) {
        JLabel lbTitle = new JLabel(title);
        lbTitle.putClientProperty("FlatLaf.style", "font:+4");
        return lbTitle;
    }

    protected void addBackButton() {
        this.panelTitle.add(createBackButton(), 0);
    }

    protected Component createCloseButton() {
        JButton cmdClose = new JButton((Icon) new FlatSVGIcon("giovanni/popup/icon/close.svg", 0.8F));
        cmdClose.putClientProperty("FlatLaf.style", "arc:999;borderWidth:0;focusWidth:0;innerFocusWidth:0;background:null");
        applyCloseButtonEvent(cmdClose);
        return cmdClose;
    }

    protected Component createBackButton() {
        JButton cmdBack = new JButton((Icon) new FlatSVGIcon("giovanni/popup/icon/back.svg", 0.8F));
        cmdBack.putClientProperty("FlatLaf.style", "arc:999;borderWidth:0;focusWidth:0;innerFocusWidth:0;background:null");
        applyBackButtonEvent(cmdBack);
        return cmdBack;
    }

    protected void applyCloseButtonEvent(JButton button) {
        button.addActionListener(e -> {
            if (this.callbackAction == null) {
                GlassPanePopup.closePopup(this);
                return;
            }
            PopupController action = createController();
            this.callbackAction.action(action, -1);
            if (!action.getConsume()) {
                GlassPanePopup.closePopup(this);
            }
        });
    }

    protected void applyBackButtonEvent(JButton button) {
        button.addActionListener(e -> GlassPanePopup.pop(this));
    }

    protected Component createActionButton(String[] action, PopupCallbackAction callbackAction) {
        JPanel panel = new JPanel((LayoutManager) new MigLayout("insets 2 25 2 25,trailing"));
        for (int i = 0; i < action.length; i++) {
            panel.add(getActionButton(action[i], i, callbackAction));
        }
        return panel;
    }

    private JButton getActionButton(String name, int index, PopupCallbackAction callbackAction) {
        JButton button = new JButton(name);
        button.setFocusable(false);
        button.addActionListener(e -> callbackAction.action(createController(), index));
        button.putClientProperty("FlatLaf.style", "borderWidth:0;focusWidth:0;innerFocusWidth:0;background:null;font:+1");
        return button;
    }

    public void onPush() {
        addBackButton();
    }

    public int getRoundBorder() {
        return this.option.roundBorder;
    }
}
