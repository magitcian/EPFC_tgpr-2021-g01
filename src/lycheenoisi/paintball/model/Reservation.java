package lycheenoisi.paintball.model;

import java.time.LocalDate;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static lycheenoisi.paintball.model.Timeslot.*;

public class Reservation extends Model {

    private int id;
    private LocalDate date;
    private Timeslot timeslot;
    private boolean isCancelled;
    private Field field;
    private List<EquipmentType> equipmentTypeList = new ArrayList<>();
    private FightType ft;
    private Member mb;
    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;

    }

    public static void cancelReservation(int id) {
        String requestSetIsCancelled = " UPDATE reservation SET is_cancelled = true where id = ?";
        String requestDeleteReservationEquipmentStock = "DELETE from reservation_equipment_stock WHERE reservation_id = ?";
        try {
            var statementUpdate = db.prepareStatement(requestSetIsCancelled);
            statementUpdate.setInt(1, id);
            statementUpdate.executeQuery();
            var statementDelete = db.prepareStatement(requestDeleteReservationEquipmentStock);
            statementDelete.setInt(1, id);
            statementDelete.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Reservation> getReservationsNotCancelled(Member m) {
//        Requete:
//        SELECT r.id, r.date, r.timeslot, f.name 'field_name', f.description 'field_description', f.is_inside, f.level, f.min_players, f.max_players, f.vip, f.price, et.name 'equipment_name', et.rent_price
//        from reservation r
//        LEFT JOIN user u ON u.id =  r.user_id
//        LEFT JOIN field f ON f.id = r.field_id
//        LEFT JOIN reservation_equipment_stock re ON re.reservation_id = r.id
//        LEFT JOIN equipment_stock eo ON eo.id = re.equipment_stock_id
//        LEFT JOIN equipment_type et ON et.id = eo.equipment_type_id
//        WHERE u.username = 'lmalsag' and r.is_cancelled IS NULL and r.date > NOW()
//        ORDER BY r.id

        String request = " SELECT r.id, r.date, r.timeslot, f.name 'field_name', f.description 'field_description', f.is_inside, f.level, f.min_players, f.max_players, f.vip, f.price, et.name 'equipment_name', et.rent_price";
        request += " from reservation r";
        request += " LEFT JOIN user u ON u.id =  r.user_id";
        request += " LEFT JOIN field f ON f.id = r.field_id";
        request += " LEFT JOIN reservation_equipment_stock re ON re.reservation_id = r.id";
        request += " LEFT JOIN equipment_stock eo ON eo.id = re.equipment_stock_id";
        request += " LEFT JOIN equipment_type et ON et.id = eo.equipment_type_id";
        request += " WHERE u.username = ? and r.is_cancelled IS NULL and r.date > NOW()";
        request += " ORDER BY r.id";

        var list = new ArrayList<Reservation>();
        try {
            var stmt = db.prepareStatement(request);
            stmt.setString(1, m.getUsername());
            var rs = stmt.executeQuery();
            //à changer pour avoir les équipements bien reliés à la bonne réservation
            int idPrec = -1;
            var r = new Reservation();
            r.setMb(m);
            while (rs.next()) {
                if (rs.getInt("id") != idPrec) {
                    r = new Reservation();
                    r.id = rs.getInt("id");
                    r.setDate(rs.getObject("date", LocalDate.class));
                    String tmsl = rs.getString("timeslot");
                    if(tmsl.equals(Morning.getNomDB())){
                        r.setTimeslot(Morning);
                    }else if(tmsl.equals(Afternoon.getNomDB())){
                        r.setTimeslot(Afternoon);
                    }else if(tmsl.equals(Evening.getNomDB())){
                        r.setTimeslot(Evening);
                    }
                    r.setCancelled(false);
                    var f = new Field(rs.getString("field_name"), rs.getString("field_description"), rs.getBoolean("is_inside"), rs.getInt("level"), rs.getInt("max_players"), rs.getInt("min_players"), rs.getBoolean("vip"), rs.getDouble("price"));
                    r.setField(f);
                    r.getEquipmentList().add(new EquipmentType(rs.getString("equipment_name"), rs.getDouble("rent_price")));
                    list.add(r);
                }else{
                    r.getEquipmentList().add(new EquipmentType(rs.getString("equipment_name"), rs.getDouble("rent_price")));
                }
                idPrec = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String toString(){
        String equipments ="";
        for(EquipmentType eq : equipmentTypeList){
            equipments += eq.toString() + ", ";
        }
        String dt = this.getDate().getDayOfMonth() + "/" + this.getDate().getMonthValue() + "/" + this.getDate().getYear();
        return "Reservation Date : " + dt + " " + this.timeslot + "; [" + this.getField() + "]; [" + equipments + "]";
    }

    public void cancelReservation(){

    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public List<EquipmentType> getEquipmentList() {
        return equipmentTypeList;
    }

    public void setEquipmentList(List<EquipmentType> equipmentTypeList) {
        this.equipmentTypeList = equipmentTypeList;
    }

    public FightType getFt() {
        return ft;
    }

    public void setFt(FightType ft) {
        this.ft = ft;
    }

    public Member getMb() {
        return mb;
    }

    public void setMb(Member mb) {
        this.mb = mb;
    }
}