package world;

import utils.ColorCodes;

/**
 * Represents a room in the cave. Static objects are attributes in the room.
 * @author Sebastián Duque on 22/09/15.
 */
public class CaveRoom {
    private boolean adventurer;
    private boolean pit;
    private boolean containsTreasure;
    private boolean wumpus;
    private boolean superbat;
    private boolean breezy;
    private boolean stenchy;
    private boolean glittery;
    private boolean exit;

    private boolean visited;
    private boolean known;
    /**
     * Constructor. Must be explicitly told if the room is a pit or not
     * @param isPit boolean, if the room is a pit or not
     */
    public CaveRoom(boolean isPit) {
        this.pit = isPit;
    }

    public CaveRoom(){

    }

    @Override
    public String toString() {
        StringBuilder roomAsString = new StringBuilder("");
        if (!isEmpty()) {
            roomAsString.append(
                    (adventurer ? ColorCodes.BLUE + "A" : "") +
                            (pit ? ColorCodes.RED + "P" : "") +
                            (containsTreasure ? ColorCodes.YELLOW + "T" : "") +
                            (wumpus ? ColorCodes.RED + "W" : "") +
                            (superbat ? ColorCodes.PURPLE + "B" : "") +
                            (breezy ? ColorCodes.CYAN + "~" : "") +
                            (stenchy ? ColorCodes.GREEN + "S" : "") +
                            (glittery ? ColorCodes.YELLOW + "£" : "") +
                            (exit ? ColorCodes.WHITE + "E" : "")
            );
        } else if(wasVisited()) {
            roomAsString.append("_");
        }
        roomAsString.append(ColorCodes.RESET);
        return roomAsString.toString();
    }

    /**
     * Returns the number of objects present in the cave.
     * Both static and dynamic objects are accounted for.
     * @return Integer with the number of static and dynamic objects in the room
     */
    public int getNumberOfObjects() {
        int numObjects = 0;
        numObjects += adventurer ? 1 : 0;
        numObjects += pit ? 1 : 0;
        numObjects += containsTreasure ? 1 : 0;
        numObjects += wumpus ? 1 : 0;
        numObjects += superbat ? 1 : 0;
        numObjects += breezy ? 1 : 0;
        numObjects += stenchy ? 1 : 0;
        numObjects += glittery ? 1 : 0;
        numObjects += exit ? 1 : 0;
        return numObjects;
    }

    /**
     * If there are no static or dynamic objects in the room then it is empty.
     * This method performs such check and returns the result.
     * @return boolean, whether the room is empty or not.
     */
    public boolean isEmpty() {
        return !isPit() && !hasAdventurer() && !isBreezy() &&
                !isStenchy() && !isGlittery() && !hasWumpus() &&
                !containsTreasure() && !isExit() && !hasSuperbat();
    }

    public boolean isRoomSafe() {
        return !isPit() && !hasWumpus() && !hasSuperbat();
    }
    public boolean hasAdventurer() {
        return adventurer;
    }

    public boolean isPit() {
        return pit;
    }

    public boolean isBreezy() {
        return breezy;
    }

    public void setBreezy(boolean isBreezy) {
        this.breezy = isBreezy;
    }

    public boolean isStenchy() {
        return stenchy;
    }

    public void setStenchy(boolean isStenchy) {
        this.stenchy = isStenchy;
    }

    public boolean isGlittery() {
        return glittery;
    }

    public boolean containsTreasure() {
        return containsTreasure;
    }

    public void setContainsTreasure(boolean containsTreasure) {
        this.containsTreasure = containsTreasure;
    }

    public void setGlittery(boolean glittery) {
        this.glittery = glittery;
    }

    public boolean hasWumpus() {
        return wumpus;
    }

    public void setWumpus(boolean wumpus) {
        this.wumpus = wumpus;
    }

    public void setAdventurer(boolean adventurer) {
        this.adventurer = adventurer;
    }

    public boolean hasSuperbat() {
        return superbat;
    }

    public void setSuperbat(boolean superbat) {
        this.superbat = superbat;
    }

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public boolean isKnown() {
        return known;
    }

    public void setKnown(boolean known) {
        this.known = known;
    }

    public boolean wasVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
