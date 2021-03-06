package lycheenoisi.paintball.view;

public class MainMenuMemberView extends View {
    public void displayHeader() {
        displayHeader("Main menu member:");
    }

    public View.Action askForAction(int size) {
        return doAskForAction(size, "\n[B] Make a reservation\n[E] Edit profile\n[R] Display reservations\n[A] Display fields\n[F] Display fight types\n[Q] Display equipments\n[D] Display available fields\n[L] Logout",
                "[bB]+|[eE]+|[rR]+|[aA]+|[fF]+|[qQ]+|[lL]+|[dD]");
    }

    public View.Action askForActionVIP(int size) {
        return doAskForAction(size, "\n[B] Make a reservation\n[E] Edit profile\n[R] Display reservations\n[A] Display fields\n[F] Display fight types\n[Q] Display equipments\n[D] Display available fields\n[L] Logout",
                "[bB]+|[eE]+|[rR]+|[aA]+|[fF]+|[qQ]+|[lL]+|[dD]");
    }
}
