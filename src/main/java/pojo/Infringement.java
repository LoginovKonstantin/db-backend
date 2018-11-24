package pojo;

import java.util.Date;

public class Infringement {
    private int id, judgeId, memberId;
    private String description;
    private Date infringementDate;
    private String comment;

    public Infringement(int id, String description, int judgeId, Date infringementDate, String comment, int memberId) {
        this.id = id;
        this.judgeId = judgeId;
        this.memberId = memberId;
        this.description = description;
        this.infringementDate = infringementDate;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public int getJudgeId() {
        return judgeId;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getDescription() {
        return description;
    }

    public Date getInfringementDate() {
        return infringementDate;
    }

    public String getComment() {
        return comment;
    }
}
