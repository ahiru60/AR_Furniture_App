package com.example.ar_furniture_application;

import java.util.Date;
import java.util.List;

public class SupportRequest {
    private String requestId;
    private String userId;
    private String requestType;
    private String requestDescription;
    private Date creationDate;
    private String status;
    private String priority;
    private String assignedTo;
    private String resolution;
    private Date resolutionDate;
    private List<String> attachments;

    // Constructor
    public SupportRequest(String requestId, String userId, String requestType, String requestDescription, Date creationDate, String status, String priority, String assignedTo, String resolution, Date resolutionDate, List<String> attachments) {
        this.requestId = requestId;
        this.userId = userId;
        this.requestType = requestType;
        this.requestDescription = requestDescription;
        this.creationDate = creationDate;
        this.status = status;
        this.priority = priority;
        this.assignedTo = assignedTo;
        this.resolution = resolution;
        this.resolutionDate = resolutionDate;
        this.attachments = attachments;
    }

    // Getters and Setters
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestDescription() {
        return requestDescription;
    }

    public void setRequestDescription(String requestDescription) {
        this.requestDescription = requestDescription;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Date getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(Date resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    // Methods
    public void createRequest() {
        // Implement create request logic here
    }

    public void updateRequest(String requestId, String requestType, String requestDescription, String status, String priority, String assignedTo, String resolution, Date resolutionDate, List<String> attachments) {
        this.requestId = requestId;
        this.requestType = requestType;
        this.requestDescription = requestDescription;
        this.status = status;
        this.priority = priority;
        this.assignedTo = assignedTo;
        this.resolution = resolution;
        this.resolutionDate = resolutionDate;
        this.attachments = attachments;
    }

    public void escalateRequest() {
        // Implement escalate request logic here
    }

    public void addAttachment(String attachment) {
        this.attachments.add(attachment);
    }

    public void removeAttachment(String attachment) {
        this.attachments.remove(attachment);
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public void addResolution(String resolution, Date resolutionDate) {
        this.resolution = resolution;
        this.resolutionDate = resolutionDate;
    }

    public void assignTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}

