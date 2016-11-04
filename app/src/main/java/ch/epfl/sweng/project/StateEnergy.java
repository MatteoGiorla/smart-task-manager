package ch.epfl.sweng.project;


import android.content.Context;

public class StateEnergy {
    private final Task.Energy energy;
    private final String spinnerEnergy;

    public StateEnergy(Task.Energy energy, Context context) {
        this.energy = energy;
        Context context1 = context;
        switch (energy) {
            case LOW:
                this.spinnerEnergy = context.getString(R.string.low_energy);
                break;
            case NORMAL:
                this.spinnerEnergy = context.getString(R.string.normal_energy);
                break;
            case HIGH:
                this.spinnerEnergy = context.getString(R.string.high_energy);
                break;
            default:
                this.spinnerEnergy = context.getString(R.string.normal_energy);
                break;
        }
    }

    public String toString() {
        return spinnerEnergy;
    }

    public Task.Energy getEnergy() {
        return energy;
    }
}
