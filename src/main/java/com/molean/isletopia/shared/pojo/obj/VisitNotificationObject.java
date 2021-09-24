package com.molean.isletopia.shared.pojo.obj;

public class VisitNotificationObject {
    private String visitor;
    private String target;
    private boolean success;

    public String getVisitor() {
        return visitor;
    }

    public void setVisitor(String visitor) {
        this.visitor = visitor;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public VisitNotificationObject() {
    }

    public VisitNotificationObject(String visitor, String target, boolean success) {
        this.visitor = visitor;
        this.target = target;
        this.success = success;
    }

    @Override
    public String toString() {
        return "VisitNotificationObject{" +
                "visitor='" + visitor + '\'' +
                ", target='" + target + '\'' +
                ", success=" + success +
                '}';
    }
}
