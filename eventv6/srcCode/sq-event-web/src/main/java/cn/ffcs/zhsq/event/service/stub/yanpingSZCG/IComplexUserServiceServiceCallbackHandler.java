
/**
 * IComplexUserServiceServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package cn.ffcs.zhsq.event.service.stub.yanpingSZCG;

    /**
     *  IComplexUserServiceServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class IComplexUserServiceServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public IComplexUserServiceServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public IComplexUserServiceServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getPublicDisOutTotal method
            * override this method for handling normal response from getPublicDisOutTotal operation
            */
           public void receiveResultgetPublicDisOutTotal(
                    cn.ffcs.zhsq.event.service.stub.yanpingSZCG.IComplexUserServiceServiceStub.GetPublicDisOutTotalResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getPublicDisOutTotal operation
           */
            public void receiveErrorgetPublicDisOutTotal(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getPublicEnterTotal method
            * override this method for handling normal response from getPublicEnterTotal operation
            */
           public void receiveResultgetPublicEnterTotal(
                    cn.ffcs.zhsq.event.service.stub.yanpingSZCG.IComplexUserServiceServiceStub.GetPublicEnterTotalResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getPublicEnterTotal operation
           */
            public void receiveErrorgetPublicEnterTotal(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getUserCodeFromToken method
            * override this method for handling normal response from getUserCodeFromToken operation
            */
           public void receiveResultgetUserCodeFromToken(
                    cn.ffcs.zhsq.event.service.stub.yanpingSZCG.IComplexUserServiceServiceStub.GetUserCodeFromTokenResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getUserCodeFromToken operation
           */
            public void receiveErrorgetUserCodeFromToken(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getUserCodeFromUserName method
            * override this method for handling normal response from getUserCodeFromUserName operation
            */
           public void receiveResultgetUserCodeFromUserName(
                    cn.ffcs.zhsq.event.service.stub.yanpingSZCG.IComplexUserServiceServiceStub.GetUserCodeFromUserNameResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getUserCodeFromUserName operation
           */
            public void receiveErrorgetUserCodeFromUserName(Exception e) {
            }
                


    }
    