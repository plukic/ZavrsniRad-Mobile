package ba.ito.assistance.model.gas_stations;

import java.util.List;

public class GasStationVM {
    public int Id;
    public double Lat;
    public double Long;
    public String City;
    public String Address;
    public List<WorkingHoursVM> WorkingHours;
    public List<FuelPricesVM> FuelPrices;
}
