package ch.epfl.sweng.project;

import android.content.Context;

public class StateDuration {
    private final long duration;
    private final String spinnerDuration;

    public StateDuration(long duration, Context context){
        this.duration = duration;
        switch ((int)duration) {
            case 5:
                this.spinnerDuration = context.getString(R.string.duration5m);
                break;
            case 15:
                this.spinnerDuration = context.getString(R.string.duration15m);
                break;
            case 30:
                this.spinnerDuration = context.getString(R.string.duration30m);
                break;
            case 60:
                this.spinnerDuration = context.getString(R.string.duration1h);
                break;
            case 120:
                this.spinnerDuration = context.getString(R.string.duration2h);
                break;
            case 240:
                this.spinnerDuration = context.getString(R.string.duration4h);
                break;
            case 1440:
                this.spinnerDuration = context.getString(R.string.duration1d);
                break;
            case 2880:
                this.spinnerDuration = context.getString(R.string.duration2d);
                break;
            case 5760:
                this.spinnerDuration = context.getString(R.string.duration4d);
                break;
            case 10080:
                this.spinnerDuration = context.getString(R.string.duration1w);
                break;
            case 20160:
                this.spinnerDuration = context.getString(R.string.duration2w);
                break;
            case 43800:
                this.spinnerDuration = context.getString(R.string.duration1m);
                break;
            default:
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
