package giovanni.drawer.component.footer;

import java.awt.LayoutManager;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import giovanni.utils.FlatLafStyleUtils;

public class SimpleFooter extends JPanel {

    private SimpleFooterData simpleFooterData;

    private JLabel labelTitle;

    private JLabel labelDescription;

    public SimpleFooter(SimpleFooterData simpleFooterData) {
        this.simpleFooterData = simpleFooterData;
        init();
    }

    private void init() {
        setLayout((LayoutManager) new MigLayout("wrap,insets 5 20 10 20,fill,gap 3"));
        this.labelTitle = new JLabel(this.simpleFooterData.title);
        this.labelDescription = new JLabel(this.simpleFooterData.description);
        if (this.simpleFooterData.simpleFooterStyle != null) {
            this.simpleFooterData.simpleFooterStyle.styleFooter(this);
            this.simpleFooterData.simpleFooterStyle.styleTitle(this.labelTitle);
            this.simpleFooterData.simpleFooterStyle.styleDescription(this.labelDescription);
        }
        FlatLafStyleUtils.appendStyleIfAbsent(this, "background:null");
        FlatLafStyleUtils.appendStyleIfAbsent(this.labelDescription, "font:-1;[light]foreground:lighten(@foreground,30%);[dark]foreground:darken(@foreground,30%)");
        add(this.labelTitle);
        add(this.labelDescription);
    }

    public SimpleFooterData getSimpleFooterData() {
        return this.simpleFooterData;
    }

    public void setSimpleFooterData(SimpleFooterData simpleFooterData) {
        this.simpleFooterData = simpleFooterData;
        this.labelTitle.setText(simpleFooterData.title);
        this.labelDescription.setText(simpleFooterData.description);
    }
}
