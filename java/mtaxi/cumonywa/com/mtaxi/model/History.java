package mtaxi.cumonywa.com.mtaxi.model;

public class History {

    String driverId;
    String startLat,startLng,
            stopLat,stopLng,
            price,startAddress,stopAddress;

    public History(){

    }

    public History(String driverId, String startLat, String startLng, String stopLat, String stopLng, String price, String startAddress, String stopAddress) {
        this.driverId = driverId;
        this.startLat = startLat;
        this.startLng = startLng;
        this.stopLat = stopLat;
        this.stopLng = stopLng;
        this.price = price;
        this.startAddress = startAddress;
        this.stopAddress = stopAddress;
    }



    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getStartLat() {
        return startLat;
    }

    public void setStartLat(String startLat) {
        this.startLat = startLat;
    }

    public String getStartLng() {
        return startLng;
    }

    public void setStartLng(String startLng) {
        this.startLng = startLng;
    }

    public String getStopLat() {
        return stopLat;
    }

    public void setStopLat(String stopLat) {
        this.stopLat = stopLat;
    }

    public String getStopLng() {
        return stopLng;
    }

    public void setStopLng(String stopLng) {
        this.stopLng = stopLng;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getStopAddress() {
        return stopAddress;
    }

    public void setStopAddress(String stopAddress) {
        this.stopAddress = stopAddress;
    }
}
