package pojo;

import java.util.Date;

public class Contest {
    private int id, organizationId;
    private String name, status;
    private Date startDate, endDate;

    public Contest(int id, String name, Date endDate, Date startDate, String status, int organizationId) {
        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
