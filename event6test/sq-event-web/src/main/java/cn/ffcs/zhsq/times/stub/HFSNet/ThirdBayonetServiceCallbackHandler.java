
/**
 * ThirdBayonetServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package cn.ffcs.zhsq.times.stub.HFSNet;

    /**
     *  ThirdBayonetServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ThirdBayonetServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ThirdBayonetServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ThirdBayonetServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for deleteCrossingInfo method
            * override this method for handling normal response from deleteCrossingInfo operation
            */
           public void receiveResultdeleteCrossingInfo(
                    cn.ffcs.zhsq.times.stub.HFSNet.ThirdBayonetServiceStub.DeleteCrossingInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteCrossingInfo operation
           */
            public void receiveErrordeleteCrossingInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAllCrossingInfo method
            * override this method for handling normal response from getAllCrossingInfo operation
            */
           public void receiveResultgetAllCrossingInfo(
                    cn.ffcs.zhsq.times.stub.HFSNet.ThirdBayonetServiceStub.GetAllCrossingInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAllCrossingInfo operation
           */
            public void receiveErrorgetAllCrossingInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for searchVehicleInfo method
            * override this method for handling normal response from searchVehicleInfo operation
            */
           public void receiveResultsearchVehicleInfo(
                    cn.ffcs.zhsq.times.stub.HFSNet.ThirdBayonetServiceStub.SearchVehicleInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from searchVehicleInfo operation
           */
            public void receiveErrorsearchVehicleInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deletePlateAlarmInfo method
            * override this method for handling normal response from deletePlateAlarmInfo operation
            */
           public void receiveResultdeletePlateAlarmInfo(
                    cn.ffcs.zhsq.times.stub.HFSNet.ThirdBayonetServiceStub.DeletePlateAlarmInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deletePlateAlarmInfo operation
           */
            public void receiveErrordeletePlateAlarmInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modifyCrossingInfo method
            * override this method for handling normal response from modifyCrossingInfo operation
            */
           public void receiveResultmodifyCrossingInfo(
                    cn.ffcs.zhsq.times.stub.HFSNet.ThirdBayonetServiceStub.ModifyCrossingInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from modifyCrossingInfo operation
           */
            public void receiveErrormodifyCrossingInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modifyPlateAlarmInfo method
            * override this method for handling normal response from modifyPlateAlarmInfo operation
            */
           public void receiveResultmodifyPlateAlarmInfo(
                    cn.ffcs.zhsq.times.stub.HFSNet.ThirdBayonetServiceStub.ModifyPlateAlarmInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from modifyPlateAlarmInfo operation
           */
            public void receiveErrormodifyPlateAlarmInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for insertPlateAlarmInfo method
            * override this method for handling normal response from insertPlateAlarmInfo operation
            */
           public void receiveResultinsertPlateAlarmInfo(
                    cn.ffcs.zhsq.times.stub.HFSNet.ThirdBayonetServiceStub.InsertPlateAlarmInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from insertPlateAlarmInfo operation
           */
            public void receiveErrorinsertPlateAlarmInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for insertCrossingInfo method
            * override this method for handling normal response from insertCrossingInfo operation
            */
           public void receiveResultinsertCrossingInfo(
                    cn.ffcs.zhsq.times.stub.HFSNet.ThirdBayonetServiceStub.InsertCrossingInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from insertCrossingInfo operation
           */
            public void receiveErrorinsertCrossingInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for initSystem method
            * override this method for handling normal response from initSystem operation
            */
           public void receiveResultinitSystem(
                    cn.ffcs.zhsq.times.stub.HFSNet.ThirdBayonetServiceStub.InitSystemResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from initSystem operation
           */
            public void receiveErrorinitSystem(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getRecentMessage method
            * override this method for handling normal response from getRecentMessage operation
            */
           public void receiveResultgetRecentMessage(
                    cn.ffcs.zhsq.times.stub.HFSNet.ThirdBayonetServiceStub.GetRecentMessageResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getRecentMessage operation
           */
            public void receiveErrorgetRecentMessage(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for insertVehicleInfo method
            * override this method for handling normal response from insertVehicleInfo operation
            */
           public void receiveResultinsertVehicleInfo(
                    cn.ffcs.zhsq.times.stub.HFSNet.ThirdBayonetServiceStub.InsertVehicleInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from insertVehicleInfo operation
           */
            public void receiveErrorinsertVehicleInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for undoPlateAlarmInfo method
            * override this method for handling normal response from undoPlateAlarmInfo operation
            */
           public void receiveResultundoPlateAlarmInfo(
                    cn.ffcs.zhsq.times.stub.HFSNet.ThirdBayonetServiceStub.UndoPlateAlarmInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from undoPlateAlarmInfo operation
           */
            public void receiveErrorundoPlateAlarmInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for searchCrossingInfo method
            * override this method for handling normal response from searchCrossingInfo operation
            */
           public void receiveResultsearchCrossingInfo(
                    cn.ffcs.zhsq.times.stub.HFSNet.ThirdBayonetServiceStub.SearchCrossingInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from searchCrossingInfo operation
           */
            public void receiveErrorsearchCrossingInfo(java.lang.Exception e) {
            }
                


    }
    