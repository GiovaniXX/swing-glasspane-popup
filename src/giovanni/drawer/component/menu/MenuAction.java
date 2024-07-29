package giovanni.drawer.component.menu;

public class MenuAction {

    private boolean consume;

    private boolean selected;

    protected boolean getConsume() {
        return this.consume;
    }

    public void consume() {
        this.consume = true;
    }

    protected boolean getSelected() {
        return this.selected;
    }

    public void selected() {
        this.selected = true;
    }
}
