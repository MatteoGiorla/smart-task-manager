package ch.epfl.sweng.project;



public class StateEnergy {
    private Task.Energy energy;
    private String spinnerEnergy;

    public StateEnergy(Task.Energy energy, String spinnerEnergy) {
        this.energy = energy;
        this.spinnerEnergy = spinnerEnergy;
    }

    public String toString() {
        return spinnerEnergy;
    }

    public Task.Energy getEnergy() {
        return energy; //Task.Energy.values()[energy];
    }
}
