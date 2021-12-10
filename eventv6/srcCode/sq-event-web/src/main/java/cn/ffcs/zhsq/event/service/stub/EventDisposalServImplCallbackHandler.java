
/**
 * EventDisposalServImplCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package cn.ffcs.zhsq.event.service.stub;

    /**
     *  EventDisposalServImplCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class EventDisposalServImplCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public EventDisposalServImplCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public EventDisposalServImplCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for handlingTask method
            * override this method for handling normal response from handlingTask operation
            */
           public void receiveResulthandlingTask(
                    EventDisposalServImplStub.HandlingTaskResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from handlingTask operation
           */
            public void receiveErrorhandlingTask(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for rejectEvt method
            * override this method for handling normal response from rejectEvt operation
            */
           public void receiveResultrejectEvt(
                    EventDisposalServImplStub.RejectEvtResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from rejectEvt operation
           */
            public void receiveErrorrejectEvt(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for startCloseEvt method
            * override this method for handling normal response from startCloseEvt operation
            */
           public void receiveResultstartCloseEvt(
                    EventDisposalServImplStub.StartCloseEvtResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from startCloseEvt operation
           */
            public void receiveErrorstartCloseEvt(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for evaluateEvent method
            * override this method for handling normal response from evaluateEvent operation
            */
           public void receiveResultevaluateEvent(
                    EventDisposalServImplStub.EvaluateEventResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from evaluateEvent operation
           */
            public void receiveErrorevaluateEvent(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for readInputStream method
            * override this method for handling normal response from readInputStream operation
            */
           public void receiveResultreadInputStream(
                    EventDisposalServImplStub.ReadInputStreamResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from readInputStream operation
           */
            public void receiveErrorreadInputStream(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for startEvt method
            * override this method for handling normal response from startEvt operation
            */
           public void receiveResultstartEvt(
                    EventDisposalServImplStub.StartEvtResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from startEvt operation
           */
            public void receiveErrorstartEvt(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for syncTask method
            * override this method for handling normal response from syncTask operation
            */
           public void receiveResultsyncTask(
                    EventDisposalServImplStub.SyncTaskResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from syncTask operation
           */
            public void receiveErrorsyncTask(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for closeEvent method
            * override this method for handling normal response from closeEvent operation
            */
           public void receiveResultcloseEvent(
                    EventDisposalServImplStub.CloseEventResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from closeEvent operation
           */
            public void receiveErrorcloseEvent(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for shuntEvent method
            * override this method for handling normal response from shuntEvent operation
            */
           public void receiveResultshuntEvent(
                    EventDisposalServImplStub.ShuntEventResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from shuntEvent operation
           */
            public void receiveErrorshuntEvent(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for userInfo method
            * override this method for handling normal response from userInfo operation
            */
           public void receiveResultuserInfo(
                    EventDisposalServImplStub.UserInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from userInfo operation
           */
            public void receiveErroruserInfo(Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for checkRejectEvtData method
            * override this method for handling normal response from checkRejectEvtData operation
            */
           public void receiveResultcheckRejectEvtData(
                    EventDisposalServImplStub.CheckRejectEvtDataResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from checkRejectEvtData operation
           */
            public void receiveErrorcheckRejectEvtData(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for closeEvt method
            * override this method for handling normal response from closeEvt operation
            */
           public void receiveResultcloseEvt(
                    EventDisposalServImplStub.CloseEvtResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from closeEvt operation
           */
            public void receiveErrorcloseEvt(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for evlEvt method
            * override this method for handling normal response from evlEvt operation
            */
           public void receiveResultevlEvt(
                    EventDisposalServImplStub.EvlEvtResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from evlEvt operation
           */
            public void receiveErrorevlEvt(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for flowTask method
            * override this method for handling normal response from flowTask operation
            */
           public void receiveResultflowTask(
                    EventDisposalServImplStub.FlowTaskResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from flowTask operation
           */
            public void receiveErrorflowTask(Exception e) {
            }
                


    }
    