package com.sd217;

/**
 * Created by sd217 on 22/09/15.
 */
public class CaveRoom {


    //    private boolean empty;
    private boolean adventurer;
    private boolean pit;
    private boolean containsTreasure;
    private boolean wumpus;
    private boolean superbat;
    private boolean breezy;
    private boolean stenchy;
    private boolean glittery;
    private boolean exit;
    private int x, y;
    private int numObjects;

    public CaveRoom(boolean isPit) {
        this.pit = isPit;
//        this.empty = !isPit;

    }

    public String printInMap() {

        StringBuilder roomAsString = new StringBuilder("");
        if (!isEmpty()) {
            roomAsString.append(
                    (adventurer ? ColorCodes.BLUE + "A" : "") +
                            (pit ? ColorCodes.RED + "P" : "") +
                            (containsTreasure ? ColorCodes.YELLOW + "T" : "") +
                            (wumpus ? ColorCodes.RED + "W" : "") +
                            (superbat ? ColorCodes.PURPLE + "}" : "") +
                            (breezy ? ColorCodes.CYAN + "B" : "") +
                            (stenchy ? ColorCodes.GREEN + "S" : "") +
                            (glittery ? ColorCodes.YELLOW + "G" : "") +
                            (exit ? ColorCodes.BLUE + "E" : "")
            );
        }
        roomAsString.append(ColorCodes.RESET);
        return roomAsString.toString();

    }

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

    public boolean isEmpty() {
        return !isPit() && !hasAdventurer() && !isBreezy() && !isStenchy() && isGlittery() && !hasWumpus();
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

    public void setGlittery(boolean glittery) {
        this.glittery = glittery;
    }

    public boolean hasWumpus() {
        return wumpus;
    }

    public void setWumpus(boolean wumpus) {
        this.wumpus = wumpus;
    }
}
