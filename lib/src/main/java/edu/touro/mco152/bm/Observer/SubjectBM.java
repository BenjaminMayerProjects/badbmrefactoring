package edu.touro.mco152.bm.Observer;

import java.util.Observer;

public interface SubjectBM {
    public void registerObserver(ObserverBM o);

    public void removeObserver(ObserverBM o);

    public void notifyObservers(ObserverBM o);
}
