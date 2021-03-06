package lycheenoisi.paintball.controller;

import lycheenoisi.paintball.PaintballApp;
import lycheenoisi.paintball.view.AddFieldView;
import lycheenoisi.paintball.view.View;
import lycheenoisi.paintball.view.MainMenuEmployeeView;

import static lycheenoisi.paintball.model.Role.admin;

public class MainMenuEmployeeController extends Controller {

    public void run() {
        MainMenuEmployeeView view = new MainMenuEmployeeView();
        try {
            View.Action res;
            do {
                view.displayHeader();
                var current = PaintballApp.getLoggedUser();
                int size = 0;

                if (current.getRole().equals(admin))
                    res = view.askForActionAdmin(size);
                else
                    res = view.askForAction(size);

                switch (res.getAction()) {
                    case 'M':
                        new DisplayMembersController().run();
                        break;
                    case 'R':
                        new DisplayFutureReservationsController().run();
                        break;
                    case 'E':
                        new DisplayEmployeesController().run();
                        break;
                    case 'S':
                        new DisplayFieldsStatsController().run();
                        break;
                    case 'A':
                        new DisplayAllFieldsController().run();
                        break;
                    case 'F':
                        new DisplayAllFightTypesController().run();
                        break;
                    case 'Q':
                        new DisplayAllEquipmentsController().run();
                        break;
                    case 'N':
                        new AddFieldController().run();
                        break;

                }
            } while (res.getAction() != 'L');
        } catch (View.ActionInterruptedException e) {
            view.pausedWarning("logged out");
        }

        PaintballApp.logout();
    }
}