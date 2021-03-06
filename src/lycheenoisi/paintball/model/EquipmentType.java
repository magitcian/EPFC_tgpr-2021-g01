package lycheenoisi.paintball.model;

import java.sql.SQLException;
import java.util.ArrayList;

public class EquipmentType extends Model{
    private String name;
    private double rent_price;
    private double sell_price;

    public EquipmentType(String name, double rent_price, double sell_price){
        this.setName(name);
        this.setRent_price(rent_price);
        this.setRent_price(sell_price);
    }

    public EquipmentType(String name, double rent_price){
        this.setName(name);
        this.setRent_price(rent_price);
    }

    public EquipmentType() { }

    public EquipmentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRent_price() {
        return rent_price;
    }

    public void setRent_price(double rent_price) {
        this.rent_price = rent_price;
    }

    public double getSell_price() { return sell_price; }

    public void setSell_price(double sell_price) { this.sell_price = sell_price; }

    public String toString(){
        return "Reserved equipment: " + this.getName();
    }

    public static ArrayList<EquipmentType> getAllEquipments(){
        String request=null;
        request = "SELECT et.name 'name', et.rent_price 'rent_price', et.sell_price 'sell_price' from  Equipment_Type et";
        var equipments = new ArrayList<EquipmentType>();
        try {
            var stmt = db.prepareStatement(request);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                var et = new EquipmentType();
                et.setName(rs.getString("name"));
                et.setRent_price(rs.getInt("rent_price"));
                et.setSell_price(rs.getInt("sell_price"));
                equipments.add(et);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipments;
    }

}
