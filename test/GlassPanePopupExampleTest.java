
import javax.swing.JPanel;

public class GlassPanePopupExampleTest {

    private FrameFixture window;

    public static void main(String[] args) {
        GlassPanePopupExampleTest test = new GlassPanePopupExampleTest();
        test.setUp();
        test.testPopupDisplayed();
        test.tearDown();
    }

    public void setUp() {
        GlassPanePopupExample frame = new GlassPanePopupExample();
        window = new FrameFixture(frame);
        window.show();
    }

    public void tearDown() {
        window.cleanUp();
    }

    public void testPopupDisplayed() {
        window.button(JButtonMatcher.withText("Show Popup")).click();
        JPanel glassPane = (JPanel) window.target().getGlassPane();
        System.out.println(glassPane.isVisible() ? "Test Passed" : "Test Failed");
    }
}
