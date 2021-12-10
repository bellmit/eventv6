
/**
 * EventDisposalServImplIOExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package cn.ffcs.zhsq.event.service.stub;

public class EventDisposalServImplIOExceptionException extends Exception{

    private static final long serialVersionUID = 1498137472606L;
    
    private EventDisposalServImplStub.EventDisposalServImplIOException faultMessage;

    
        public EventDisposalServImplIOExceptionException() {
            super("EventDisposalServImplIOExceptionException");
        }

        public EventDisposalServImplIOExceptionException(String s) {
           super(s);
        }

        public EventDisposalServImplIOExceptionException(String s, Throwable ex) {
          super(s, ex);
        }

        public EventDisposalServImplIOExceptionException(Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(EventDisposalServImplStub.EventDisposalServImplIOException msg){
       faultMessage = msg;
    }
    
    public EventDisposalServImplStub.EventDisposalServImplIOException getFaultMessage(){
       return faultMessage;
    }
}
    