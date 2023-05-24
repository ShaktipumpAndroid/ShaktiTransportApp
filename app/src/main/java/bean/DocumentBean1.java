package bean;

/**
 * Created by Administrator on 12/30/2016.
 */
public class DocumentBean1 {

    public String inst_docno = "";

    public String photo_4 = "";


    public DocumentBean1() {

    }

    public DocumentBean1(String inst_docno_txt, String photo_4_text) {
        inst_docno = inst_docno_txt;
        photo_4 = photo_4_text;

    }

    public String getInst_docno() {
        return inst_docno;
    }

    public void setInst_docno(String inst_docno) {
        this.inst_docno = inst_docno;
    }

    public String getPhoto_4() {
        return photo_4;
    }

    public void setPhoto_4(String photo_4) {
        this.photo_4 = photo_4;
    }

}




