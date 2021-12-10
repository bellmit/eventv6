
/**
 * SendDataServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package cn.ffcs.zhsq.event.service.stub.gansu;

    /**
     *  SendDataServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class SendDataServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public SendDataServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public SendDataServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for archiveCase method
            * override this method for handling normal response from archiveCase operation
            */
           public void receiveResultarchiveCase(
                    cn.ffcs.zhsq.event.service.stub.gansu.SendDataServiceStub.ArchiveCaseResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from archiveCase operation
           */
            public void receiveErrorarchiveCase(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for feedbackResult method
            * override this method for handling normal response from feedbackResult operation
            */
           public void receiveResultfeedbackResult(
                    cn.ffcs.zhsq.event.service.stub.gansu.SendDataServiceStub.FeedbackResultResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from feedbackResult operation
           */
            public void receiveErrorfeedbackResult(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for callbackCase method
            * override this method for handling normal response from callbackCase operation
            */
           public void receiveResultcallbackCase(
                    cn.ffcs.zhsq.event.service.stub.gansu.SendDataServiceStub.CallbackCaseResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from callbackCase operation
           */
            public void receiveErrorcallbackCase(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for process method
            * override this method for handling normal response from process operation
            */
           public void receiveResultprocess(
                    cn.ffcs.zhsq.event.service.stub.gansu.SendDataServiceStub.ProcessResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from process operation
           */
            public void receiveErrorprocess(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for excuteException method
            * override this method for handling normal response from excuteException operation
            */
           public void receiveResultexcuteException(
                    cn.ffcs.zhsq.event.service.stub.gansu.SendDataServiceStub.ExcuteExceptionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from excuteException operation
           */
            public void receiveErrorexcuteException(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for closeCaseResult method
            * override this method for handling normal response from closeCaseResult operation
            */
           public void receiveResultcloseCaseResult(
                    cn.ffcs.zhsq.event.service.stub.gansu.SendDataServiceStub.CloseCaseResultResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from closeCaseResult operation
           */
            public void receiveErrorcloseCaseResult(java.lang.Exception e) {
            }
                


    }
    