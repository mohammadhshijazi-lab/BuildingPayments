public class Apartment {
    int floor ;
    int number ;
    String tenantName ;
    boolean paid ;
    void printInfo(){
        System.out.println("Floor : " + floor + "\napartment : " + number);
    }
    void pay()
    {
        paid = true ;
    }
    void changeTenant(String newTenant){
        tenantName = newTenant ;
    }
}
