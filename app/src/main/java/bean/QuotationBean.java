package bean;

public class QuotationBean {
    public String rfq_doc, vehicle_rc, vehicle_make, remark, rate, puc, insurance,
            licence, quote_completed, sync, quote_status, res_pernr, res_pernr_type;

    public QuotationBean() {
    }

    public QuotationBean(String rfq_doc,
                         String remark,
                         String rate,
                         String vehicle_rc,
                         String vehicle_make,
                         String puc,
                         String insurance,
                         String licence,
                         String quote_completed,
                         String quote_status,
                         String res_pernr,
                         String res_pernr_type,
                         String sync) {

        this.rfq_doc = rfq_doc;
        this.vehicle_rc = vehicle_rc;
        this.vehicle_make = vehicle_make;
        this.remark = remark;
        this.rate = rate;
        this.puc = puc;
        this.insurance = insurance;
        this.licence = licence;
        this.quote_completed = quote_completed;
        this.sync = sync;
        this.quote_status = quote_status;
        this.res_pernr = res_pernr;
        this.res_pernr_type = res_pernr_type;
    }

    public String getRfq_doc() {
        return rfq_doc;
    }

    public void setRfq_doc(String rfq_doc) {
        this.rfq_doc = rfq_doc;
    }

    public String getVehicle_rc() {
        return vehicle_rc;
    }

    public void setVehicle_rc(String vehicle_rc) {
        this.vehicle_rc = vehicle_rc;
    }

    public String getVehicle_make() {
        return vehicle_make;
    }

    public void setVehicle_make(String vehicle_make) {
        this.vehicle_make = vehicle_make;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getPuc() {
        return puc;
    }

    public void setPuc(String puc) {
        this.puc = puc;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getQuote_completed() {
        return quote_completed;
    }

    public void setQuote_completed(String quote_completed) {
        this.quote_completed = quote_completed;
    }

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }

    public String getQuote_status() {
        return quote_status;
    }

    public void setQuote_status(String quote_status) {
        this.quote_status = quote_status;
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