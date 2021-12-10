
/**
 * ReceiveRecServiceServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package cn.ffcs.zhsq.event.service.stub.sgdj;

    /**
     *  ReceiveRecServiceServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ReceiveRecServiceServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ReceiveRecServiceServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ReceiveRecServiceServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for transResult method
            * override this method for handling normal response from transResult operation
            */
           public void receiveResulttransResult(
                    cn.ffcs.zhsq.event.service.stub.sgdj.ReceiveRecServiceServiceStub.TransResultResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from transResult operation
           */
            public void receiveErrortransResult(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for process method
            * override this method for handling normal response from process operation
            */
           public void receiveResultprocess(
                    cn.ffcs.zhsq.event.service.stub.sgdj.ReceiveRecServiceServiceStub.ProcessResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from process operation
           */
            public void receiveErrorprocess(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for uploadPic method
            * override this method for handling normal response from uploadPic operation
            */
           public void receiveResultuploadPic(
                    cn.ffcs.zhsq.event.service.stub.sgdj.ReceiveRecServiceServiceStub.UploadPicResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from uploadPic operation
           */
            public void receiveErroruploadPic(java.lang.Exception e) {
            }
                


    }
    