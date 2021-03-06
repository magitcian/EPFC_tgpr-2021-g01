package lycheenoisi.paintball.controller;

import lycheenoisi.paintball.model.Employee;
import lycheenoisi.paintball.model.Role;
import lycheenoisi.paintball.model.User;
import lycheenoisi.paintball.view.AddEmployeeView;
import lycheenoisi.paintball.view.View;

import java.time.LocalDate;
//import java.util.ArrayList;
import java.util.List;

public class AddEmployeeController extends Controller{
    private final AddEmployeeView view = new AddEmployeeView();

    public void run() {
        View.Action res;
        List<String> errors;
        Employee newEmployee = new Employee();
        try {
            do {
                view.displayHeader();
                // encodage des données
                String username = view.askUserName();
                String firstname = view.askFirstname();
                String name = view.askName();
                String email = view.askEmail();
                String psw = view.askPsw();
                boolean admin = view.askAdmin();
                LocalDate birthDate = askBirthDate();

                // validations métier
                newEmployee.setUsername(username);
                newEmployee.setFirstName(firstname);
                newEmployee.setLastName(name);
                newEmployee.setEmail(email);
                newEmployee.setPassword(psw);
                if(admin){
                    newEmployee.setRole(Role.admin);
                }else{
                    newEmployee.setRole(Role.employee);
                }
                newEmployee.setBirthdate(birthDate);
                errors = newEmployee.validate();
                // affichage des erreurs
                if (errors.size() > 0)
                    view.showErrors(errors);

            } while (errors.size() > 0); // on recommence tant qu'il y a des erreurs

            res = view.askForAction();
            if (res.getAction() == 'O') {
                newEmployee.save();   // sauvegarde des nouvelles données
            }
        } catch (View.ActionInterruptedException e) {
            view.pausedWarning("add profile aborted");
        }
    }

    public LocalDate askBirthDate() {
        LocalDate date;
        String error;
        do {
            date = view.askBirthDate();
            error = User.isValidBirthdate(date);
            if (error != null) view.error(error);
        } while (error != null);
        return date;
    }

}
