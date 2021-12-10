
/**
 * MessagePushCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package cn.ffcs.zhsq.event.service.stub.jiangyin.XunfangApp;

    /**
     *  MessagePushCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class MessagePushCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public MessagePushCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public MessagePushCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for closeEvt method
            * override this method for handling normal response from closeEvt operation
            */
           public void receiveResultcloseEvt(
                    MessagePushStub.CloseEvtResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from closeEvt operation
           */
            public void receiveErrorcloseEvt(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for pushMessahe method
            * override this method for handling normal response from pushMessahe operation
            */
           public void receiveResultpushMessahe(
                    MessagePushStub.PushMessaheResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from pushMessahe operation
           */
            public void receiveErrorpushMessahe(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for rejectEvt method
            * override this method for handling normal response from rejectEvt operation
            */
           public void receiveResultrejectEvt(
                    MessagePushStub.RejectEvtResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from rejectEvt operation
           */
            public void receiveErrorrejectEvt(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for startEvt method
            * override this method for handling normal response from startEvt operation
            */
           public void receiveResultstartEvt(
                    MessagePushStub.StartEvtResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from startEvt operation
           */
            public void receiveErrorstartEvt(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for pushRejectEvt method
            * override this method for handling normal response from pushRejectEvt operation
            */
           public void receiveResultpushRejectEvt(
                    MessagePushStub.PushRejectEvtResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from pushRejectEvt operation
           */
            public void receiveErrorpushRejectEvt(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for pushEvlEvt method
            * override this method for handling normal response from pushEvlEvt operation
            */
           public void receiveResultpushEvlEvt(
                    MessagePushStub.PushEvlEvtResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from pushEvlEvt operation
           */
            public void receiveErrorpushEvlEvt(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for pushCloseEvt method
            * override this method for handling normal response from pushCloseEvt operation
            */
           public void receiveResultpushCloseEvt(
                    MessagePushStub.PushCloseEvtResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from pushCloseEvt operation
           */
            public void receiveErrorpushCloseEvt(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for evlEvt method
            * override this method for handling normal response from evlEvt operation
            */
           public void receiveResultevlEvt(
                    MessagePushStub.EvlEvtResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from evlEvt operation
           */
            public void receiveErrorevlEvt(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for pushMessageInfo method
            * override this method for handling normal response from pushMessageInfo operation
            */
           public void receiveResultpushMessageInfo(
                    MessagePushStub.PushMessageInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from pushMessageInfo operation
           */
            public void receiveErrorpushMessageInfo(Exception e) {
            }
                


    }
    