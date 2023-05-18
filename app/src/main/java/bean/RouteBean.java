package bean;


public class RouteBean {


    public String fr_state,
            fr_district,
            fr_tehsil,
            to_state,
            to_district,
            to_tehsil,
            tr_mode,
            distance,
            status,
            res_pernr,
            res_pernr_type;

    public RouteBean() {


    }


    public RouteBean(String fr_state_txt,
                     String fr_district_txt,
                     String fr_tehsil_txt,
                     String to_state_txt,
                     String to_district_txt,
                     String to_tehsil_txt,
                     String tr_mode_txt,
                     String distance_txt,
                     String status_txt,
                     String res_pernr_txt,
                     String res_pernr_type_txt
    ) {

        fr_state = fr_state_txt;
        fr_district = fr_district_txt;
        fr_tehsil = fr_tehsil_txt;
        to_state = to_state_txt;
        to_district = to_district_txt;
        to_tehsil = to_tehsil_txt;
        tr_mode = tr_mode_txt;
        distance = distance_txt;
        status = status_txt;
        res_pernr = res_pernr_txt;
        res_pernr_type = res_pernr_type_txt;
    }

    public String getFr_state() {
        return fr_state;
    }

    public void setFr_state(String fr_state) {
        this.fr_state = fr_state;
    }

    public String getFr_district() {
        return fr_district;
    }

    public void setFr_district(String fr_district) {
        this.fr_district = fr_district;
    }

    public String getFr_tehsil() {
        return fr_tehsil;
    }

    public void setFr_tehsil(String fr_tehsil) {
        this.fr_tehsil = fr_tehsil;
    }

    public String getTo_state() {
        return to_state;
    }

    public void setTo_state(String to_state) {
        this.to_state = to_state;
    }

    public String getTo_tehsil() {
        return to_tehsil;
    }

    public void setTo_tehsil(String to_tehsil) {
        this.to_tehsil = to_tehsil;
    }

    public String getTo_district() {
        return to_district;
    }

    public void setTo_district(String to_district) {
        this.to_district = to_district;
    }

    public String getTr_mode() {
        return tr_mode;
    }

    public void setTr_mode(String tr_mode) {
        this.tr_mode = tr_mode;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getRes_pernr() {
        return res_pernr;
    }

    public void setRes_pernr(String res_pernr) {
        this.res_pernr = res_pernr;
    }

    public String getRes_pernr_type() {
        return res_pernr_type;
    }

    public void setRes_pernr_type(String res_pernr_type) {
        this.res_pernr_type = res_pernr_type;
    }
}
