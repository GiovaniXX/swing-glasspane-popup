package giovanni.popup;

import java.awt.Graphics;
import java.awt.image.VolatileImage;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

public class WindowSnapshots {

    private JFrame frame;

    private JDialog dialog;

    private JComponent snapshotLayer;

    private boolean inShowSnapshot;

    public WindowSnapshots(JFrame frame) {
        this.frame = frame;
    }

    public WindowSnapshots(JDialog dialog) {
        this.dialog = dialog;
    }

    public void createSnapshot() {
        if (this.inShowSnapshot) {
            return;
        }
        this.inShowSnapshot = true;
        if ((this.frame != null && this.frame.isShowing()) || (this.dialog != null && this.dialog.isShowing())) {
            final VolatileImage snapshot = (this.frame != null) ? this.frame.createVolatileImage(this.frame.getWidth(), this.frame.getHeight()) : this.dialog.createVolatileImage(this.dialog.getWidth(), this.dialog.getHeight());
            if (snapshot != null) {
                JLayeredPane layeredPane = (this.frame != null) ? this.frame.getLayeredPane() : this.dialog.getLayeredPane();
                layeredPane.paint(snapshot.getGraphics());
                this.snapshotLayer = new JComponent() {
                    public void paint(Graphics g) {
                        if (snapshot.contentsLost()) {
                            return;
                        }
                        g.drawImage(snapshot, 0, 0, null);
                    }

                    public void removeNotify() {
                        super.removeNotify();
                        snapshot.flush();
                    }
                };
                this.snapshotLayer.setSize(layeredPane.getSize());
                layeredPane.add(this.snapshotLayer, Integer.valueOf(JLayeredPane.DRAG_LAYER.intValue() + 1));
            }
        }
    }

    public void removeSnapshot() {
        if (this.frame != null) {
            this.frame.getLayeredPane().remove(this.snapshotLayer);
        } else {
            this.dialog.getLayeredPane().remove(this.snapshotLayer);
        }
        this.inShowSnapshot = false;
    }
}
