
/**
 * XmhbSjjhServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package cn.ffcs.zhsq.event.service.stub.yanping.zhst;

    /**
     *  XmhbSjjhServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class XmhbSjjhServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public XmhbSjjhServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public XmhbSjjhServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getPointData method
            * override this method for handling normal response from getPointData operation
            */
           public void receiveResultgetPointData(
                    cn.ffcs.zhsq.event.service.stub.yanping.zhst.XmhbSjjhServiceStub.GetPointDataResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getPointData operation
           */
            public void receiveErrorgetPointData(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addEvent method
            * override this method for handling normal response from addEvent operation
            */
           public void receiveResultaddEvent(
                    cn.ffcs.zhsq.event.service.stub.yanping.zhst.XmhbSjjhServiceStub.AddEventResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addEvent operation
           */
            public void receiveErroraddEvent(Exception e) {
            }
                


    }
    