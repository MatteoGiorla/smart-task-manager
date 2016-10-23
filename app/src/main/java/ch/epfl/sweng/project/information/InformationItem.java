package ch.epfl.sweng.project.information;

/**
 * Class that represents an information
 * constituted by a title and a body.
 */
class InformationItem {
    private final String title;
    private final String body;

    /**
     * Constructor of the InformationItem class
     *
     * @param title The item's title
     * @param body The item's description
     */
    InformationItem(String title, String body) {
        this.title = title;
        this.body = body;
    }

    /**
     * Getter that returns the item's title
     *
     * @return The item's title
     */
    String getTitle() {
        return title;
    }

    /**
     * Getter that returns the item's title
     *
     * @return The item's description
     */
    String getBody() {
        return body;
    }
}
