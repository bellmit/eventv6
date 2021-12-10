
/**
 * EventDockingServiceImplCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package cn.ffcs.zhsq.event.service.stub;

    /**
     *  EventDockingServiceImplCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class EventDockingServiceImplCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public EventDockingServiceImplCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public EventDockingServiceImplCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for rejectEvent method
            * override this method for handling normal response from rejectEvent operation
            */
           public void receiveResultrejectEvent(
                    cn.ffcs.zhsq.event.service.stub.EventDockingServiceImplStub.RejectEventResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from rejectEvent operation
           */
            public void receiveErrorrejectEvent(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for closeEvent method
            * override this method for handling normal response from closeEvent operation
            */
           public void receiveResultcloseEvent(
                    cn.ffcs.zhsq.event.service.stub.EventDockingServiceImplStub.CloseEventResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from closeEvent operation
           */
            public void receiveErrorcloseEvent(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for syncEvent method
            * override this method for handling normal response from syncEvent operation
            */
           public void receiveResultsyncEvent(
                    cn.ffcs.zhsq.event.service.stub.EventDockingServiceImplStub.SyncEventResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from syncEvent operation
           */
            public void receiveErrorsyncEvent(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for reportEvent method
            * override this method for handling normal response from reportEvent operation
            */
           public void receiveResultreportEvent(
                    cn.ffcs.zhsq.event.service.stub.EventDockingServiceImplStub.ReportEventResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from reportEvent operation
           */
            public void receiveErrorreportEvent(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for test method
            * override this method for handling normal response from test operation
            */
           public void receiveResulttest(
                    cn.ffcs.zhsq.event.service.stub.EventDockingServiceImplStub.TestResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from test operation
           */
            public void receiveErrortest(java.lang.Exception e) {
            }
                


    }
    