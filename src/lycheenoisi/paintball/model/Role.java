package lycheenoisi.paintball.model;

public enum Role {

    admin("admin"),
    employee("employe"),
    member("member"),
    membervip("member vip");

    private String nomDB;

    private Role(String nomDB) {
        this.nomDB = nomDB;
    }

    public String getNomDB() {
        return this.nomDB;
    }

}
