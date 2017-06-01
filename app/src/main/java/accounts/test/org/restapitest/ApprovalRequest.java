package accounts.test.org.restapitest;

/**
 * Created by adarshs on 01-06-2017.
 */

class ApprovalRequest {
    String requestID;
    String approvalStatus;
    String approvers;
    String application;
    String process;
    String request;

    public ApprovalRequest(String requestID, String approvalStatus, String approvers, String application, String process, String request) {
        this.requestID = requestID;
        this.approvalStatus = approvalStatus;
        this.approvers = approvers;
        this.application = application;
        this.process = process;
        this.request = request;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovers() {
        return approvers;
    }

    public void setApprovers(String approvers) {
        this.approvers = approvers;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
