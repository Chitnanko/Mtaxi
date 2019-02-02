package mtaxi.cumonywa.com.mtaxi;


public class Driver {
    private  String driverId;
    private String phone,name,carNo,carType;
    private String driverPhotoUrl=null;
    private boolean active;

    public Driver(String driverId, String phone, String name, String carNo, String carType, String driverPhotoUrl, boolean active) {
        this.driverId = driverId;
        this.phone = phone;
        this.name = name;
        this.carNo = carNo;
        this.carType = carType;
        this.driverPhotoUrl = driverPhotoUrl;
        this.active = active;
    }

    public Driver(){

    }

    public String getDriverId() {
        return driverId;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }


    public String getCarNo() {
        return carNo;
    }


    public String getCarType() {
        return carType;
    }


    public String getDriverPhotoUrl() {
        return driverPhotoUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
