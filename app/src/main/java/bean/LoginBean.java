package bean;


public class LoginBean {

    public static String userid;
    public static String username;
    public static String usertype;
    public static String userrfq;

    public static void setLogin(String id, String name, String type, String rfqno) {
        userid = id;
        username = name;
        usertype = type;
        userrfq = rfqno;
    }

    public static String getUsername() {
        return username;
    }

    public static String getUseid() {
        return userid;
    }

    public static String getUsertype() {
        return usertype;
    }

    public static String getUserrfq() {
        return userrfq;
    }
}
