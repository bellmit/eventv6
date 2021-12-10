
/**
 * ThirdService_NewCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package cn.ffcs.zhsq.event.service.stub.jiangyin;

    /**
     *  ThirdService_NewCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ThirdService_NewCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ThirdService_NewCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ThirdService_NewCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getFlow method
            * override this method for handling normal response from getFlow operation
            */
           public void receiveResultgetFlow(
                    ThirdService_NewStub.GetFlowResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getFlow operation
           */
            public void receiveErrorgetFlow(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for submitThird method
            * override this method for handling normal response from submitThird operation
            */
           public void receiveResultsubmitThird(
                    ThirdService_NewStub.SubmitThirdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from submitThird operation
           */
            public void receiveErrorsubmitThird(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for submitThirdToSub method
            * override this method for handling normal response from submitThirdToSub operation
            */
           public void receiveResultsubmitThirdToSub(
                    ThirdService_NewStub.SubmitThirdToSubResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from submitThirdToSub operation
           */
            public void receiveErrorsubmitThirdToSub(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getVillages method
            * override this method for handling normal response from getVillages operation
            */
           public void receiveResultgetVillages(
                    ThirdService_NewStub.GetVillagesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getVillages operation
           */
            public void receiveErrorgetVillages(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getOrgs method
            * override this method for handling normal response from getOrgs operation
            */
           public void receiveResultgetOrgs(
                    ThirdService_NewStub.GetOrgsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getOrgs operation
           */
            public void receiveErrorgetOrgs(Exception e) {
            }
                


    }
    