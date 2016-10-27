package ch.epfl.sweng.project.information;

/**
 * Class that represents an information
 * constituted by a title and a body.
 */
class InformationItem {
    private final String title;
    private final String body;
    private final int imageId;

    /**
     * Constructor of the InformationItem class.
     *
     * @param title The item's title
     * @param body  The item's description
     * @param imageSrcId the resource id of the image
     *                   belong to R.drawable
     */
    InformationItem(String title, String body, int imageSrcId) {
        if (title.isEmpty())
            throw new IllegalArgumentException("Title passed to Information Item is empty");

        if(body == null)
            throw new IllegalArgumentException("Body passed to Information Item is null");
        this.title = title;
        this.body = body;
        this.imageId = imageSrcId;
    }

    /**
     * Getter that returns the item's title.
     *
     * @return The item's title
     */
    String getTitle() {
        return title;
    }

    /**
     * Getter that returns the item's title.
     *
     * @return The item's description
     */
    String getBody() {
        return body;
    }

    /**
     * Getter that returns the image ID from the drawable folder.
     *
     * @return The image ID
     */
    int getImageSrcId() {
        return imageId;
    }
}
