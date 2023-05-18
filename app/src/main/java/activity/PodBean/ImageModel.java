package activity.PodBean;

public class ImageModel {

    String ID;
    String name;
    String ImagePath;

    String billNo;
    boolean isImageSelected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return ImagePath;
    }


    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public boolean isImageSelected() {
        return isImageSelected;
    }

    public void setImageSelected(boolean imageSelected) {
        isImageSelected = imageSelected;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}

