package ch.epfl.sweng.project;


public class StateDuration {
    private long duration;
    private String spinnerDuration;

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
