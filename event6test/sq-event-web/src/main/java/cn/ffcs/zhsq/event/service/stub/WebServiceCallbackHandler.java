
/**
 * WebServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package cn.ffcs.zhsq.event.service.stub;

    /**
     *  WebServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class WebServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public WebServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public WebServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for flow_Close method
            * override this method for handling normal response from flow_Close operation
            */
           public void receiveResultflow_Close(
                    cn.ffcs.zhsq.event.service.stub.WebServiceStub.Flow_CloseResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from flow_Close operation
           */
            public void receiveErrorflow_Close(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for flow_Next method
            * override this method for handling normal response from flow_Next operation
            */
           public void receiveResultflow_Next(
                    cn.ffcs.zhsq.event.service.stub.WebServiceStub.Flow_NextResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from flow_Next operation
           */
            public void receiveErrorflow_Next(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for flow_Create method
            * override this method for handling normal response from flow_Create operation
            */
           public void receiveResultflow_Create(
                    cn.ffcs.zhsq.event.service.stub.WebServiceStub.Flow_CreateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from flow_Create operation
           */
            public void receiveErrorflow_Create(java.lang.Exception e) {
            }
                


    }
    