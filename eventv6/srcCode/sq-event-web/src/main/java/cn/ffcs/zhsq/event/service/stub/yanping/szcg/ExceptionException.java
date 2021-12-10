
/**
 * ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package cn.ffcs.zhsq.event.service.stub.yanping.szcg;

public class ExceptionException extends Exception{

    private static final long serialVersionUID = 1508396429623L;
    
    private SXDataPoolWebServiceStub.ExceptionE faultMessage;

    
        public ExceptionException() {
            super("ExceptionException");
        }

        public ExceptionException(String s) {
           super(s);
        }

        public ExceptionException(String s, Throwable ex) {
          super(s, ex);
        }

        public ExceptionException(Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(SXDataPoolWebServiceStub.ExceptionE msg){
       faultMessage = msg;
    }
    
    public SXDataPoolWebServiceStub.ExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    