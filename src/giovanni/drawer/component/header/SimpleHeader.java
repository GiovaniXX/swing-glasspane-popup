package giovanni.drawer.component.header;

import java.awt.LayoutManager;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import giovanni.utils.FlatLafStyleUtils;

public class SimpleHeader extends JPanel {

    private SimpleHeaderData simpleHeaderData;

    private JLabel profile;

    private JLabel labelTitle;

    private JLabel labelDescription;

    public SimpleHeader(SimpleHeaderData simpleHeaderData) {
        this.simpleHeaderData = simpleHeaderData;
        init();
    }

    private void init() {
        setLayout((LayoutManager) new MigLayout("wrap,insets 10 20 5 20,fill,gap 3"));
        this.profile = new JLabel(this.simpleHeaderData.icon);
        this.labelTitle = new JLabel(this.simpleHeaderData.title);
        this.labelDescription = new JLabel(this.simpleHeaderData.description);
        if (this.simpleHeaderData.simpleHeaderStyle != null) {
            this.simpleHeaderData.simpleHeaderStyle.styleHeader(this);
            this.simpleHeaderData.simpleHeaderStyle.styleProfile(this.profile);
            this.simpleHeaderData.simpleHeaderStyle.styleTitle(this.labelTitle);
            this.simpleHeaderData.simpleHeaderStyle.styleDescription(this.labelDescription);
        }
        FlatLafStyleUtils.appendStyleIfAbsent(this, "background:null");
        FlatLafStyleUtils.appendStyleIfAbsent(this.profile, "background:$Component.borderColor");
        FlatLafStyleUtils.appendStyleIfAbsent(this.labelDescription, "font:-1;[light]foreground:lighten(@foreground,30%);[dark]foreground:darken(@foreground,30%)");
        add(this.profile);
        add(this.labelTitle);
        add(this.labelDescription);
    }

    public SimpleHeaderData getSimpleHeaderData() {
        return this.simpleHeaderData;
    }

    public void setSimpleHeaderData(SimpleHeaderData simpleHeaderData) {
        this.simpleHeaderData = simpleHeaderData;
        this.profile.setIcon(simpleHeaderData.icon);
        this.labelTitle.setText(simpleHeaderData.title);
        this.labelDescription.setText(simpleHeaderData.description);
    }
}
