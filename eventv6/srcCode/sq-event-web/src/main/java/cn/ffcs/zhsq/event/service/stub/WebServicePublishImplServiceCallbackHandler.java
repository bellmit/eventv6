
/**
 * WebServicePublishImplServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package cn.ffcs.zhsq.event.service.stub;

    /**
     *  WebServicePublishImplServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class WebServicePublishImplServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public WebServicePublishImplServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public WebServicePublishImplServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for request method
            * override this method for handling normal response from request operation
            */
           public void receiveResultrequest(
        		   cn.ffcs.zhsq.event.service.stub.WebServicePublishImplServiceStub.RequestResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from request operation
           */
            public void receiveErrorrequest(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for requestAsBytes method
            * override this method for handling normal response from requestAsBytes operation
            */
           public void receiveResultrequestAsBytes(
        		   cn.ffcs.zhsq.event.service.stub.WebServicePublishImplServiceStub.RequestAsBytesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from requestAsBytes operation
           */
            public void receiveErrorrequestAsBytes(java.lang.Exception e) {
            }
                


    }
    