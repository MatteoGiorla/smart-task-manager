package ch.epfl.sweng.project;



class StateEnergy {
    private final Task.Energy energy;
    private final String spinnerEnergy;

    public StateEnergy(Task.Energy energy, String spinnerEnergy) {
        this.energy = energy;
        this.spinnerEnergy = spinnerEnergy;
    }

    public String toString() {
        return spinnerEnergy;
    }

    public Task.Energy getEnergy() {
        return energy;
    }
}
