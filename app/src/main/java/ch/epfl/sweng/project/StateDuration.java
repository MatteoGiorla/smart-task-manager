package ch.epfl.sweng.project;


class StateDuration {
    private final long duration;
    private final String spinnerDuration;

    public StateDuration(long duration, String spinnerDuration) {
        this.duration = duration;
        this.spinnerDuration = spinnerDuration;
    }

    public String toString() {
        return spinnerDuration;
    }

    public long getDuration() {
        return duration;
    }
}
