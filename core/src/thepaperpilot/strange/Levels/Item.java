package thepaperpilot.strange.Levels;

public class Item {
    String name;
    Level level;

    public Item(ItemPrototype item, Level level) {
        this.level = level;
    }

    public static class ItemPrototype {
        String name;
    }
}
