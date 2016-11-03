package ch.epfl.sweng.project;

import android.content.res.Resources;

public class StateDuration {
    private final long duration;
    private final String spinnerDuration;

    public StateDuration(long duration){
        switch ((int)duration) {
            case 5:
                this.duration = duration;
                this.spinnerDuration = "5 minutes";
                break;
            case 15:
                this.duration = duration;
                this.spinnerDuration = "15 minutes";
                break;
            case 30:
                this.duration = duration;
                this.spinnerDuration = "30 minutes";
                break;
            case 60:
                this.duration = duration;
                this.spinnerDuration = "1 hour";
                break;
            case 120:
                this.duration = duration;
                this.spinnerDuration = "2 hours";
                break;
            case 240:
                this.duration = duration;
                this.spinnerDuration = "4 hours";
                break;
            case 1440:
                this.duration = duration;
                this.spinnerDuration = "1 day";
                break;
            case 2880:
                this.duration = duration;
                this.spinnerDuration = "2 days";
                break;
            case 5760:
                this.duration = duration;
                this.spinnerDuration = "4 days";
                break;
            case 10080:
                this.duration = duration;
                this.spinnerDuration = "1 week";
                break;
            case 20160:
                this.duration = duration;
                this.spinnerDuration = "2 weeks";
                break;
            case 43800:
                this.duration = duration;
                this.spinnerDuration = "1 month";
                break;
            default:
                this.duration = 0;
                this.spinnerDuration = "Unknown";
                break;
        }
    }

    public String toString() {
        return spinnerDuration;
    }

    public long getDuration() {
        return duration;
    }
}
