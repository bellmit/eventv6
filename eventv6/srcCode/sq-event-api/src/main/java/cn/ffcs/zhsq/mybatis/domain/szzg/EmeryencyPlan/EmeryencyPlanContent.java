package cn.ffcs.zhsq.mybatis.domain.szzg.EmeryencyPlan;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;

public class EmeryencyPlanContent implements java.io.Serializable {
    private static final long serialVersionUID = -4249261984632495452L;

    private Long id;

    private String title;

    private Date createdtime;

    private Long priority;

    private Long treeId;

    private String status;

    private byte[] txt;

    private String txtStr;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Date createdtime) {
        this.createdtime = createdtime;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Long getTreeId() {
        return treeId;
    }

    public void setTreeId(Long treeId) {
        this.treeId = treeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public byte[] getTxt() {
            return txt;
    }

    public String getTxtStr() {

        if(txt!=null)
        {
            try {
                if (getEncoding(new String(txt))) {
                    return new String(txt);
                }

                return new String(txt, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "";
        }else
        {
            return "";
        }

    }

    public void setTxtStr(String txtStr) {
        if(txtStr!=null)
        {
            this.txtStr = txtStr;
            try {
                this.txt = txtStr.getBytes("utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    public void setTxt(byte[] txt) {
        if(txt!=null)
        {
            this.txt = txt;
        }

    }

    public EmeryencyPlanContent() {
        super();
    }

    public static Boolean getEncoding(String str) {
        String encode = "gbk";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是GBk;
                return true;      //是的话，返回true，以下代码同理
            }
        } catch (Exception exception) {
        }


        return false;        //如果都不是，说明输入的内容不属于常见的编码格式。
    }

    @Override
    public String toString() {
        return "EmeryencyPlanContent{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", createdtime=" + createdtime +
                ", priority=" + priority +
                ", treeId=" + treeId +
                ", status='" + status + '\'' +
                ", txt=" + Arrays.toString(txt) +
                ", txtStr='" + txtStr + '\'' +
                '}';
    }
}
