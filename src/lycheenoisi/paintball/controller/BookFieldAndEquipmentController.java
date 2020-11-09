package lycheenoisi.paintball.controller;

import lycheenoisi.paintball.PaintballApp;
import lycheenoisi.paintball.model.*;
import lycheenoisi.paintball.view.BookFieldAndEquipmentView;
import lycheenoisi.paintball.view.CancelReservationView;
import lycheenoisi.paintball.view.View;

import java.time.LocalDate;


public class BookFieldAndEquipmentController extends Controller {
    private final BookFieldAndEquipmentView view = new BookFieldAndEquipmentView();

    @Override
    public void run() {
        User connectedUser = PaintballApp.getLoggedUser();
        String res = null;
        do {
            view.displayHeader();
            LocalDate dateSouhaitee = null;
            boolean hasError = false;

            do {
                dateSouhaitee = view.askDate();
                hasError = !this.dateValide(dateSouhaitee, connectedUser);
            } while (hasError);

            int timeSlot = 0;
            do {
                timeSlot = view.askTimeslot();
            } while (timeSlot < 1 || timeSlot > 3);
            String fightType = view.askFightType();
            Timeslot ts = null;
            if (timeSlot == 1)
                ts = Timeslot.Morning;
            if (timeSlot == 2)
                ts = Timeslot.Afternoon;
            if (timeSlot == 3)
                ts = Timeslot.Evening;
            var fields = Field.getAvailableFields(dateSouhaitee, ts, fightType);
            view.displayAvailableFields(fields);
            if (fields.isEmpty()) {
                view.println("Aucun terrain disponible avec vos filtres.");
                res = view.askString("[R] Ressayer, [S] Sortir", null);
                continue;
            }
            int idTerrain = view.askInt("Choissez le terrain à réserver: ");
            Reservation.createReservation(dateSouhaitee, ts, idTerrain, connectedUser.getId(), fightType);
            break;
        } while (!res.equals("S") && !res.equals("s"));
    }

    public boolean dateValide(LocalDate dateSouhaitee, User connectedUser) {
        if (dateSouhaitee == null) {
            return false;
        }
        if (LocalDate.now().plusDays(1).isAfter(dateSouhaitee)) {
            return false;
        }
        if (connectedUser.getRole() == Role.membervip) {
            return dateSouhaitee.isBefore(LocalDate.now().plusDays(1).plusMonths(3));
        }
       return dateSouhaitee.isBefore(LocalDate.now().plusDays(1).plusWeeks(2));
    }


}
