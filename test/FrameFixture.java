
import javax.swing.*;
import java.awt.*;

public class FrameFixture {

    private final JFrame frame;

    public FrameFixture(JFrame frame) {
        this.frame = frame;
    }

    public void show() {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    public void cleanUp() {
        SwingUtilities.invokeLater(() -> frame.dispose());
    }

    public JButtonFixture button(JButtonMatcher matcher) {
        JButton button = findButton(frame.getContentPane(), matcher);
        if (button != null) {
            return new JButtonFixture(button);
        }
        throw new IllegalArgumentException("Button not found: " + matcher.toString());
    }

    private JButton findButton(Container container, JButtonMatcher matcher) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton && matcher.matches((JButton) component)) {
                return (JButton) component;
            }
            if (component instanceof Container container1) {
                JButton button = findButton(container1, matcher);
                if (button != null) {
                    return button;
                }
            }
        }
        return null;
    }

    public JFrame target() {
        return frame;
    }
}

class JButtonFixture {

    private final JButton button;

    public JButtonFixture(JButton button) {
        this.button = button;
    }

    public void click() {
        SwingUtilities.invokeLater(() -> button.doClick());
    }
}

class JButtonMatcher {

    private final String text;

    private JButtonMatcher(String text) {
        this.text = text;
    }

    public static JButtonMatcher withText(String text) {
        return new JButtonMatcher(text);
    }

    public boolean matches(JButton button) {
        return button.getText().equals(text);
    }

    @Override
    public String toString() {
        return "JButtonMatcher{text='" + text + '\'' + '}';
    }
}
