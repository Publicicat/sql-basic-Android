package com.publicicat.mismascotastres;

import java.util.ArrayList;

public interface FragmentOneInt {

    void generarLinearLayour();

    AdapterOne crearAdaptador(ArrayList<Constructor> mascotas);

    void initAdapterRV(AdapterOne adapter);
}
