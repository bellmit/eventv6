
/**
 * UserServiceServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package cn.ffcs.zhsq.times.stub.xgy;

    /**
     *  UserServiceServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class UserServiceServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public UserServiceServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public UserServiceServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for hello method
            * override this method for handling normal response from hello operation
            */
           public void receiveResulthello(
                    cn.ffcs.zhsq.times.stub.xgy.UserServiceServiceStub.HelloResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from hello operation
           */
            public void receiveErrorhello(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for andClientRequest method
            * override this method for handling normal response from andClientRequest operation
            */
           public void receiveResultandClientRequest(
                    cn.ffcs.zhsq.times.stub.xgy.UserServiceServiceStub.AndClientRequestResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from andClientRequest operation
           */
            public void receiveErrorandClientRequest(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for postEvent method
            * override this method for handling normal response from postEvent operation
            */
           public void receiveResultpostEvent(
                    cn.ffcs.zhsq.times.stub.xgy.UserServiceServiceStub.PostEventResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from postEvent operation
           */
            public void receiveErrorpostEvent(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for test method
            * override this method for handling normal response from test operation
            */
           public void receiveResulttest(
                    cn.ffcs.zhsq.times.stub.xgy.UserServiceServiceStub.TestResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from test operation
           */
            public void receiveErrortest(java.lang.Exception e) {
            }
                


    }
    