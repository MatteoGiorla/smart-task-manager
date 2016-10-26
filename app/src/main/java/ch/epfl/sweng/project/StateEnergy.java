package ch.epfl.sweng.project;



public class StateEnergy {
    private int energy;
    private String spinnerEnergy;

    public StateEnergy(Task.Energy energy, String spinnerEnergy) {
        this.energy = energy.ordinal();
        this.spinnerEnergy = spinnerEnergy;
    }

    public Task.Energy getEnergy() {
        return energy.;
    }
}
