package ch.epfl.sweng.project;



class StateLocationType {
    private final Location.LocationType locationType;
    private final String spinnerLocationType;

    public StateLocationType(Location.LocationType locationType, String spinnerLocationType) {
        this.locationType = locationType;
        this.spinnerLocationType = spinnerLocationType;
    }

    public String toString() {
        return spinnerLocationType;
    }

    public Location.LocationType getLocationType() {
        return locationType;
    }
}
