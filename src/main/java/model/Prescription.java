package model;

import java.util.Date;
import java.util.List;

public class Prescription {
    private int id;
    private Medicine medicine;
    private Issuer issuer;
    private Date issueDate;
    private String comment;
    private int quantity;
    private boolean isRealized;


}
