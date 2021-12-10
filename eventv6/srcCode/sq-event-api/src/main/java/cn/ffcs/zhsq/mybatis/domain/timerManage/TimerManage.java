package cn.ffcs.zhsq.mybatis.domain.timerManage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TimerManage implements Serializable {
    private Long timerId;

    private String timerName;

    private String taskClass;

    private String expression;

    private String timerRemark;

    private String appCode;

    private String status;

    private Date operateTime;

    public String getTimerName() {
        return timerName;
    }

    public void setTimerName(String timerName) {
        this.timerName = timerName == null ? null : timerName.trim();
    }

    public String getTaskClass() {
        return taskClass;
    }

    public void setTaskClass(String taskClass) {
        this.taskClass = taskClass == null ? null : taskClass.trim();
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression == null ? null : expression.trim();
    }

    public String getTimerRemark() {
        return timerRemark;
    }

    public void setTimerRemark(String timerRemark) {
        this.timerRemark = timerRemark == null ? null : timerRemark.trim();
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode == null ? null : appCode.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Long getTimerId() {
        return timerId;
    }

    public void setTimerId(Long timerId) {
        this.timerId = timerId;
    }
}