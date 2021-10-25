package com.publicicat.mismascotastres;

import java.util.ArrayList;

public interface FragmentTwoInt {

    void generarGridLayour();

    AdapterTwo crearAdaptador(ArrayList<Constructor> mascota);

    void initAdapterRV(AdapterTwo adapterTwo);
}
