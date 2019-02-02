package mtaxi.cumonywa.com.mtaxi;

public class ItemGenerator {
    String driverName;
    String driverCarType;
    String price;

    public ItemGenerator(String driverName, String driverCarType, String price, String startPlace, String endPlace) {
        this.driverName = driverName;
        this.driverCarType = driverCarType;
        this.price = price;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
    }

    String startPlace;
    String endPlace;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverCarType() {
        return driverCarType;
    }

    public void setDriverCarType(String driverCarType) {
        this.driverCarType = driverCarType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }
}
