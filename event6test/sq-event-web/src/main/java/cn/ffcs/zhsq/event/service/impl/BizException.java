
/**
 * BizException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package cn.ffcs.zhsq.event.service.impl;

public class BizException extends java.lang.Exception{

    private static final long serialVersionUID = 1432697024283L;
    
    private cn.ffcs.zhsq.event.service.impl.DailyIncidentServiceStub.BizException faultMessage;

    
        public BizException() {
            super("BizException");
        }

        public BizException(java.lang.String s) {
           super(s);
        }

        public BizException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public BizException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(cn.ffcs.zhsq.event.service.impl.DailyIncidentServiceStub.BizException msg){
       faultMessage = msg;
    }
    
    public cn.ffcs.zhsq.event.service.impl.DailyIncidentServiceStub.BizException getFaultMessage(){
       return faultMessage;
    }
}
    